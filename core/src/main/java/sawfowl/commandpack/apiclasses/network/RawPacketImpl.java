package sawfowl.commandpack.apiclasses.network;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.network.channel.ChannelBuf;
import org.spongepowered.common.network.channel.SpongeChannelPayload;

import io.netty.buffer.ByteBuf;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.api.network.packets.RawPacket;

public class RawPacketImpl implements CustomPacketPayload, RawPacket {

	private static final Map<ResourceKey, Type<RawPacketImpl>> TYPES = new HashMap<>();
	private static final Map<ResourceKey, StreamCodec<ByteBuf, RawPacketImpl>> CODECS = new HashMap<>();

	public static StreamCodec<ByteBuf, RawPacketImpl> codec(ResourceKey channel) {
		if(!CODECS.containsKey(channel)) CODECS.put(channel, StreamCodec.of(
				(buffer, packet) -> {
					if((CustomPacketPayload) packet instanceof SpongeChannelPayload spongePayload) {
						spongePayload.write((FriendlyByteBuf) buffer);
					} else buffer.writeCharSequence(packet.getDataAsString(), StandardCharsets.UTF_8);
				},
				buffer -> new RawPacketImpl(channel, (ChannelBuf) buffer, buffer.readableBytes() > 0 ? buffer.readCharSequence(buffer.readableBytes(), StandardCharsets.UTF_8).toString() : "")
			)
		);
		return CODECS.get(channel);
	}

	public static Type<RawPacketImpl> type(ResourceKey channel) {
		if(!TYPES.containsKey(channel)) TYPES.put(channel, new Type<>((ResourceLocation) (Object) channel));
		return TYPES.get(channel);
	}

	private Type<RawPacketImpl> type;
	private String data;
	private ChannelBuf buffer;
	public RawPacketImpl(ResourceKey channel, ChannelBuf buffer, String data) {
		type = type(channel);
		this.buffer = buffer;
		this.data = data;
	}

	public RawPacketImpl(){}

	public RawPacket.Builder builder() {
		return new Builder();
	}

	public String getDataAsString() {
		return data;
	}

	@Override
	public ChannelBuf getBuffer() {
		return buffer;
	}

	@Override
	public ResourceKey channel() {
		return (ResourceKey) (Object) type().id();
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return type;
	}

	@Override
	public void sendTo(ServerPlayer player) {
		MixinServerPlayer.cast(player).sendPacket(this);
	}

	private class Builder implements RawPacket.Builder {

		@Override
		public @NotNull RawPacket build() {
			throw new NullPointerException();
		}

		@Override
		public RawPacket create(ResourceKey channel, String data) {
			RawPacketImpl.this.type = new Type<>((ResourceLocation) (Object) channel);
			RawPacketImpl.this.data = data;
			return RawPacketImpl.this;
		}

	}

}
