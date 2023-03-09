package sawfowl.commandpack.api;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.api.data.player.PlayerData;
import sawfowl.commandpack.api.data.player.Warp;

public interface PlayersData {

	/**
	 * A collection of plugin data about all the players who have ever logged on to the server.
	 */
	Collection<PlayerData> getPlayersData();

	/**
	 * Getting plugin data on the player.
	 * 
	 * @param uuid - Player {@link UUID}
	 * @return {@link PlayerData}
	 */
	Optional<PlayerData> getPlayerData(UUID uuid);

	/**
	 * Getting plugin data on the player.
	 * 
	 * @return {@link PlayerData}
	 */
	PlayerData getOrCreatePlayerData(ServerPlayer player);

	/**
	 * Getting a collection of all players' {@link Warp}s.
	 */
	Collection<Warp> getPlayersWarps();

	/**
	 * Get a collection of all player {@link Warp}s with filtering by condition.
	 */
	Collection<Warp> getPlayersWarps(Predicate<Warp> allowTeleport);

	/**
	 * Search {@link Warp} by name.
	 */
	Optional<Warp> getWarp(String name);

	/**
	 * {@link Warp} search by name and condition.
	 */
	Optional<Warp> getWarp(String name, Predicate<Warp> filter);

	/**
	 * Getting a collection of administrative {@link Warp}s.
	 */
	Map<String, Warp> getAdminWarps();

	/**
	 * Adding admin {@link Warp}.
	 */
	void addAdminWarp(Warp warp);

	/**
	 * Adding and save admin {@link Warp}.
	 */
	void addAndSaveAdminWarp(Warp warp);

	/**
	 * Remove admin {@link Warp}.
	 */
	boolean removeAdminWarp(String name);

	/**
	 * Search admin {@link Warp} by name.
	 */
	public Optional<Warp> getAdminWarp(String name);

	/**
	 * Search admin {@link Warp} by name.<br>
	 * A search filter is used.
	 */
	Optional<Warp> getAdminWarp(String name, Predicate<Warp> predicate);

}
