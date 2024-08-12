package sawfowl.commandpack.mixins.forge.network;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import io.netty.buffer.Unpooled;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraftforge.network.NetworkContext;
import net.minecraftforge.network.packets.ModVersions;

import sawfowl.commandpack.api.mixin.network.CustomPacket;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.api.mixin.network.PlayerModInfo;
import sawfowl.commandpack.apiclasses.CPConnection;
import sawfowl.commandpack.apiclasses.CustomPacketImpl;

import sawfowl.localeapi.api.Text;

@Mixin(value = ServerPlayer.class, remap = false)
public abstract class MixinServerPlayerImpl implements MixinServerPlayer {

	@Shadow
	public ServerGamePacketListenerImpl connection;
	private List<PlayerModInfo> mods = new ArrayList<PlayerModInfo>();

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
	public List<PlayerModInfo> getModList() {
		if(!mods.isEmpty()) return mods;
		mods = NetworkContext.get(connection.getConnection()).getModList().entrySet().stream().map(info -> createModInfo(info.getValue(), info.getKey())).toList();
		return mods;
	}

	@Override
	public String getClientName() {
		return ((CPConnection) connection.getConnection()).getClientName();
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

	private PlayerModInfo createModInfo(ModVersions.Info info, String id) {
		return new PlayerModInfo() {

			String modId = id;
			String version = info.version();
			String name = info.name();
			String fullInfo = info.name() + "(" + id + ")" + " - v" + info.version();
			Component component = Component.text(info.name()).color(TextColor.color(255, 137, 0))
				.append(Component.text(" - ").color(TextColor.color(255, 255, 255)))
				.append(Component.text("v" + info.version()).color(TextColor.color(135, 70, 222)));

			@Override
			public String getVersion() {
				return version;
			}
			
			@Override
			public String getName() {
				return name;
			}
			
			@Override
			public String getId() {
				return modId;
			}

			@Override
			public String getFullInfo() {
				return fullInfo;
			}
			
			@Override
			public Component asComponent() {
				return component;
			}
		};
	}

}
