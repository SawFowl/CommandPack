package sawfowl.commandpack.mixins.vanilla.network;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.network.channel.raw.RawDataChannel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.math.vector.Vector3i;

import io.netty.buffer.Unpooled;

import net.kyori.adventure.text.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.mixin.network.CustomPacket;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.api.mixin.network.PlayerModInfo;
import sawfowl.commandpack.api.network.packets.RawPacket;
import sawfowl.commandpack.apiclasses.CPConnection;
import sawfowl.commandpack.apiclasses.CustomPacketImpl;
import sawfowl.commandpack.utils.CommandsUtil;

import sawfowl.localeapi.api.Text;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayerImpl implements MixinServerPlayer {

	@Shadow public ServerGamePacketListenerImpl connection;

	@Override
	public void sendPacket(@SuppressWarnings("deprecation") CustomPacket packet) {
		if(packet instanceof CustomPacketImpl custom) {
			ResourceKey channel = ResourceKey.resolve(custom.getLocation());
			if(getSpongeChannels().containsKey(channel)) {
				getSpongeChannels().get(channel).play().sendTo(this, buffer -> buffer.writeString(custom.getData()));
			} else connection.send(createPacket(custom));
		}
	}

	@Override
	public void sendPacket(RawPacket packet) {
		if(getSpongeChannels().containsKey(packet.channel())) {
			getSpongeChannels().get(packet.channel()).play().sendTo(this, buffer -> buffer.writeBytes(packet.getDataAsString().getBytes(StandardCharsets.UTF_8)));
		} else connection.send(createPacket(packet));
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

	private FriendlyByteBuf createFriendlyByteBuf(RawPacket custom) {
		return new FriendlyByteBuf(Unpooled.buffer()).writeResourceLocation((ResourceLocation) (Object) custom.channel()).writeBytes(custom.getDataAsString().getBytes(StandardCharsets.UTF_8));
	}

	private ClientboundCustomPayloadPacket createPacket(RawPacket custom) {
		return ClientboundCustomPayloadPacket.CONFIG_STREAM_CODEC.decode(createFriendlyByteBuf(custom));
	}

	private FriendlyByteBuf createFriendlyByteBuf(CustomPacketImpl custom) {
		return new FriendlyByteBuf(Unpooled.buffer()).writeResourceLocation(ResourceLocation.parse(custom.getLocation())).writeBytes(custom.getData().getBytes(StandardCharsets.UTF_8));
	}

	private ClientboundCustomPayloadPacket createPacket(CustomPacketImpl custom) {
		return ClientboundCustomPayloadPacket.CONFIG_STREAM_CODEC.decode(createFriendlyByteBuf(custom));
	}

	@Override
	public float getMiningSpeed(BlockState block, Vector3i position) {
		return ((ServerPlayer) (Object) this).getDestroySpeed((net.minecraft.world.level.block.state.BlockState) block);
	}

	private Map<ResourceKey, RawDataChannel> getSpongeChannels() {
		return CommandPackInstance.getInstance().getPayloadsService().getSpongeChannels();
	}

}
