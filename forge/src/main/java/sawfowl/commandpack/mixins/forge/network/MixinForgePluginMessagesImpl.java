package sawfowl.commandpack.mixins.forge.network;

import java.nio.charset.StandardCharsets;
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

	private static final CommandPackInstance plugin = CommandPackInstance.getInstance();

	@Shadow public ServerPlayer player;

	private MixinServerPlayer getPlayer() {
		return (MixinServerPlayer) player;
	}

	private Cause createCause() {
		return Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, getPlayer()).add(EventContextKeys.PLUGIN, plugin.getPluginContainer()).build(), plugin.getPluginContainer());
	}

	@Inject(method = "handleCustomPayload", at = @At("HEAD"))
	public void onPluginMessage(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
		FriendlyByteBuf copy = new FriendlyByteBuf(Unpooled.buffer());
		ServerboundCustomPayloadPacket.STREAM_CODEC.encode(copy, packet);
		Sponge.eventManager().post(new PacketEvent(packet.payload().type().id().toString(), copy));
	}

	private class PacketEvent implements RecievePacketEvent {

		Cause cause;
		String packetName;
		String stringData;
		byte[] data;
		int readableBytes;
		boolean isReadable;

		PacketEvent(String packet, FriendlyByteBuf buffer) {
			cause = createCause();
			packetName = packet;
			readableBytes = buffer.readableBytes();
			isReadable = buffer.isReadable();
			data = buffer.readableBytes() == 0 ? new byte[]{} : buffer.array();
			stringData = readableBytes == 0 ? "" : buffer.toString(0, readableBytes, StandardCharsets.UTF_8);
			if(stringData.startsWith("\r")) stringData = stringData.substring(1);
			if(stringData.startsWith(packet)) stringData = stringData.replaceFirst(packet, "");
			if(plugin.getMainConfig().getDebugPlayerData().packets()) {
				plugin.getLogger().info(plugin.getLocales().getSystemLocale().getDebug().getDebugPlayerData().getPackets(player.getName().getString(), packet, stringData));
			}
		}

		@Override
		public Cause cause() {
			return cause;
		}

		@Override
		public UUID getPlayerUniqueId() {
			return player.getUUID();
		}

		@Override
		public MixinServerPlayer getMixinPlayer() {
			return getPlayer();
		}

		@Override
		public GameProfile getPlayerProfile() {
			return getPlayer().profile();
		}

		@Override
		public String getPacketName() {
			return packetName;
		}

		@Override
		public byte[] getData() {
			return data;
		}

		@Override
		public int readableBytes() {
			return readableBytes;
		}

		@Override
		public String getDataAsString() {
			return stringData;
		}

		@Override
		public boolean isReadable() {
			return isReadable;
		}
		
	}

}
