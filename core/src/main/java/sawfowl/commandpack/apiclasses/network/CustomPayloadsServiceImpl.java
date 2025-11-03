package sawfowl.commandpack.apiclasses.network;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.plugin.PluginContainer;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
import sawfowl.commandpack.api.network.CustomPayloadsService;
import sawfowl.commandpack.api.network.listeners.PacketListener;
import sawfowl.commandpack.api.network.listeners.RawPacketListener;
import sawfowl.commandpack.api.network.packets.SerializedPacket;

public class CustomPayloadsServiceImpl implements CustomPayloadsService {

	private Map<CustomPacketPayload.Type<RawPacketImpl>, StreamCodec<RegistryFriendlyByteBuf, RawPacketImpl>> codecs = new HashMap<>();
	private Map<ResourceKey, Function<String, SerializedPacket<?>>> serializers = new HashMap<>();
	private Map<ResourceKey, Map<PluginContainer, RawPacketListener>> rawListeners = new HashMap<>();
	private Map<ResourceKey, Map<PluginContainer, PacketListener<?>>> listeners = new HashMap<>();
	private Map<ResourceKey, Object> handlers = new HashMap<>();
	public CustomPayloadsServiceImpl() {
		init();
	}

	private void init() {}

	@Override
	public void registerRawCodec(ResourceKey channel) {
		registerRawCodec(new Type<>((ResourceLocation) (Object) channel), channel);
	}

	private void registerRawCodec(CustomPacketPayload.Type<RawPacketImpl> type, ResourceKey channel) {
		if(!codecs.containsKey(type)) codecs.put(type, StreamCodec.of(
				(buffer, packet) -> buffer.writeCharSequence(packet.data(), StandardCharsets.UTF_8),
				buffer -> new RawPacketImpl(channel, buffer.readCharSequence(buffer.readableBytes(), StandardCharsets.UTF_8).toString())
			)
		);
	}

	public Map<CustomPacketPayload.Type<RawPacketImpl>, StreamCodec<RegistryFriendlyByteBuf, RawPacketImpl>> getCodecs() {
		return codecs;
	}

	@Override
	public void registerSerializer(ResourceKey channel, Function<String, SerializedPacket<?>> function) {
		if(!serializers.containsKey(channel)) serializers.put(channel, function);
	}

	public boolean containsSerializer(ResourceKey channel) {
		return serializers.containsKey(channel);
	}

	public Function<String, SerializedPacket<?>> getSerializer(ResourceKey channel) {
		return serializers.get(channel);
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

	public Collection<RawPacketListener> getRawListeners(ResourceKey channel) {
		return rawListeners.containsKey(channel) ? rawListeners.get(channel).values() : Collections.emptyList();
	}

	public Collection<PacketListener<?>> getListeners(ResourceKey channel) {
		return listeners.containsKey(channel) ? listeners.get(channel).values() : Collections.emptyList();
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

	public Map<ResourceKey, Object> getHandlers() {
		return handlers;
	}

}
