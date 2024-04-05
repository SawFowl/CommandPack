package sawfowl.commandpack.mixins.vanilla.network;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.network.channel.ChannelBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.events.RecievePacketEvent;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinVanillaPluginMessagesImpl {

	Cause cause = Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, CommandPack.getInstance().getPluginContainer()).build(), CommandPack.getInstance().getPluginContainer());
	
	@Shadow
	public abstract ServerPlayer getPlayer();

	@Inject(method = "handleCustomPayload", at = @At("HEAD"))
	public void onPluginMessage(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
		Sponge.eventManager().post(
			new RecievePacketEvent() {

				@Override
				public Cause cause() {
					return cause;
				}

				@Override
				public UUID getPlayerUniqueId() {
					return getPlayer().getUUID();
				}

				@Override
				public String getPacketName() {
					return packet.payload().id().toString();
				}

				@Override
				public String getDataAsString() {
					return ((ChannelBuf) packet.payload()).readString();
				}

				@Override
				public byte[] getData() {
					return ((ChannelBuf) packet.payload()).readByteArray();
				}

				@Override
				public MixinServerPlayer getMixinPlayer() {
					return (MixinServerPlayer) getPlayer();
				}

				@Override
				public int readableBytes() {
					return ((ChannelBuf) packet.payload()).available();
				}

				@Override
				public boolean isReadable() {
					return ((ChannelBuf) packet.payload()).hasArray();
				}

			}
		);
	}

}
