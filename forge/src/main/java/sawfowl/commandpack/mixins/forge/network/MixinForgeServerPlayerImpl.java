package sawfowl.commandpack.mixins.forge.network;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraftforge.network.ConnectionData;
import net.minecraftforge.network.NetworkHooks;

import sawfowl.commandpack.api.mixin.network.CustomPacket;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.apiclasses.CustomPacketImpl;
import sawfowl.localeapi.api.Text;

@Mixin(ServerPlayer.class)
public abstract class MixinForgeServerPlayerImpl implements MixinServerPlayer {

	@Shadow
	public ServerGamePacketListenerImpl connection;
	private List<String> mods = new ArrayList<String>();

	@Override
	public void sendPacket(CustomPacket packet) {
		if(packet instanceof CustomPacketImpl custom) connection.send(custom.getPacket());
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
		ConnectionData data = NetworkHooks.getConnectionData(connection.connection);
		if(data == null) return mods;
		mods = data.getModList();
		return mods;
	}

}
