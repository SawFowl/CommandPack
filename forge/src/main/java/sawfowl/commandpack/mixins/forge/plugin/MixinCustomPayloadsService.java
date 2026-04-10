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
import net.minecraft.resources.Identifier;

import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.Channel.VersionTest;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkRegistry;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.game.server.player.CPServerPlayer;
import sawfowl.commandpack.api.network.listeners.PacketListener;
import sawfowl.commandpack.api.network.listeners.RawPacketListener;
import sawfowl.commandpack.api.network.packets.RawPacket;
import sawfowl.commandpack.api.network.packets.SerializedPacket;
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

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public void registerChannel(ResourceKey channel) {
		if(finished) {
			plugin.getLocales().getSystemAsReferenced().getDebug().getFinishedRegisterNetworkData(channel);
		} else if(!forgeChannels.contains(channel)) forgeChannels.add(channel);
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	private void spongeEvent(RegisterChannelEvent event) {
		forgeChannels.forEach(channel -> {
			var find = NetworkRegistry.findTarget((Identifier) (Object) channel);
			if(find != null) {
				find.addListener(listener -> {
					if(listener.getSource().getSender() instanceof CPServerPlayer player && listener.getPayload() instanceof ChannelBuf buf) handle(player, new RawPacketImpl(channel, buf, listener.getPayload().readableBytes() > 0 ? listener.getPayload().toString(StandardCharsets.UTF_8) : ""));
				});
				find = null;
			} else ChannelBuilder
				.named((Identifier) (Object) channel)
				.connectionHandler(_ -> ((MinecraftServerAccessor) Sponge.server()).getconnection())
				.serverAcceptedVersions(VersionTest.exact(channelVersion))
				.clientAcceptedVersions(VersionTest.exact(channelVersion))
				.networkProtocolVersion(channelVersion)
				.optional()
				.payloadChannel()
				.any()
				.bidirectional()
				.add(new Type<>((Identifier) (Object) channel), createCodec(channel), (payload, context) -> handle(payload, context))
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
		handle((CPServerPlayer) ctx.getSender(), payload);
	}

	private void handle(CPServerPlayer player, RawPacket rawPacket) {
		getRawListeners(rawPacket.channel()).forEach(listener -> listener.read(player, rawPacket));
		handleSerialized(player, rawPacket, containsSerializer(rawPacket.channel()), containsBufferSerializer(rawPacket.channel()));
	}

	@SuppressWarnings("unchecked")
	private void handleSerialized(CPServerPlayer player, RawPacket rawPacket, boolean stringSerializer, boolean bufferSerializer) {
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
