package sawfowl.commandpack.mixins.forge.plugin;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.lifecycle.RegisterChannelEvent;
import org.spongepowered.api.network.channel.ChannelBuf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.common.network.channel.SpongeChannelPayload;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import net.minecraftforge.event.network.CustomPayloadEvent.Context;
import net.minecraftforge.network.Channel.VersionTest;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkRegistry;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
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
	private int channelVersion = 766; // NetworkInitialization.getVersion();

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	private void spongeEvent(RegisterChannelEvent event) {
		getCodecs().forEach((type, codec) -> {
			var find = NetworkRegistry.findTarget(type.id());
			if(find != null) {
				find.addListener(listener -> {
					if(listener.getSource().getSender() instanceof MixinServerPlayer player && listener.getPayload() instanceof ChannelBuf buf) handle(player, new RawPacketImpl((ResourceKey) (Object) type.id(), buf, listener.getPayload().readableBytes() > 0 ? listener.getPayload().toString(StandardCharsets.UTF_8) : ""));
				});
				find = null;
			} else ChannelBuilder
				.named(type.id())
				.connectionHandler(consumer -> ((MinecraftServerAccessor) Sponge.server()).getconnection())
				.serverAcceptedVersions(VersionTest.exact(channelVersion))
				.clientAcceptedVersions(VersionTest.exact(channelVersion))
				.networkProtocolVersion(channelVersion)
				.optional()
				.payloadChannel()
				.any()
				.bidirectional()
				.add(type, codec, (payload, context) -> handle(payload, context))
				.build();
			
		});
	}

	private void handle(SpongeChannelPayload payload, Context context) {
		context.setPacketHandled(true);
		if(!getCodecs().containsKey(payload.type()) || !(context.getSender() instanceof MixinServerPlayer player)) return;
		var buffer = new FriendlyByteBuf(Unpooled.buffer());
		getCodecs().get(payload.type()).decode(buffer);
		handle(player, new RawPacketImpl((ResourceKey) (Object) payload.type().id(), (ChannelBuf) buffer, buffer.readableBytes() > 0 ? buffer.readCharSequence(buffer.readableBytes(), StandardCharsets.UTF_8).toString() : ""));
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

	@Shadow abstract Map<CustomPacketPayload.Type<SpongeChannelPayload>, StreamCodec<FriendlyByteBuf, SpongeChannelPayload>> getCodecs();

	@Shadow abstract boolean containsSerializer(ResourceKey channel);

	@Shadow abstract boolean containsBufferSerializer(ResourceKey channel);

	@Shadow abstract Function<String, SerializedPacket<?>> getSerializer(ResourceKey channel);

	@Shadow abstract Function<ChannelBuf, SerializedPacket<?>> getBufferSerializer(ResourceKey channel);

}
