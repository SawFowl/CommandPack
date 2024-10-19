package sawfowl.commandpack.api;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.api.data.player.PlayerData;
import sawfowl.commandpack.api.data.player.Warp;

/**
 * @author SawFowl
 */
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
	 * Getting all registered warps.
	 */
	Stream<Warp> streamAllWarps();

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
	void addWarp(Warp warp, @Nullable PlayerData data);

	/**
	 * Adding and save admin {@link Warp}.
	 */
	void addAndSaveWarp(Warp warp, @Nullable PlayerData data);

	/**
	 * Remove admin {@link Warp}.
	 */
	boolean removeWarp(String name, @Nullable PlayerData data);

	/**
	 * Search admin {@link Warp} by name.<br>
	 * A search filter is used.
	 */
	Optional<Warp> getAdminWarp(String name, Predicate<Warp> predicate);

	/**
	 * Temporary player data.<br>
	 * CommandPack does not save this data to disk.
	 */
	TempPlayerData getTempData();

}
