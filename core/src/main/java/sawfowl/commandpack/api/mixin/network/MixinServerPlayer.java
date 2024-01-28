package sawfowl.commandpack.api.mixin.network;

import java.util.List;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.localeapi.api.Text;

public interface MixinServerPlayer extends ServerPlayer {

	public static MixinServerPlayer cast(ServerPlayer player) {
		return (MixinServerPlayer) player;
	}

	void sendPacket(CustomPacket packet);

	void sendMessage(Text message);

	void sendMessage(String message);

	List<String> getModList();

}
