package sawfowl.commandpack.api;

import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.api.data.player.PlayerData;

public interface PlayersData {

	/**
	 * Getting plugin data on the player.
	 * 
	 * @param uuid - Player {@link UUID}
	 * @return {@link PlayerData}
	 */
	Optional<PlayerData> getPlayerData(UUID uuid);

	PlayerData getOrCreatePlayerData(ServerPlayer player);

}
