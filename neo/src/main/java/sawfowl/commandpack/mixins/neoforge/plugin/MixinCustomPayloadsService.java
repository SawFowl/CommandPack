package sawfowl.commandpack.mixins.neoforge.plugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.lifecycle.RegisterChannelEvent;
import org.spongepowered.api.network.ServerConnectionState;
import org.spongepowered.api.network.channel.ChannelBuf;
import org.spongepowered.api.network.channel.raw.RawDataChannel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.network.listeners.PacketListener;
import sawfowl.commandpack.api.network.listeners.RawPacketListener;
import sawfowl.commandpack.api.network.packets.SerializedPacket;
import sawfowl.commandpack.apiclasses.network.CustomPayloadsServiceImpl;
import sawfowl.commandpack.apiclasses.network.RawPacketImpl;
import sawfowl.commandpack.apiclasses.network.SpongeChannelHandler;

@Mixin(CustomPayloadsServiceImpl.class)
public abstract class MixinCustomPayloadsService {

	//private ModContainer modContainer;
	@Shadow private boolean finished;
	@Shadow @Final private CommandPackInstance plugin;
	@Shadow private Set<ResourceKey> needRecode;
	@Shadow private Map<ResourceKey, RawDataChannel> spongeChannels;
	private Set<ResourceKey> spongeChannelsToRegister = new HashSet<>();

	/*@Overwrite
	private void init() {
		modContainer = ModList.get().getModContainerById("commandpack").get();
		modContainer.getEventBus().register(this);
	}*/

	@Overwrite
	public void registerChannel(ResourceKey channel) {
		if(finished) {
			plugin.getLocales().getSystemAsReference().getDebug().getFinishedRegisterNetworkData(channel);
		} else if(!spongeChannelsToRegister.contains(channel)) spongeChannelsToRegister.add(channel);
	}

	@Overwrite
	private void spongeEvent(RegisterChannelEvent event) {
		spongeChannelsToRegister.forEach(id -> {
			var existChannel = Sponge.channelManager().get(id).filter(channel -> channel instanceof RawDataChannel);
			if(existChannel.isPresent()) {
				if(existChannel.get() instanceof RawDataChannel raw) {
					raw.play().addHandler(ServerConnectionState.Game.class, new SpongeChannelHandler(plugin, id));
					spongeChannels.put(id, raw);
				}
			} else {
				var channel = event.register(id, RawDataChannel.class);
				channel.play().addHandler(ServerConnectionState.Game.class, new SpongeChannelHandler(plugin, id));
				spongeChannels.put(id, channel);
			}
		});
	}
/*
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
	private PayloadRegistration<?> createNewHandler(PayloadRegistration existingHandler) {//⬅
		needRecode.add((ResourceKey) (Object) existingHandler.type().id());
		return new PayloadRegistration(existingHandler.type(), existingHandler.codec(), (payload, context) -> {
			// Since the data channel has already been registered by another mod, then in this place you need to use the codec that was previously registered in NeoForge.
			// This avoids the ClassCastException error.
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
			if(context.player() instanceof MixinServerPlayer player && buffer.hasArray())
				handle(
					player,
					new RawPacketImpl((ResourceKey) (Object) payload.type().id(),
						(ChannelBuf) buffer,
						buffer.readableBytes() > 0 ? buffer.readCharSequence(buffer.readableBytes(), StandardCharsets.UTF_8).toString() : ""
					)
				);
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
*/
	@Shadow abstract Collection<RawPacketListener> getRawListeners(ResourceKey channel);

	@Shadow abstract Map<CustomPacketPayload.Type<RawPacketImpl>, StreamCodec<ByteBuf, RawPacketImpl>> getCodecs();

	@Shadow abstract Collection<PacketListener<?>> getListeners(ResourceKey channel);

	@Shadow abstract boolean containsSerializer(ResourceKey channel);

	@Shadow abstract boolean containsBufferSerializer(ResourceKey channel);

	@Shadow abstract Function<String, SerializedPacket<?>> getSerializer(ResourceKey channel);

	@Shadow abstract Function<ChannelBuf, SerializedPacket<?>> getBufferSerializer(ResourceKey channel);

}
