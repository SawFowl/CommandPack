package sawfowl.commandpack.mixins.vanilla.network;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.events.RecievePacketEvent;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;

@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class MixinVanillaPluginMessagesImpl {

	@Shadow protected abstract GameProfile getOwner();

	private MixinServerPlayer getPlayer() {
		return Sponge.server().player(getOwner().getId()).map(MixinServerPlayer::cast).orElse(null);
	}

	private Cause createCause() {
		return getPlayer() == null ? Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, CommandPack.getInstance().getPluginContainer()).build(), CommandPack.getInstance().getPluginContainer()) : Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, getPlayer()).add(EventContextKeys.PLUGIN, CommandPack.getInstance().getPluginContainer()).build(), CommandPack.getInstance().getPluginContainer());
	}

	@Inject(method = "handleCustomPayload", at = @At("HEAD"))
	public void onPluginMessage(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
		FriendlyByteBuf copy = new FriendlyByteBuf(Unpooled.buffer());
		ServerboundCustomPayloadPacket.STREAM_CODEC.encode(copy, packet);
		Sponge.eventManager().post(
			new RecievePacketEvent() {

				@Override
				public Cause cause() {
					return createCause();
				}

				@Override
				public UUID getPlayerUniqueId() {
					return getOwner() == null ? null : getOwner().getId();
				}

				@Override
				public String getPacketName() {
					return packet.payload().type().id().toString();
				}

				@Override
				public String getDataAsString() {
					return copy.getCharSequence(0, copy.readableBytes(), StandardCharsets.UTF_8).toString();
				}

				@Override
				public byte[] getData() {
					return copy.array();
				}

				@Override
				public MixinServerPlayer getMixinPlayer() {
					return getPlayer();
				}

				@Override
				public int readableBytes() {
					return copy.readableBytes();
				}

				@Override
				public boolean isReadable() {
					return copy.isReadable();
				}

			}
		);
	}

}
