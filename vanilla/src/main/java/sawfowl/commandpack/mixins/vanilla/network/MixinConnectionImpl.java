package sawfowl.commandpack.mixins.vanilla.network;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.ChannelHandlerContext;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.BrandPayload;

import sawfowl.commandpack.apiclasses.CPConnection;

@Mixin(Connection.class)
public class MixinConnectionImpl implements CPConnection {

	private String clientName;

	@Override
	public String getClientName() {
		return clientName;
	}

	@Inject(method = "channelRead0", at = @At("HEAD"))
	public void onRead(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
		if(clientName == null && packet instanceof ServerboundCustomPayloadPacket p && p.payload() instanceof BrandPayload b) clientName = b.brand();
	}

}
