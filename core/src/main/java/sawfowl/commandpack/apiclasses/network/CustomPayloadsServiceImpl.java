package sawfowl.commandpack.apiclasses.network;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.lifecycle.RegisterChannelEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.network.channel.ChannelBuf;
import org.spongepowered.api.network.channel.raw.RawDataChannel;
import org.spongepowered.plugin.PluginContainer;

import io.netty.buffer.ByteBuf;

import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.network.CustomPayloadsService;
import sawfowl.commandpack.api.network.listeners.PacketListener;
import sawfowl.commandpack.api.network.listeners.RawPacketListener;
import sawfowl.commandpack.api.network.packets.RawPacket;
import sawfowl.commandpack.api.network.packets.SerializedPacket;
import sawfowl.commandpack.apiclasses.DataChannelRegistrationEventImpl;

public class CustomPayloadsServiceImpl implements CustomPayloadsService {

	private Map<CustomPacketPayload.Type<RawPacketImpl>, StreamCodec<ByteBuf, RawPacketImpl>> codecs = new HashMap<>();
	private Map<ResourceKey, Function<String, SerializedPacket<?>>> serializers = new HashMap<>();
	private Map<ResourceKey, Function<ChannelBuf, SerializedPacket<?>>> bufferSerializers = new HashMap<>();
	private Map<ResourceKey, Map<PluginContainer, RawPacketListener>> rawListeners = new HashMap<>();
	private Map<ResourceKey, Map<PluginContainer, PacketListener<?>>> listeners = new HashMap<>();
	private Map<ResourceKey, RawDataChannel> spongeChannels = new HashMap<>();
	private Set<ResourceKey> needRecode = new HashSet<ResourceKey>();
	private boolean finished = false;
	private final CommandPackInstance plugin;
	public CustomPayloadsServiceImpl(CommandPackInstance plugin) {
		this.plugin = plugin;
		/*if(!plugin.isNeoForgeServer()) */Sponge.eventManager().registerListeners(plugin.getPluginContainer(), this, MethodHandles.lookup());
		init();
	}

	private void init() {}

	private void spongeEvent(RegisterChannelEvent event) {}

	private void spongeEvent(StartedEngineEvent<Server> event) {}

	@Listener
	public void onServerStarted(StartedEngineEvent<Server> event) {
		spongeEvent(event);
	}

	@Listener(order = Order.LAST)
	public void onChannelRegistration(RegisterChannelEvent event) {
		Sponge.eventManager().post(new DataChannelRegistrationEventImpl(plugin));
		finished = true;
		spongeEvent(event);
	}

	@Override
	public void registerChannel(ResourceKey channel) {
		if(finished) {
			plugin.getLocales().getSystemAsReference().getDebug().getFinishedRegisterNetworkData(channel);
		} else registerRawCodec(RawPacketImpl.type(channel), channel);
	}

	@SuppressWarnings("unchecked")
	public Optional<StreamCodec<ByteBuf, RawPacket>> findCodec(ResourceKey channel) {
		return codecs.entrySet().stream().filter(entry -> entry.getKey().id().equals((ResourceLocation) (Object) channel)).findFirst().map(entry -> (StreamCodec<ByteBuf, RawPacket>) (Object) entry.getValue());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> void registerSerializer(ResourceKey channel, Function<String, SerializedPacket<T>> function) {
		if(!serializers.containsKey(channel)) serializers.put(channel, (Function) function);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> void registerBufferSerializer(ResourceKey channel, Function<ChannelBuf, SerializedPacket<T>> function) {
		if(!bufferSerializers.containsKey(channel)) bufferSerializers.put(channel, (Function) function);
	}

	@Override
	public void registerRawListener(PluginContainer container, ResourceKey channel, RawPacketListener listener) {
		if(!rawListeners.containsKey(channel)) rawListeners.put(channel, new HashMap<>());
		if(rawListeners.get(channel).containsKey(container)) return;
		rawListeners.get(channel).put(container, listener);
	}

	@Override
	public <T> void registerListener(PluginContainer container, ResourceKey channel, PacketListener<T> listener) {
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

	public boolean isNeedRecode(ResourceKey channel) {
		return needRecode.contains(channel);
	}

	Map<CustomPacketPayload.Type<RawPacketImpl>, StreamCodec<ByteBuf, RawPacketImpl>> getCodecs() {
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
		if(!codecs.containsKey(type)) codecs.put(type, RawPacketImpl.codec(channel));
	}

	public Map<ResourceKey, RawDataChannel> getSpongeChannels() {
		return spongeChannels;
	}

}
