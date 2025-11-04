package sawfowl.commandpack.api.network;

import java.util.Optional;
import java.util.function.Function;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.network.channel.ChannelBuf;
import org.spongepowered.plugin.PluginContainer;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import sawfowl.commandpack.api.network.listeners.PacketListener;
import sawfowl.commandpack.api.network.listeners.RawPacketListener;
import sawfowl.commandpack.api.network.packets.RawPacket;
import sawfowl.commandpack.api.network.packets.SerializedPacket;

public interface CustomPayloadsService {

	void registerRawCodecAndChannel(ResourceKey channel);

	Optional<StreamCodec<RegistryFriendlyByteBuf, RawPacket>> findCodec(ResourceKey channel);

	void registerSerializer(ResourceKey channel, Function<String, SerializedPacket<?>> function);

	void registerBufferSerializer(ResourceKey channel, Function<ChannelBuf, SerializedPacket<?>> function);

	void registerRawListener(PluginContainer container, ResourceKey channel, RawPacketListener listener);

	void registerListener(PluginContainer container, ResourceKey channel, PacketListener<?> listener);

	void unregisterRawListener(PluginContainer container, ResourceKey channel);

	void unregisterListener(PluginContainer container, ResourceKey channel);

	void unregisterRawListeners(PluginContainer container);

	void unregisterAllListeners(PluginContainer container);

	void unregisterListeners(PluginContainer container);

}
