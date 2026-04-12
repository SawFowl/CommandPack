package sawfowl.commandpack.mixins.forge.network;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.buffer.Unpooled;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.events.RecievePacketEvent;
import sawfowl.commandpack.api.game.server.player.CPServerPlayer;

@Mixin(value = ServerGamePacketListenerImpl.class, remap = false)
public abstract class MixinPluginMessagesImpl {

	@Unique private static final CommandPackInstance commandPack$plugin = CommandPackInstance.getInstance();

	@Shadow public ServerPlayer player;

	@Unique
	private CPServerPlayer commandPack$getPlayer() {
		return (CPServerPlayer) player;
	}

	@Unique
    private Cause commandPack$createCause() {
		return Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, commandPack$getPlayer()).add(EventContextKeys.PLUGIN, commandPack$plugin.getPluginContainer()).build(), commandPack$plugin.getPluginContainer());
	}

	@Inject(method = "handleCustomPayload", at = @At("HEAD"))
	public void commandpack$onPluginMessage(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
		String packetId = packet.payload().type().id().toString();
		FriendlyByteBuf copy = new FriendlyByteBuf(Unpooled.buffer());
		boolean emptyBuffer = true;
		if(commandPack$plugin.getMainConfig().getRestrictMods().isEnable() && packetId.equals("minecraft:register") && !commandPack$getPlayer().hasPermission(Permissions.ALL_MODS_ACCESS)) {
			ServerboundCustomPayloadPacket.STREAM_CODEC.encode(copy, packet);
			emptyBuffer = false;
			if(copy.readableBytes() > 0) {
				List<String> disAllowedMods = commandPack$plugin.getMainConfig().getRestrictMods().getDisAllowedMods(copy.toString(0, copy.readableBytes(), StandardCharsets.UTF_8));
				if(!disAllowedMods.isEmpty()) {
					commandPack$getPlayer().kick(commandPack$plugin.getLocales().getAsReferenced(commandPack$getPlayer()).getOther().getIllegalMods(true, String.join(", ", disAllowedMods)));
					copy = null;
					disAllowedMods = null;
					return;
				}
				disAllowedMods = null;
			}
		}
		if(commandPack$plugin.getMainConfig().getIgnorePackets().isEnable()) {
			if(commandPack$plugin.getMainConfig().getIgnorePackets().isDebug()) commandPack$plugin.getLogger().debug(packetId);
			if(!commandPack$plugin.getMainConfig().getIgnorePackets().canEncode(packetId)) return;
		}
		try {
			if(emptyBuffer) ServerboundCustomPayloadPacket.STREAM_CODEC.encode(copy, packet);
			PacketEvent event = new PacketEvent(packetId, copy);
			if(commandPack$getPlayer().isOnline()) Sponge.eventManager().post(event);
			event = null;
		} catch (Exception e) {
		}
		packetId = null;
		copy = null;
	}

	private class PacketEvent implements RecievePacketEvent {

		Cause cause;
		String packetName;
		String stringData;
		byte[] data;
		int readableBytes;
		boolean isReadable;

		PacketEvent(String packet, FriendlyByteBuf buffer) {
			cause = commandPack$createCause();
			packetName = packet;
			readableBytes = buffer.readableBytes();
			isReadable = buffer.isReadable();
			data = buffer.readableBytes() == 0 ? new byte[]{} : buffer.readByteArray();
			stringData = readableBytes == 0 ? "" : buffer.toString(0, readableBytes, StandardCharsets.UTF_8);
			char first = stringData.charAt(0);
			stringData = !stringData.startsWith(packet) ? (stringData.contains(packet) && stringData.startsWith(first + packet) ? stringData.replace(first + packet, "") : stringData) : stringData.replace(first + packet, "");
			stringData = stringData.replaceAll("\\p{C}", " ");
			while(stringData.length() > 0 && stringData.charAt(stringData.length() - 1) == ' ') {
				stringData = stringData.substring(0, stringData.length() - 1);
			}
			if(commandPack$plugin.getMainConfig().getDebugPlayerData().packets()) {
				commandPack$plugin.getLogger().info(commandPack$plugin.getLocales().getSystemAsReferenced().getDebug().getDebugPlayerData().getPackets(player.getName().getString(), packet, stringData));
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
		public CPServerPlayer getMixinPlayer() {
			return commandPack$getPlayer();
		}

		@Override
		public GameProfile getPlayerProfile() {
			return commandPack$getPlayer().profile();
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
