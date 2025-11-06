package sawfowl.commandpack.api.events;

import java.util.function.Function;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.network.channel.ChannelBuf;
import org.spongepowered.plugin.PluginContainer;

import sawfowl.commandpack.api.network.listeners.PacketListener;
import sawfowl.commandpack.api.network.listeners.RawPacketListener;
import sawfowl.commandpack.api.network.packets.SerializedPacket;

public interface DataChannelRegistrationEvent extends Event {

	void registerChannel(ResourceKey channel);

	void registerSerializer(ResourceKey channel, Function<String, SerializedPacket<?>> function);

	void registerBufferSerializer(ResourceKey channel, Function<ChannelBuf, SerializedPacket<?>> function);

	void registerRawListener(PluginContainer container, ResourceKey channel, RawPacketListener listener);

	<T> void registerListener(PluginContainer container, ResourceKey channel, PacketListener<T> listener);

}
