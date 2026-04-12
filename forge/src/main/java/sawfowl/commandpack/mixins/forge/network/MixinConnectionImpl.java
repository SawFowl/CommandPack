package sawfowl.commandpack.mixins.forge.network;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.ChannelHandlerContext;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.BrandPayload;

import sawfowl.commandpack.apiclasses.CPConnection;

@Mixin(value = Connection.class, remap = false)
public class MixinConnectionImpl implements CPConnection {

	@Unique private String clientName;

	@Override
	public String getClientName() {
		return clientName;
	}

	@Inject(method = "channelRead0*", at = @At("HEAD"))
	public void commandpack$onRead(ChannelHandlerContext ctx, Packet<?> packet, CallbackInfo ci) {
		if(clientName == null && packet instanceof ServerboundCustomPayloadPacket(
				net.minecraft.network.protocol.common.custom.CustomPacketPayload payload
		) && payload instanceof BrandPayload(String brand)) clientName = brand;
	}

}
