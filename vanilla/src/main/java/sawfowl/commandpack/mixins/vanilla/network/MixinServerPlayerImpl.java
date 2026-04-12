package sawfowl.commandpack.mixins.vanilla.network;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.network.channel.raw.RawDataChannel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.math.vector.Vector3i;

import io.netty.buffer.Unpooled;

import net.kyori.adventure.text.Component;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.game.server.player.CPServerPlayer;
import sawfowl.commandpack.api.game.server.player.PlayerModInfo;
import sawfowl.commandpack.api.network.packets.RawPacket;
import sawfowl.commandpack.apiclasses.CPConnection;
import sawfowl.commandpack.utils.CommandsUtil;

import sawfowl.localeapi.api.Text;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayerImpl implements CPServerPlayer {

	@Shadow public ServerGamePacketListenerImpl connection;

	@Override
	public void sendPacket(RawPacket packet) {
		if(commandPack$getSpongeChannels().containsKey(packet.channel())) {
			commandPack$getSpongeChannels().get(packet.channel()).play().sendTo(this, buffer -> buffer.writeBytes(packet.getDataAsString().getBytes(StandardCharsets.UTF_8)));
		} else connection.send(createPacket(packet));
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

	@Unique
	private FriendlyByteBuf commandPack$createFriendlyByteBuf(RawPacket custom) {
		return new FriendlyByteBuf(Unpooled.buffer()).writeIdentifier((Identifier) (Object) custom.channel()).writeBytes(custom.getDataAsString().getBytes(StandardCharsets.UTF_8));
	}

	private ClientboundCustomPayloadPacket createPacket(RawPacket custom) {
		return ClientboundCustomPayloadPacket.CONFIG_STREAM_CODEC.decode(commandPack$createFriendlyByteBuf(custom));
	}

	@Override
	public float getMiningSpeed(BlockState block, Vector3i position) {
		return ((ServerPlayer) (Object) this).getDestroySpeed((net.minecraft.world.level.block.state.BlockState) block);
	}

	@Unique
	private Map<ResourceKey, RawDataChannel> commandPack$getSpongeChannels() {
		return CommandPackInstance.getInstance().getPayloadsService().getSpongeChannels();
	}

}
