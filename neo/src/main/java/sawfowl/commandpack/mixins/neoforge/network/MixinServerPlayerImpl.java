package sawfowl.commandpack.mixins.neoforge.network;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.network.channel.raw.RawDataChannel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.math.vector.Vector3i;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.kyori.adventure.text.Component;

import net.minecraft.core.BlockPos;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import net.neoforged.neoforge.network.connection.ConnectionType;
import net.neoforged.neoforge.network.registration.NetworkRegistry;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.game.server.player.CPServerPlayer;
import sawfowl.commandpack.api.game.server.player.PlayerModInfo;
import sawfowl.commandpack.api.network.packets.RawPacket;
import sawfowl.commandpack.apiclasses.network.RawPacketImpl;
import sawfowl.commandpack.apiclasses.CPConnection;
import sawfowl.commandpack.utils.CommandsUtil;

import sawfowl.localeapi.api.Text;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayerImpl implements CPServerPlayer {

	@Shadow
	public ServerGamePacketListenerImpl connection;
	private static final CommandPackInstance plugin = CommandPackInstance.getInstance();

	@Override
	public void sendPacket(RawPacket packet) {
		if(getSpongeChannels().containsKey(packet.channel())) {
			getSpongeChannels().get(packet.channel()).play().sendTo(this, buffer -> buffer.writeBytes(packet.getDataAsString().getBytes(StandardCharsets.UTF_8)));
		} else if(packet instanceof RawPacketImpl impl) commandPack$sendCustomPacketPayload(plugin.getPayloadsService().isNeedRecode(impl.channel()) ? commandPack$recode(impl, null) : impl);
	}

	@Override
	public void sendMessage(Text message) {
		sendMessage(message.applyPlaceholders(Component.empty(), (CPServerPlayer) this).get());
	}

	@Override
	public void sendMessage(String message) {
		sendMessage(Text.of(message));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PlayerModInfo> getModList() {
		return (List<PlayerModInfo>) CommandsUtil.EMPTY_VARIANTS;
	}

	@Override
	public String getClientName() {
		return ((CPConnection) ((MixinAccessorServerCommonPacketListener) connection).accessor$connection()).getClientName();
	}

	@Override
	public long getPing() {
		return connection.latency();
	}

	@Override
	public float getMiningSpeed(BlockState block, Vector3i position) {
		return ((ServerPlayer) (Object) this).getDestroySpeed((net.minecraft.world.level.block.state.BlockState) block, new BlockPos(position.x(), position.y(), position.z()));
	}

	@Unique
	private void commandPack$sendCustomPacketPayload(CustomPacketPayload payload) {
		if(payload != null) connection.send(payload);
	}

	@Unique
	private CustomPacketPayload commandPack$recode(RawPacketImpl impl, CustomPacketPayload payload) {
		@SuppressWarnings("unchecked")
		@Nullable var codec = (@Nullable StreamCodec<@NotNull ByteBuf, @NotNull CustomPacketPayload>) NetworkRegistry.getCodec((Identifier) (Object) impl.channel(), ConnectionProtocol.PLAY, PacketFlow.SERVERBOUND);
		if(codec == null) return null;
		var cpCodec = plugin.getPayloadsService().findCodec(impl.channel()).get();
		ByteBuf buffer = null;
		try {
			buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), null, ConnectionType.OTHER);
			cpCodec.encode(buffer, impl);
			payload = codec.decode(buffer);
		} catch (Exception e) {
			try {
				buffer = new FriendlyByteBuf(Unpooled.buffer());
				cpCodec.encode(buffer, impl);
				payload = codec.decode(buffer);
			} catch (Exception e2) {
			}
		}
		codec = null;
		buffer = null;
		return payload;
	}

	private Map<ResourceKey, RawDataChannel> getSpongeChannels() {
		return CommandPackInstance.getInstance().getPayloadsService().getSpongeChannels();
	}

}
