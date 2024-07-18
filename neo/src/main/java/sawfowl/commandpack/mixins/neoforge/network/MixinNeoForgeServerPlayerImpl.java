package sawfowl.commandpack.mixins.neoforge.network;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import io.netty.buffer.Unpooled;

import net.kyori.adventure.text.Component;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import sawfowl.commandpack.api.mixin.network.CustomPacket;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.api.mixin.network.PlayerModInfo;
import sawfowl.commandpack.apiclasses.CustomPacketImpl;
import sawfowl.commandpack.utils.CommandsUtil;

import sawfowl.localeapi.api.Text;

@Mixin(ServerPlayer.class)
public abstract class MixinNeoForgeServerPlayerImpl implements MixinServerPlayer {

	@Shadow
	public ServerGamePacketListenerImpl connection;

	@Override
	public void sendPacket(CustomPacket packet) {
		if(packet instanceof CustomPacketImpl custom) connection.send(createPacket(custom));
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
	public long getPing() {
		return connection.latency();
	}

	private FriendlyByteBuf createFriendlyByteBuf(CustomPacketImpl custom) {
		return new FriendlyByteBuf(Unpooled.buffer()).writeResourceLocation(ResourceLocation.parse(custom.getLocation())).writeBytes(custom.getData().getBytes(StandardCharsets.UTF_8));
	}

	private ClientboundCustomPayloadPacket createPacket(CustomPacketImpl custom) {
		return ClientboundCustomPayloadPacket.CONFIG_STREAM_CODEC.decode(createFriendlyByteBuf(custom));
	}

}
