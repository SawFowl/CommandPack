package sawfowl.commandpack.api;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.configure.configs.commands.CommandSettings;

/**
 * Used to store temporary data of players who used the functionality of the CommandPack plugin.
 */
public interface ITempPlayerData {

	/**
	 * Adding command usage tracking.<br>
	 * Only CommandPack plugin commands are accepted.
	 * 
	 * @param command - CommandSettings or alias.
	 * @param player - The player.
	 */
	void addCommandTracking(String command, ServerPlayer player);

	/**
	 * Tracking check.
	 * 
	 * @param player - The player.
	 * @return If true, then the plugin monitors the execution of any command by the player.
	 */
	boolean isTrackingPlayer(ServerPlayer player);

	/**
	 * Tracking check.
	 * 
	 * @param player - The player {@link UUID}
	 * @return If true, then the plugin monitors the execution of any command by the player.
	 */
	boolean isTrackingPlayer(UUID uuid);

	/**
	 * 
	 * Removing command usage tracking.<br>
	 * Only CommandPack plugin commands are accepted.<br>
	 * If you remove the tracking of a command to be executed by the player when the timer expires, it will not be executed.
	 * 
	 * @param command - Command or alias.
	 * @param player - The player.
	 */
	void removeCommandTracking(String command, ServerPlayer player);

	/**
	 * 
	 * Removing command usage tracking.<br>
	 * Only CommandPack plugin commands are accepted.<br>
	 * If you remove the tracking of a command to be executed by the player when the timer expires, it will not be executed.
	 * 
	 * @param command - Command or alias.
	 * @param player - The player.
	 */
	void removeCommandTracking(String command, UUID uuid);

	/**
	 * Getting a tracking map of waiting execute player commands.<br>
	 * Changing this map has no effect on the tracking.
	 * 
	 * @param player - CommandSettings or alias.
	 * @return - Empty {@link Optional} or tracking {@link Map}
	 */
	Optional<Map<String, CommandSettings>> getTrackingPlayerCommands(ServerPlayer player);

	/**
	 * Getting a tracking map of waiting execute player commands.<br>
	 * Changing this map has no effect on the tracking.
	 * 
	 * @param player - CommandSettings or alias.
	 * @return - Empty {@link Optional} or tracking {@link Map}
	 */
	Optional<Map<String, CommandSettings>> getTrackingPlayerCommands(UUID uuid);

}
