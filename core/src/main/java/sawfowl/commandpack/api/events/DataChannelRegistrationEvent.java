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

	<T> void registerSerializer(ResourceKey channel, Function<String, SerializedPacket<T>> function);

	<T> void registerBufferSerializer(ResourceKey channel, Function<ChannelBuf, SerializedPacket<T>> function);

	void registerRawListener(PluginContainer container, ResourceKey channel, RawPacketListener listener);

	<T> void registerListener(PluginContainer container, ResourceKey channel, PacketListener<T> listener);

	default void registerChannel(ResourceKey channel, PluginContainer container, RawPacketListener listener) {
		registerChannel(channel);
		registerRawListener(container, channel, listener);
	}

	default <T> void registerChannel(ResourceKey channel, PluginContainer container, Function<String, SerializedPacket<T>> serializer, PacketListener<T> listener) {
		registerChannel(channel);
		registerSerializer(channel, serializer);
		registerListener(container, channel, listener);
	}

	default <T> void registerChannel(PluginContainer container, ResourceKey channel, Function<ChannelBuf, SerializedPacket<T>> serializer, PacketListener<T> listener) {
		registerChannel(channel);
		registerBufferSerializer(channel, serializer);
		registerListener(container, channel, listener);
	}

}
