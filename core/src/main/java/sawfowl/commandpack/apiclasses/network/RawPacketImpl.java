package sawfowl.commandpack.apiclasses.network;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.network.channel.ChannelBuf;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.api.network.packets.RawPacket;

public class RawPacketImpl implements CustomPacketPayload, RawPacket {

	public RawPacket.Builder builder() {
		return new Builder();
	}

	private Type<RawPacketImpl> type;
	private String data;
	private ChannelBuf buffer;
	public RawPacketImpl(ResourceKey channel, ChannelBuf buffer, String data) {
		type = new Type<>((ResourceLocation) (Object) channel);
		this.buffer = buffer;
		this.data = data;
	}

	public RawPacketImpl(){}

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
