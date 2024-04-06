package sawfowl.commandpack.mixins.vanilla.network;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import io.netty.buffer.Unpooled;

import net.kyori.adventure.text.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import sawfowl.commandpack.api.mixin.network.CustomPacket;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.apiclasses.CustomPacketImpl;
import sawfowl.commandpack.utils.CommandsUtil;

import sawfowl.localeapi.api.Text;

@Mixin(ServerPlayer.class)
public abstract class MixinVanillaServerPlayerImpl implements MixinServerPlayer {

	@Shadow
	public ServerGamePacketListenerImpl connection;

	@Override
	public void sendPacket(CustomPacket packet) {
		if(packet instanceof CustomPacketImpl custom) connection.send(createPacket(custom));
	}

	@Override
	public void sendMessage(Text message) {
		sendMessage(message.applyPlaceholders(Component.empty(), (MixinServerPlayer) this).get());
	}

	@Override
	public void sendMessage(String message) {
		sendMessage(Text.of(message));
	}

	@Override
	public List<String> getModList() {
		return CommandsUtil.EMPTY_VARIANTS;
	}

	private FriendlyByteBuf createFriendlyByteBuf(byte[] data) {
		return new FriendlyByteBuf(Unpooled.copiedBuffer(data));
	}

	private CustomPacketPayload createCustomPacketPayload(ResourceLocation id, FriendlyByteBuf buf) {
		return new CustomPacketPayload() {
			
			@Override
			public void write(FriendlyByteBuf arg0) {
				if (arg0 != null) {
					arg0.writeBytes(buf.slice());
				}
			}
			
			@Override
			public ResourceLocation id() {
				return id;
			}
		};
	}

	private ClientboundCustomPayloadPacket createPacket(CustomPacketImpl custom) {
		return new ClientboundCustomPayloadPacket(createCustomPacketPayload(new ResourceLocation(custom.getLocation()), createFriendlyByteBuf(custom.getData().getBytes(StandardCharsets.UTF_8))));
	}

}
