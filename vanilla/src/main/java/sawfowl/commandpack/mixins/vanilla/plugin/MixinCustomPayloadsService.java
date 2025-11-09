package sawfowl.commandpack.mixins.vanilla.plugin;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
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
import org.spongepowered.api.network.channel.raw.play.RawPlayDataHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.api.network.listeners.PacketListener;
import sawfowl.commandpack.api.network.listeners.RawPacketListener;
import sawfowl.commandpack.api.network.packets.RawPacket;
import sawfowl.commandpack.api.network.packets.SerializedPacket;
import sawfowl.commandpack.apiclasses.network.CustomPayloadsServiceImpl;
import sawfowl.commandpack.apiclasses.network.RawPacketImpl;
import sawfowl.commandpack.apiclasses.network.SerializedPacketBuilder.SerializedPacketImpl;

@Mixin(CustomPayloadsServiceImpl.class)
public abstract class MixinCustomPayloadsService {

	@Shadow @Final private CommandPackInstance plugin;
	@Shadow private boolean finished;
	@Shadow private Map<ResourceKey, RawDataChannel> spongeChannels = new HashMap<>();
	private Set<ResourceKey> spongeChannelsToRegister = new HashSet<>();

	@Overwrite
	public void registerChannel(ResourceKey channel) {
		if(finished) {
			plugin.getLocales().getSystemLocale().getDebug().getFinishedRegisterNetworkData(channel);
		} else if(!spongeChannelsToRegister.contains(channel)) spongeChannelsToRegister.add(channel);
	}

	@Overwrite
	private void spongeEvent(RegisterChannelEvent event) {
		spongeChannelsToRegister.forEach(id -> {
			var existChannel = Sponge.channelManager().get(id).filter(channel -> channel instanceof RawDataChannel);
			if(existChannel.isPresent()) {
				if(existChannel.get() instanceof RawDataChannel raw) {
					raw.play().addHandler(ServerConnectionState.Game.class, new SpongeHandler(id));
					spongeChannels.put(id, raw);
				}
			} else {
				var channel = event.register(id, RawDataChannel.class);
				channel.play().addHandler(ServerConnectionState.Game.class, new SpongeHandler(id));
				spongeChannels.put(id, channel);
			}
		});
	}

	@Shadow abstract Collection<RawPacketListener> getRawListeners(ResourceKey channel);

	@Shadow abstract Collection<PacketListener<?>> getListeners(ResourceKey channel);

	@Shadow abstract boolean containsSerializer(ResourceKey channel);

	@Shadow abstract boolean containsBufferSerializer(ResourceKey channel);

	@Shadow abstract Function<String, SerializedPacket<?>> getSerializer(ResourceKey channel);

	@Shadow abstract Function<ChannelBuf, SerializedPacket<?>> getBufferSerializer(ResourceKey channel);

	private class SpongeHandler implements RawPlayDataHandler<ServerConnectionState.Game> {

		private final ResourceKey channel;
		SpongeHandler(ResourceKey channel) {
			this.channel = channel;
		}

		@Override
		public void handlePayload(ChannelBuf data, ServerConnectionState.Game state) {
			handle(MixinServerPlayer.cast(state.player()), new RawPacketImpl(channel, data, data.available() > 0 ? new String(data.readBytes(data.available()), StandardCharsets.UTF_8) : ""));
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
		
	}

}
