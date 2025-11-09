package sawfowl.commandpack.apiclasses;

import java.util.function.Function;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.network.channel.ChannelBuf;
import org.spongepowered.plugin.PluginContainer;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.events.DataChannelRegistrationEvent;
import sawfowl.commandpack.api.network.CustomPayloadsService;
import sawfowl.commandpack.api.network.listeners.PacketListener;
import sawfowl.commandpack.api.network.listeners.RawPacketListener;
import sawfowl.commandpack.api.network.packets.SerializedPacket;

public class DataChannelRegistrationEventImpl implements DataChannelRegistrationEvent {

	private final CommandPackInstance plugin;
	private Cause cause;
	public DataChannelRegistrationEventImpl(CommandPackInstance plugin) {
		this.plugin = plugin;
		cause = Cause.builder().append(plugin.getPluginContainer()).build(EventContext.builder().add(EventContextKeys.PLUGIN, plugin.getPluginContainer()).build());
	}

	@Override
	public Cause cause() {
		return cause;
	}

	@Override
	public void registerChannel(ResourceKey channel) {
		getService().registerChannel(channel);
	}

	@Override
	public void registerSerializer(ResourceKey channel, Function<String, SerializedPacket<?>> function) {
		getService().registerSerializer(channel, function);
	}

	@Override
	public void registerBufferSerializer(ResourceKey channel, Function<ChannelBuf, SerializedPacket<?>> function) {
		getService().registerBufferSerializer(channel, function);
	}

	@Override
	public void registerRawListener(PluginContainer container, ResourceKey channel, RawPacketListener listener) {
		getService().registerRawListener(container, channel, listener);
	}

	@Override
	public <T> void registerListener(PluginContainer container, ResourceKey channel, PacketListener<T> listener) {
		getService().registerListener(container, channel, listener);
	}

	private CustomPayloadsService getService() {
		return plugin.getPayloadsService();
	}

}
