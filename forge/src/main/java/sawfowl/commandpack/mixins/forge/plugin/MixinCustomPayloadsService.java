package sawfowl.commandpack.mixins.forge.plugin;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.lifecycle.RegisterChannelEvent;
import org.spongepowered.api.network.channel.ChannelBuf;
import org.spongepowered.api.network.channel.raw.RawDataChannel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.Channel.VersionTest;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkRegistry;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.api.network.listeners.PacketListener;
import sawfowl.commandpack.api.network.listeners.RawPacketListener;
import sawfowl.commandpack.api.network.packets.RawPacket;
import sawfowl.commandpack.api.network.packets.SerializedPacket;
import sawfowl.commandpack.apiclasses.DataChannelRegistrationEventImpl;
import sawfowl.commandpack.apiclasses.network.CustomPayloadsServiceImpl;
import sawfowl.commandpack.apiclasses.network.RawPacketImpl;
import sawfowl.commandpack.apiclasses.network.SerializedPacketBuilder.SerializedPacketImpl;
import sawfowl.commandpack.mixins.forge.game.MinecraftServerAccessor;

@Mixin(value = CustomPayloadsServiceImpl.class, remap = false)
public abstract class MixinCustomPayloadsService {

	@Shadow @Final private CommandPackInstance plugin;
	@Shadow private boolean finished;
	@Shadow private Map<ResourceKey, RawDataChannel> spongeChannels = new HashMap<>();
	private Set<ResourceKey> forgeChannels = new HashSet<>();
	private int channelVersion = 766; // NetworkInitialization.getVersion();

	@Overwrite
	private void init() {
		Sponge.eventManager().registerListeners(plugin.getPluginContainer(), this);
	}

	@Overwrite
	public void registerChannel(ResourceKey channel) {
		if(finished) {
			plugin.getLocales().getSystemLocale().getDebug().getFinishedRegisterNetworkData(channel);
		} else if(!forgeChannels.contains(channel)) forgeChannels.add(channel);
	}

	@Listener(order = Order.LAST)
	public void onChannelRegistration(RegisterChannelEvent event) {
		Sponge.eventManager().post(new DataChannelRegistrationEventImpl(plugin));
		finished = true;
		forgeChannels.forEach(channel -> {
			var find = NetworkRegistry.findTarget((ResourceLocation) (Object) channel);
			if(find != null) {
				find.addListener(listener -> {
					if(listener.getPayloadObject() instanceof MixinServerPlayer player && listener.getPayload() instanceof ChannelBuf buf) handle(player, new RawPacketImpl(channel, buf, listener.getPayload().readableBytes() > 0 ? listener.getPayload().toString(StandardCharsets.UTF_8) : ""));
				});
				find = null;
			} else ChannelBuilder
				.named((ResourceLocation) (Object) channel)
				.connectionHandler(consumer -> MinecraftServerAccessor.getconnection())
				.serverAcceptedVersions(VersionTest.exact(channelVersion))
				.clientAcceptedVersions(VersionTest.exact(channelVersion))
				.networkProtocolVersion(channelVersion)
				.payloadChannel()
				.any()
				.bidirectional()
				.add(new Type<>((ResourceLocation) (Object) channel), createCodec(channel), (payload, context) -> handle(payload, context))
				.build();
			}
		);
	}

	private StreamCodec<FriendlyByteBuf, RawPacketImpl> createCodec(ResourceKey channel) {
		return StreamCodec.of(
			(buffer, packet) -> buffer.writeCharSequence(packet.getDataAsString(), StandardCharsets.UTF_8),
			buffer -> new RawPacketImpl(channel, (ChannelBuf) buffer, buffer.readableBytes() > 0 ? buffer.readCharSequence(buffer.readableBytes(), StandardCharsets.UTF_8).toString() : "")
		);
	}

	private void handle(RawPacketImpl payload, CustomPayloadEvent.Context ctx) {
		ctx.setPacketHandled(true);
		handle((MixinServerPlayer) ctx.getSender(), payload);
	}

	private void handle(MixinServerPlayer player, RawPacket rawPacket) {
		getRawListeners(rawPacket.channel()).forEach(listener -> listener.read(player, rawPacket));
		handleSerialized(player, rawPacket, containsSerializer(rawPacket.channel()), containsBufferSerializer(rawPacket.channel()));
	}

	@SuppressWarnings("unchecked")
	private void handleSerialized(MixinServerPlayer player, RawPacket rawPacket, boolean stringSerializer, boolean bufferSerializer) {
		if(stringSerializer || bufferSerializer) for(PacketListener<?> listener : getListeners(rawPacket.channel())) listener.read(player, serialize(rawPacket, bufferSerializer));
	}

	@SuppressWarnings("rawtypes")
	private SerializedPacket serialize(RawPacket packet, boolean bufferSerializer) {
		return ((SerializedPacketImpl) (bufferSerializer 
			?
			SerializedPacket.ofBuffer(packet.channel(), getBufferSerializer(packet.channel()))
			:
			SerializedPacket.of(packet.channel(), getSerializer(packet.channel()))))
		.apply(packet.getBuffer(), packet.getDataAsString());
	}

	@Shadow abstract Collection<RawPacketListener> getRawListeners(ResourceKey channel);

	@Shadow abstract Collection<PacketListener<?>> getListeners(ResourceKey channel);

	@Shadow abstract boolean containsSerializer(ResourceKey channel);

	@Shadow abstract boolean containsBufferSerializer(ResourceKey channel);

	@Shadow abstract Function<String, SerializedPacket<?>> getSerializer(ResourceKey channel);

	@Shadow abstract Function<ChannelBuf, SerializedPacket<?>> getBufferSerializer(ResourceKey channel);

}
