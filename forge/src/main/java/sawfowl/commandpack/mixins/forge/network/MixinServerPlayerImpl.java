package sawfowl.commandpack.mixins.forge.network;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
import net.kyori.adventure.text.format.TextColor;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraftforge.network.NetworkContext;
import net.minecraftforge.network.packets.ModVersions;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.game.server.player.CPServerPlayer;
import sawfowl.commandpack.api.game.server.player.PlayerModInfo;
import sawfowl.commandpack.api.network.packets.RawPacket;
import sawfowl.commandpack.apiclasses.CPConnection;
import sawfowl.localeapi.api.Text;

@Mixin(value = ServerPlayer.class, remap = false)
public abstract class MixinServerPlayerImpl implements CPServerPlayer {

	@Shadow public ServerGamePacketListenerImpl connection;
	private List<PlayerModInfo> mods = new ArrayList<PlayerModInfo>();

	@Override
	public void sendPacket(RawPacket packet) {
		if(getSpongeChannels().containsKey(packet.channel())) {
			getSpongeChannels().get(packet.channel()).play().sendTo(this, buffer -> buffer.writeBytes(packet.getDataAsString().getBytes(StandardCharsets.UTF_8)));
		} else connection.send(createPacket(packet));}

	@Override
	public void sendMessage(Text message) {
		sendMessage(message.applyPlaceholders(Component.empty(), (CPServerPlayer) this).get());
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

	private FriendlyByteBuf createFriendlyByteBuf(RawPacket custom) {
		return new FriendlyByteBuf(Unpooled.buffer()).writeIdentifier((Identifier) (Object) custom.channel()).writeBytes(custom.getDataAsString().getBytes(StandardCharsets.UTF_8));
	}

	private ClientboundCustomPayloadPacket createPacket(RawPacket custom) {
		return ClientboundCustomPayloadPacket.CONFIG_STREAM_CODEC.decode(createFriendlyByteBuf(custom));
	}

	@Override
	public float getMiningSpeed(BlockState block, Vector3i position) {
		return ((ServerPlayer) (Object) this).getDestroySpeed((net.minecraft.world.level.block.state.BlockState) block, new BlockPos(position.x(), position.y(), position.z()));
	}

	private Map<ResourceKey, RawDataChannel> getSpongeChannels() {
		return CommandPackInstance.getInstance().getPayloadsService().getSpongeChannels();
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
