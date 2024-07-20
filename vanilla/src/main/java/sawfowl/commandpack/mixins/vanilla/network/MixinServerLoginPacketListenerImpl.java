package sawfowl.commandpack.mixins.vanilla.network;

import java.nio.charset.StandardCharsets;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import io.netty.buffer.Unpooled;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.login.ServerboundLoginAcknowledgedPacket;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import sawfowl.commandpack.CommandPackInstance;
import sawfowl.localeapi.api.Logger;

@Mixin(ServerLoginPacketListenerImpl.class)
public class MixinServerLoginPacketListenerImpl {


	private static final Logger logger = CommandPackInstance.getInstance().getLogger();
	@Shadow Connection connection;
	@Shadow GameProfile authenticatedProfile;

	@Inject(
		method = "handleLoginAcknowledgement",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/network/CommonListenerCookie;createInitial(Lcom/mojang/authlib/GameProfile;Z)Lnet/minecraft/server/network/CommonListenerCookie;"
		)
	) // At this stage it is possible to send a list of server mods to the client.
	public void onHandleLoginAcknowledgement(ServerboundLoginAcknowledgedPacket packet, CallbackInfo ci) {
		//sendFakeForgePacket(createForgePacket1());
		//sendFakeForgePacket(createForgePacket2());
	}

	void sendFakeForgePacket(Packet<?> packet) {
		connection.send(packet);
	}

	// minecraft:register forge:handshake forge:login
	// Packet id = minecraft:register
	// The byte array already contains all the data.
	ClientboundCustomPayloadPacket createForgePacket1() {
		byte[] data = {18, 109, 105, 110, 101, 99, 114, 97, 102, 116, 58, 114, 101, 103, 105, 115, 116, 101, 114, 102, 111, 114, 103, 101, 58, 104, 97, 110, 100, 115, 104, 97, 107, 101, 0, 102, 111, 114, 103, 101, 58, 108, 111, 103, 105, 110, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
		buffer.writeBytes(data);
		return ClientboundCustomPayloadPacket.CONFIG_STREAM_CODEC.decode(buffer);
	}

	// Server ModList. Сhannel id = forge:handshake
	// The byte array already contains all the data.
	// It's not working. When encoding a packet, all data except channel id is lost.
	// I have no idea how to register a Forge channel id.
	ClientboundCustomPayloadPacket createForgePacket2() { 
		byte[] data = {15, 102, 111, 114, 103, 101, 58, 104, 97, 110, 100, 115, 104, 97, 107, 101, 1, 7, 9, 109, 105, 110, 101, 99, 114, 97, 102, 116, 9, 77, 105, 110, 101, 99, 114, 97, 102, 116, 4, 49, 46, 50, 49, 9, 108, 111, 99, 97, 108, 101, 97, 112, 105, 9, 76, 111, 99, 97, 108, 101, 65, 80, 73, 5, 53, 46, 48, 46, 48, 6, 115, 112, 111, 110, 103, 101, 6, 83, 112, 111, 110, 103, 101, 23, 49, 46, 50, 49, 45, 53, 49, 46, 48, 46, 50, 50, 45, 49, 50, 46, 48, 46, 48, 45, 82, 67, 48, 11, 115, 112, 111, 110, 103, 101, 102, 111, 114, 103, 101, 11, 83, 112, 111, 110, 103, 101, 70, 111, 114, 103, 101, 23, 49, 46, 50, 49, 45, 53, 49, 46, 48, 46, 50, 50, 45, 49, 50, 46, 48, 46, 48, 45, 82, 67, 48, 9, 115, 112, 111, 110, 103, 101, 97, 112, 105, 9, 83, 112, 111, 110, 103, 101, 65, 80, 73, 6, 49, 50, 46, 48, 46, 48, 5, 102, 111, 114, 103, 101, 5, 70, 111, 114, 103, 101, 7, 53, 49, 46, 48, 46, 50, 52, 11, 99, 111, 109, 109, 97, 110, 100, 112, 97, 99, 107, 11, 67, 111, 109, 109, 97, 110, 100, 80, 97, 99, 107, 5, 52, 46, 48, 46, 48, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		logger.warn("&5" + new String(data, StandardCharsets.UTF_8));
		FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
		buffer.writeBytes(data);
		ClientboundCustomPayloadPacket packet = ClientboundCustomPayloadPacket.CONFIG_STREAM_CODEC.decode(buffer);
		return packet;
	}

}
