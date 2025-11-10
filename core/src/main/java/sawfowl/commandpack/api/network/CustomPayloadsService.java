package sawfowl.commandpack.api.network;

import java.util.function.Function;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.network.channel.ChannelBuf;
import org.spongepowered.plugin.PluginContainer;

import sawfowl.commandpack.api.network.listeners.PacketListener;
import sawfowl.commandpack.api.network.listeners.RawPacketListener;
import sawfowl.commandpack.api.network.packets.SerializedPacket;

public interface CustomPayloadsService {

	void registerChannel(ResourceKey channel);

	<T> void registerSerializer(ResourceKey channel, Function<String, SerializedPacket<T>> function);

	<T> void registerBufferSerializer(ResourceKey channel, Function<ChannelBuf, SerializedPacket<T>> function);

	void registerRawListener(PluginContainer container, ResourceKey channel, RawPacketListener listener);

	<T> void registerListener(PluginContainer container, ResourceKey channel, PacketListener<T> listener);

	void unregisterRawListener(PluginContainer container, ResourceKey channel);

	void unregisterListener(PluginContainer container, ResourceKey channel);

	void unregisterRawListeners(PluginContainer container);

	void unregisterAllListeners(PluginContainer container);

	void unregisterListeners(PluginContainer container);

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
