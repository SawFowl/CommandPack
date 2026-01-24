package sawfowl.commandpack.mixins.neoforge.network;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.ResourceKey;
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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.neoforged.neoforge.network.connection.ConnectionType;
import net.neoforged.neoforge.network.payload.MinecraftRegisterPayload;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.events.RecievePacketEvent;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.api.network.packets.RawPacket;
import sawfowl.commandpack.apiclasses.network.RawPacketImpl;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinPluginMessagesImpl {

	private static final CommandPackInstance plugin = CommandPackInstance.getInstance();

	@Shadow ServerPlayer player;

	private MixinServerPlayer getPlayer() {
		return (MixinServerPlayer) player;
	}

	private Cause createCause() {
		return Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, getPlayer()).add(EventContextKeys.PLUGIN, plugin.getPluginContainer()).build(), plugin.getPluginContainer());
	}

	@Inject(method = "handleCustomPayload", at = @At("HEAD"))
	public void commandpack$onPluginMessage(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
		String packetId = packet.payload().type().id().toString();
		if(plugin.getMainConfig().getRestrictMods().isEnable() && !getPlayer().hasPermission(Permissions.ALL_MODS_ACCESS) && packet.payload() instanceof MinecraftRegisterPayload minecraftRegisterPayload && restrinctMods(minecraftRegisterPayload)) return;
		if(plugin.getMainConfig().getIgnorePackets().isEnable()) {
			if(plugin.getMainConfig().getIgnorePackets().isDebug()) plugin.getLogger().info(packetId);
			if(!plugin.getMainConfig().getIgnorePackets().canEncode(packetId)) {
				packetId = null;
				return;
			}
		}
		try {
			FriendlyByteBuf copy = new FriendlyByteBuf(Unpooled.buffer());
			Optional<StreamCodec<ByteBuf, RawPacket>> codec = plugin.getPayloadsService().findCodec((ResourceKey) (Object) packet.payload().type().id());
			if(codec.isPresent() && packet.payload() instanceof RawPacketImpl rawPacketImpl) {
				RegistryFriendlyByteBuf friendlyByteBuf = new RegistryFriendlyByteBuf(copy, null, ConnectionType.OTHER);
				codec.get().encode(friendlyByteBuf, rawPacketImpl);
				copy = friendlyByteBuf;
			} else ServerboundCustomPayloadPacket.STREAM_CODEC.encode(copy, packet);
			PacketEvent event = new PacketEvent(packetId, copy);
			if(getPlayer().isOnline()) Sponge.eventManager().post(event);
			event = null;
			copy = null;
			codec = null;
		} catch (Exception e) {
		}
		packetId = null;
	}

	private boolean restrinctMods(MinecraftRegisterPayload payload) {
		List<String> disAllowedMods = plugin.getMainConfig().getRestrictMods().getDisAllowedMods(payload.newChannels().stream().map(rl -> rl.toString()).toList());
		if(!disAllowedMods.isEmpty()) {
			getPlayer().kick(plugin.getLocales().getAsReference(getPlayer()).getOther().getIllegalMods(true, String.join(", ", disAllowedMods)));
			return true;
		}
		disAllowedMods = null;
		return false;
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
			data = buffer.readableBytes() == 0 ? new byte[]{} : buffer.readByteArray();
			stringData = readableBytes == 0 ? "" : buffer.toString(0, readableBytes, StandardCharsets.UTF_8);
			char first = stringData.charAt(0);
			stringData = !stringData.startsWith(packet) ? (stringData.contains(packet) && stringData.startsWith(first + packet) ? stringData.replace(first + packet, "") : stringData) : stringData.replace(first + packet, "");
			stringData = stringData.replaceAll("\\p{C}", " ");
			while(stringData.length() > 0 && stringData.charAt(stringData.length() - 1) == ' ') {
				stringData = stringData.substring(0, stringData.length() - 1);
			}
			if(plugin.getMainConfig().getDebugPlayerData().packets()) {
				plugin.getLogger().info(plugin.getLocales().getSystemAsReference().getDebug().getDebugPlayerData().getPackets(player.getName().getString(), packet, stringData));
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
