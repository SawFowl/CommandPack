package sawfowl.commandpack.mixins.neoforge.plugin;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.network.channel.ChannelBuf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.connection.ConnectionType;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
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
	@Shadow private Set<ResourceKey> needRecode;

	@Overwrite
	private void init() {
		modContainer = ModList.get().getModContainerById("commandpack").get();
		modContainer.getEventBus().register(this);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void register(RegisterPayloadHandlersEvent event) {
		Sponge.eventManager().post(new DataChannelRegistrationEventImpl(plugin));
		finished = true;
		register(event, NetworkRegistryAccessor.getPAYLOAD_REGISTRATIONS());
	}

	private void register(RegisterPayloadHandlersEvent event, Map<ConnectionProtocol, Map<ResourceLocation, PayloadRegistration<?>>> registrations) {
		getCodecs().forEach((type, codec) -> {
			var existingHandler = registrations.get(ConnectionProtocol.PLAY).get(type.id());
			if(existingHandler == null) {
				event.registrar("1")
				.optional()
				.playBidirectional(
					type,
					codec,
					(payload, context) -> {
						CommandPackInstance.getInstance().getLogger().warn("ИМЯ КЛАССА НАГРУЗКИ -> " + payload.getClass().getName());
						if (context.player() instanceof MixinServerPlayer player) {
							// Server-side packet, let plugin handle it
							handle(player, payload);
							return;
						}
					}
				);
			} else registrations.get(ConnectionProtocol.PLAY).put(type.id(), createNewHandler(existingHandler));
			existingHandler = null;
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private PayloadRegistration<?> createNewHandler(PayloadRegistration existingHandler) {
		needRecode.add((ResourceKey) (Object) existingHandler.type().id());
		return new PayloadRegistration(existingHandler.type(), existingHandler.codec(), (payload, context) -> {
			@Nullable StreamCodec<ByteBuf, CustomPacketPayload> codec = (@Nullable StreamCodec<ByteBuf, CustomPacketPayload>) NetworkRegistry.getCodec(payload.type().id(), ConnectionProtocol.PLAY, PacketFlow.SERVERBOUND);
			ByteBuf buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), null, ConnectionType.OTHER);
			try {
				codec.encode(buffer, payload);
			} catch (Exception e) {
				buffer = new FriendlyByteBuf(Unpooled.buffer());
				try {
					codec.encode(buffer, payload);
				} catch (Exception e2) {
				}
			}
			codec = null;
			if(context.player() instanceof MixinServerPlayer player && buffer.hasArray()) handle(player, new RawPacketImpl((ResourceKey) (Object) payload.type().id(), (ChannelBuf) buffer, buffer.readableBytes() > 0 ? buffer.readCharSequence(buffer.readableBytes(), StandardCharsets.UTF_8).toString() : ""));
			((IPayloadHandler)existingHandler.handler()).handle(payload, context);
			buffer = null;
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

	@Shadow abstract Map<CustomPacketPayload.Type<RawPacketImpl>, StreamCodec<ByteBuf, RawPacketImpl>> getCodecs();

	@Shadow abstract Collection<PacketListener<?>> getListeners(ResourceKey channel);

	@Shadow abstract boolean containsSerializer(ResourceKey channel);

	@Shadow abstract boolean containsBufferSerializer(ResourceKey channel);

	@Shadow abstract Function<String, SerializedPacket<?>> getSerializer(ResourceKey channel);

	@Shadow abstract Function<ChannelBuf, SerializedPacket<?>> getBufferSerializer(ResourceKey channel);

}
