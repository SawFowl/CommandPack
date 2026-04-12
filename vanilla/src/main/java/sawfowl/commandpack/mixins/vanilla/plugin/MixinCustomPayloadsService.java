package sawfowl.commandpack.mixins.vanilla.plugin;

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
import org.spongepowered.asm.mixin.*;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.network.listeners.PacketListener;
import sawfowl.commandpack.api.network.listeners.RawPacketListener;
import sawfowl.commandpack.api.network.packets.SerializedPacket;
import sawfowl.commandpack.apiclasses.network.CustomPayloadsServiceImpl;
import sawfowl.commandpack.apiclasses.network.SpongeChannelHandler;

@Mixin(CustomPayloadsServiceImpl.class)
public abstract class MixinCustomPayloadsService {

	@Shadow @Final private CommandPackInstance plugin;
	@Shadow private boolean finished;
	@Shadow private Map<ResourceKey, RawDataChannel> spongeChannels;
	@Unique private Set<ResourceKey> commandPack$spongeChannelsToRegister = new HashSet<>();

	/**
	 * @author SawFowl
	 * @reason This is an internal method of the plugin.
	 */
	@Overwrite
	public void registerChannel(ResourceKey channel) {
		if(finished) {
			plugin.getLocales().getSystemAsReferenced().getDebug().getFinishedRegisterNetworkData(channel);
		} else commandPack$spongeChannelsToRegister.add(channel);
	}

	/**
	 * @author SawFowl
	 * @reason This is an internal method of the plugin.
	 */
	@Overwrite
	private void spongeEvent(RegisterChannelEvent event) {
		commandPack$spongeChannelsToRegister.forEach(id -> {
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

	@Shadow abstract Collection<RawPacketListener> getRawListeners(ResourceKey channel);

	@Shadow abstract Collection<PacketListener<?>> getListeners(ResourceKey channel);

	@Shadow abstract boolean containsSerializer(ResourceKey channel);

	@Shadow abstract boolean containsBufferSerializer(ResourceKey channel);

	@Shadow abstract Function<String, SerializedPacket<?>> getSerializer(ResourceKey channel);

	@Shadow abstract Function<ChannelBuf, SerializedPacket<?>> getBufferSerializer(ResourceKey channel);

}
