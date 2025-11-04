package sawfowl.commandpack.apiclasses.network;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.network.channel.ChannelBuf;
import org.spongepowered.plugin.PluginContainer;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.network.CustomPayloadsService;
import sawfowl.commandpack.api.network.listeners.PacketListener;
import sawfowl.commandpack.api.network.listeners.RawPacketListener;
import sawfowl.commandpack.api.network.packets.RawPacket;
import sawfowl.commandpack.api.network.packets.SerializedPacket;

public class CustomPayloadsServiceImpl implements CustomPayloadsService {

	private Map<CustomPacketPayload.Type<RawPacketImpl>, StreamCodec<RegistryFriendlyByteBuf, RawPacketImpl>> codecs = new HashMap<>();
	private Map<ResourceKey, Function<String, SerializedPacket<?>>> serializers = new HashMap<>();
	private Map<ResourceKey, Function<ChannelBuf, SerializedPacket<?>>> bufferSerializers = new HashMap<>();
	private Map<ResourceKey, Map<PluginContainer, RawPacketListener>> rawListeners = new HashMap<>();
	private Map<ResourceKey, Map<PluginContainer, PacketListener<?>>> listeners = new HashMap<>();
	private boolean finished = false;
	private final CommandPackInstance plugin;
	public CustomPayloadsServiceImpl(CommandPackInstance plugin) {
		this.plugin = plugin;
		init();
	}

	private void init() {}

	@Override
	public void registerRawCodecAndChannel(ResourceKey channel) {
		if(finished) {
			plugin.getLocales().getSystemLocale().getDebug().getFinishedRegisterNetworkData(channel);
		} else registerRawCodec(new Type<>((ResourceLocation) (Object) channel), channel);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<StreamCodec<RegistryFriendlyByteBuf, RawPacket>> findCodec(ResourceKey channel) {
		return codecs.entrySet().stream().filter(entry -> entry.getKey().id().equals((ResourceLocation) (Object) channel)).findFirst().map(entry -> (StreamCodec<RegistryFriendlyByteBuf, RawPacket>) (Object) entry.getValue());
	}

	@Override
	public void registerSerializer(ResourceKey channel, Function<String, SerializedPacket<?>> function) {
		if(!serializers.containsKey(channel)) serializers.put(channel, function);
	}

	@Override
	public void registerBufferSerializer(ResourceKey channel, Function<ChannelBuf, SerializedPacket<?>> function) {
		if(!bufferSerializers.containsKey(channel)) bufferSerializers.put(channel, function);
	}

	@Override
	public void registerRawListener(PluginContainer container, ResourceKey channel, RawPacketListener listener) {
		if(!rawListeners.containsKey(channel)) rawListeners.put(channel, new HashMap<>());
		if(rawListeners.get(channel).containsKey(container)) return;
		rawListeners.get(channel).put(container, listener);
	}

	@Override
	public void registerListener(PluginContainer container, ResourceKey channel, PacketListener<?> listener) {
		if(!listeners.containsKey(channel)) listeners.put(channel, new HashMap<>());
		if(listeners.get(channel).containsKey(container)) return;
		listeners.get(channel).put(container, listener);
	}

	@Override
	public void unregisterRawListener(PluginContainer container, ResourceKey channel) {
		if(rawListeners.containsKey(channel) && rawListeners.get(channel).containsKey(container)) rawListeners.get(channel).remove(container);
	}

	@Override
	public void unregisterListener(PluginContainer container, ResourceKey channel) {
		if(listeners.containsKey(channel) && listeners.get(channel).containsKey(container)) listeners.get(channel).remove(container);
	}

	@Override
	public void unregisterRawListeners(PluginContainer container) {
		rawListeners.values().forEach(map -> {
			if(map.containsKey(container)) map.remove(container);
		});
	}

	@Override
	public void unregisterAllListeners(PluginContainer container) {
		rawListeners.values().forEach(map -> {
			if(map.containsKey(container)) map.remove(container);
		});
		listeners.values().forEach(map -> {
			if(map.containsKey(container)) map.remove(container);
		});
	}

	@Override
	public void unregisterListeners(PluginContainer container) {
		listeners.values().forEach(map -> {
			if(map.containsKey(container)) map.remove(container);
		});
	}

	Map<CustomPacketPayload.Type<RawPacketImpl>, StreamCodec<RegistryFriendlyByteBuf, RawPacketImpl>> getCodecs() {
		return codecs;
	}

	boolean containsSerializer(ResourceKey channel) {
		return serializers.containsKey(channel);
	}

	boolean containsBufferSerializer(ResourceKey channel) {
		return bufferSerializers.containsKey(channel);
	}

	Function<String, SerializedPacket<?>> getSerializer(ResourceKey channel) {
		return serializers.get(channel);
	}

	Function<ChannelBuf, SerializedPacket<?>> getBufferSerializer(ResourceKey channel) {
		return bufferSerializers.get(channel);
	}

	Collection<RawPacketListener> getRawListeners(ResourceKey channel) {
		return rawListeners.containsKey(channel) ? rawListeners.get(channel).values() : Collections.emptyList();
	}

	Collection<PacketListener<?>> getListeners(ResourceKey channel) {
		return listeners.containsKey(channel) ? listeners.get(channel).values() : Collections.emptyList();
	}

	private void registerRawCodec(CustomPacketPayload.Type<RawPacketImpl> type, ResourceKey channel) {
		if(!codecs.containsKey(type)) codecs.put(type, StreamCodec.of(
				(buffer, packet) -> buffer.writeCharSequence(packet.getDataAsString(), StandardCharsets.UTF_8),
				buffer -> new RawPacketImpl(channel, (ChannelBuf) buffer, buffer.readableBytes() > 0 ? buffer.readCharSequence(buffer.readableBytes(), StandardCharsets.UTF_8).toString() : "")
			)
		);
	}

}
