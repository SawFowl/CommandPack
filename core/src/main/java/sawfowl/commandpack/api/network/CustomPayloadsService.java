package sawfowl.commandpack.api.network;

import java.util.function.Function;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.plugin.PluginContainer;

import sawfowl.commandpack.api.network.listeners.PacketListener;
import sawfowl.commandpack.api.network.listeners.RawPacketListener;
import sawfowl.commandpack.api.network.packets.SerializedPacket;

public interface CustomPayloadsService {

	void registerRawCodec(ResourceKey channel);

	void registerSerializer(ResourceKey channel, Function<String, SerializedPacket<?>> function);

	void registerRawListener(PluginContainer container, ResourceKey channel, RawPacketListener listener);

	void registerListener(PluginContainer container, ResourceKey channel, PacketListener<?> listener);

	void unregisterRawListener(PluginContainer container, ResourceKey channel);

	void unregisterListener(PluginContainer container, ResourceKey channel);

	void unregisterRawListeners(PluginContainer container);

	void unregisterAllListeners(PluginContainer container);

	void unregisterListeners(PluginContainer container);

}
