package sawfowl.commandpack.mixins.neoforge.plugin;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.network.channel.ChannelBuf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistration;
import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.api.network.listeners.PacketListener;
import sawfowl.commandpack.api.network.listeners.RawPacketListener;
import sawfowl.commandpack.api.network.packets.RawPacket;
import sawfowl.commandpack.api.network.packets.SerializedPacket;
import sawfowl.commandpack.apiclasses.DataChannelRegistrationEventImpl;
import sawfowl.commandpack.apiclasses.network.CustomPayloadsServiceImpl;
import sawfowl.commandpack.apiclasses.network.RawPacketImpl;
import sawfowl.commandpack.apiclasses.network.SerializedPacketBuilder.SerializedPacketImpl;
import sawfowl.commandpack.mixins.neoforge.network.NetworkRegistryAccessor;

@Mixin(CustomPayloadsServiceImpl.class)
public abstract class MixinCustomPayloadsService {

	private ModContainer modContainer;
	@Shadow private boolean finished;
	@Shadow @Final private CommandPackInstance plugin;

	@Overwrite
	private void init() {
		modContainer = ModList.get().getModContainerById("commandpack").get();
		modContainer.getEventBus().register(this);
	}
	
	@SubscribeEvent
	public void register(RegisterPayloadHandlersEvent event) {
		Sponge.eventManager().post(new DataChannelRegistrationEventImpl(plugin));
		finished = true;
		register(event, NetworkRegistryAccessor.getPAYLOAD_REGISTRATIONS());
	}

	private void register(RegisterPayloadHandlersEvent event, Map<ConnectionProtocol, Map<ResourceLocation, PayloadRegistration<?>>> registrations) {
		getCodecs().forEach((type, codec) -> {
			if(!registrations.get(ConnectionProtocol.PLAY).containsKey(type.id())) {
				event.registrar("1")
				.optional()
				.playBidirectional(
					type,
					codec,
					(payload, context) -> {
						if (context.player() instanceof MixinServerPlayer player && payload instanceof RawPacket rawPacket) {
							// Server-side packet, let plugin handle it
							handle(player, rawPacket);
							return;
						}
					}
				);
			} else registrations.get(ConnectionProtocol.PLAY).put(type.id(), createNewHandler(registrations.get(ConnectionProtocol.PLAY).get(type.id()), type, codec));
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private PayloadRegistration<?> createNewHandler(PayloadRegistration<?> existingHandler, CustomPacketPayload.Type<RawPacketImpl> type, StreamCodec<RegistryFriendlyByteBuf, RawPacketImpl> codec) {
		return new PayloadRegistration(existingHandler.type(), existingHandler.codec(), (payload, context) -> {
			if (context.player() instanceof MixinServerPlayer player && payload instanceof RawPacket rawPacket) {
				// Server-side packet, let plugin handle it
				handle(player, rawPacket);
				((IPayloadHandler)existingHandler.handler()).handle(payload, context);
				return;
			}
		}, existingHandler.protocols(), existingHandler.flow(), existingHandler.version(), existingHandler.optional());
	}

	private void handle(MixinServerPlayer player, RawPacket rawPacket) {
		getRawListeners(rawPacket.channel()).forEach(listener -> listener.read(player, rawPacket));
		handleSerialized(player, rawPacket, containsSerializer(rawPacket.channel()), containsBufferSerializer(rawPacket.channel()));
	}

	@SuppressWarnings("unchecked")
	private void handleSerialized(MixinServerPlayer player, RawPacket rawPacket, boolean stringSerializer, boolean bufferSerializer) {
		if(stringSerializer || bufferSerializer) for(PacketListener<?> listener : getListeners(rawPacket.channel())) listener.read(player, serialize(rawPacket, bufferSerializer));
	}

	@SuppressWarnings("rawtypes")
	private SerializedPacket serialize(RawPacket packet, boolean bufferSerializer) {
		return ((SerializedPacketImpl) (bufferSerializer 
			?
			SerializedPacket.ofBuffer(packet.channel(), getBufferSerializer(packet.channel()))
			:
			SerializedPacket.of(packet.channel(), getSerializer(packet.channel()))))
		.apply(packet.getBuffer(), packet.getDataAsString());
	}

	@Shadow abstract Collection<RawPacketListener> getRawListeners(ResourceKey channel);

	@Shadow abstract Map<CustomPacketPayload.Type<RawPacketImpl>, StreamCodec<RegistryFriendlyByteBuf, RawPacketImpl>> getCodecs();

	@Shadow abstract Collection<PacketListener<?>> getListeners(ResourceKey channel);

	@Shadow abstract boolean containsSerializer(ResourceKey channel);

	@Shadow abstract boolean containsBufferSerializer(ResourceKey channel);

	@Shadow abstract Function<String, SerializedPacket<?>> getSerializer(ResourceKey channel);

	@Shadow abstract Function<ChannelBuf, SerializedPacket<?>> getBufferSerializer(ResourceKey channel);

}
