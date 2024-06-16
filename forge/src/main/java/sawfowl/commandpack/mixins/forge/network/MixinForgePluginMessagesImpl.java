package sawfowl.commandpack.mixins.forge.network;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.buffer.Unpooled;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.events.RecievePacketEvent;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;

@Mixin(value = ServerGamePacketListenerImpl.class, remap = false)
public abstract class MixinForgePluginMessagesImpl {

	@Shadow public ServerPlayer player;

	private Optional<MixinServerPlayer> getPlayer() {
		return Optional.ofNullable((MixinServerPlayer) player);
	}

	private Cause createCause() {
		return Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, getPlayer().get()).add(EventContextKeys.PLUGIN, CommandPackInstance.getInstance().getPluginContainer()).build(), CommandPackInstance.getInstance().getPluginContainer());
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
					return player == null ? null : player.getUUID();
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
				public Optional<MixinServerPlayer> getMixinPlayer() {
					return getPlayer();
				}

				@Override
				public GameProfile getPlayerProfile() {
					return player == null ? null : GameProfile.of(player.getUUID(), player.getName().getString());
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

