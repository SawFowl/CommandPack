package sawfowl.commandpack.mixins.forge.network;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import io.netty.buffer.Unpooled;
import net.kyori.adventure.text.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraftforge.network.NetworkContext;

import sawfowl.commandpack.api.mixin.network.CustomPacket;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.apiclasses.CustomPacketImpl;
import sawfowl.commandpack.apiclasses.CustomPacketPayloadImpl;
import sawfowl.localeapi.api.Text;

@Mixin(ServerPlayer.class)
public abstract class MixinForgeServerPlayerImpl implements MixinServerPlayer {

	@Shadow
	public ServerGamePacketListenerImpl connection;
	private List<String> mods = new ArrayList<String>();

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

	@Override
	public List<String> getModList() {
		if(!mods.isEmpty()) return mods;
		mods = new ArrayList<String>(NetworkContext.get(connection.getConnection()).getModList().keySet());
		return mods;
	}

	@Override
	public long getPing() {
		return connection.latency();
	}

	private FriendlyByteBuf createFriendlyByteBuf(byte[] data) {
		return new FriendlyByteBuf(Unpooled.copiedBuffer(data));
	}

	private CustomPacketPayload createCustomPacketPayload(ResourceLocation id, FriendlyByteBuf buf) {
		return new CustomPacketPayloadImpl(new Type<CustomPacketPayload>(id), buf);
	}

	private ClientboundCustomPayloadPacket createPacket(CustomPacketImpl custom) {
		return new ClientboundCustomPayloadPacket(createCustomPacketPayload(new ResourceLocation(custom.getLocation()), createFriendlyByteBuf(custom.getData().getBytes(StandardCharsets.UTF_8))));
	}

}
