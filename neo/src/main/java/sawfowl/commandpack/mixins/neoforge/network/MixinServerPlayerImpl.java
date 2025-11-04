package sawfowl.commandpack.mixins.neoforge.network;

import java.util.List;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.math.vector.Vector3i;

import net.kyori.adventure.text.Component;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import sawfowl.commandpack.api.mixin.network.CustomPacket;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.api.mixin.network.PlayerModInfo;
import sawfowl.commandpack.api.network.packets.RawPacket;
import sawfowl.commandpack.apiclasses.network.RawPacketImpl;
import sawfowl.commandpack.apiclasses.CPConnection;
import sawfowl.commandpack.apiclasses.CustomPacketImpl;
import sawfowl.commandpack.utils.CommandsUtil;

import sawfowl.localeapi.api.Text;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayerImpl implements MixinServerPlayer {

	@Shadow
	public ServerGamePacketListenerImpl connection;

	@Override
	public void sendPacket(@SuppressWarnings("deprecation") CustomPacket packet) {
		if(packet instanceof CustomPacketImpl custom) connection.send(new RawPacketImpl(ResourceKey.resolve(custom.getLocation()), null, custom.getData()));
	}

	@Override
	public void sendPacket(RawPacket packet) {
		if(packet instanceof RawPacketImpl impl) connection.send(impl);
	}

	@Override
	public void sendMessage(Text message) {
		sendMessage(message.applyPlaceholders(Component.empty(), (MixinServerPlayer) this).get());
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
		return ((ServerPlayer) (Object) this).getDigSpeed((net.minecraft.world.level.block.state.BlockState) block, new BlockPos(position.x(), position.y(), position.z()));
	}

}
