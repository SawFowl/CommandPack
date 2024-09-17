package sawfowl.commandpack.apiclasses;

import java.nio.charset.Charset;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import sawfowl.commandpack.api.mixin.network.CustomPacket;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;

public class CustomPacketImpl implements CustomPacket {

	private ClientboundCustomPayloadPacket packet;

	public Builder builder() {

		return new Builder() {
			@Override
			public CustomPacket create(String resourceLocation, String data) {
				packet = new ClientboundCustomPayloadPacket(new ResourceLocation(resourceLocation), new FriendlyByteBuf(Unpooled.copiedBuffer(data.getBytes(Charset.forName("UTF-8")))));
				return build();
			}
			@Override
			public @NotNull CustomPacket build() {
				return CustomPacketImpl.this;
			}
		};
	}

	@Override
	public void sendTo(ServerPlayer player) {
		MixinServerPlayer.cast(player).sendPacket(this);
	}

	public ClientboundCustomPayloadPacket getPacket() {
		return packet;
	}

	@Override
	public int contentVersion() {
		return 0;
	}

	@Override
	public DataContainer toContainer() {
		return null;
	}

}
