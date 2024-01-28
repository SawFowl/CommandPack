package sawfowl.commandpack.api;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerLocation;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.api.commands.PluginCommand;
import sawfowl.commandpack.api.data.command.Settings;

/**
 * Used to store temporary data of players who used the functionality of the CommandPack plugin.
 * 
 * @author SawFowl
 */
public interface TempPlayerData {

	/**
	 * Registering to track a command.
	 */
	void registerCommandTracking(PluginCommand command);

	/**
	 * Adding command usage tracking.<br>
	 * Only CommandPack plugin API commands are accepted.
	 * 
	 * @param command - Settings or alias.
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
	 * @param uuid - The player uuid {@link UUID}
	 * @return If true, then the plugin monitors the execution of any command by the player.
	 */
	boolean isTrackingPlayer(UUID uuid);

	/**
	 * 
	 * Removing command usage tracking.<br>
	 * Only CommandPack plugin commands are accepted.<br>
	 * If you remove the tracking of a command to be executed by the player when the timer expires, it will not be executed.
	 * 
	 * @param command - PluginCommand or alias.
	 * @param player - The player.
	 */
	void removeCommandTracking(String command, ServerPlayer player);

	/**
	 * 
	 * Removing command usage tracking.<br>
	 * Only CommandPack plugin commands are accepted.<br>
	 * If you remove the tracking of a command to be executed by the player when the timer expires, it will not be executed.
	 * 
	 * @param command - PluginCommand or alias.
	 * @param uuid - The player uuid.
	 */
	void removeCommandTracking(String command, UUID uuid);

	/**
	 * Getting a tracking map of waiting execute player commands.<br>
	 * Changing this map has no effect on the tracking.
	 * 
	 * @param player - The player.
	 * @return - Empty {@link Optional} or tracking {@link Map}
	 */
	Optional<Map<String, Settings>> getTrackingPlayerCommands(ServerPlayer player);

	/**
	 * Getting a tracking map of waiting execute player commands.<br>
	 * Changing this map has no effect on the tracking.
	 * 
	 * @param uuid - The player uuid.
	 * @return - Empty {@link Optional} or tracking {@link Map}
	 */
	Optional<Map<String, Settings>> getTrackingPlayerCommands(UUID uuid);

	/**
	 * Change the status of receiving teleportation requests.
	 */
	public void tpToggle(ServerPlayer player);

	/**
	 * Whether the player accepts teleportation requests.
	 */
	public boolean isDisableTpRequests(ServerPlayer player);

	/**
	 * Getting the player's location before teleportation.
	 */
	public Optional<ServerLocation> getPreviousLocation(ServerPlayer player);

	/**
	 * Set the location of the player's previous location.
	 */
	public void setPreviousLocation(ServerPlayer player);

	/**
	 * Register the tracking of delays between command executions.
	 */
	void addTrackingCooldownCommand(PluginCommand command);

	/**
	 * Register the tracking of delays between command executions.
	 */
	void addTrackingCooldownCommand(String command);

	/**
	 * A map of players who cannot currently reuse a command.
	 */
	Map<UUID, Long> getTrackingMap(PluginCommand command);

	/**
	 * A map of players who cannot currently reuse a command.
	 */
	Map<UUID, Long> getTrackingMap(String command);

	/**
	 * Updating the time of the player's last activity
	 */
	void updateLastActivity(ServerPlayer player);

	/**
	 * Getting the time of the last activity of the player.
	 */
	long getLastActivity(ServerPlayer player);

	/**
	 * Checks the status of the player's current activity.
	 * @return If false, then the player was recently active.<br> 
	 * If true, then the time has passed since the last activity of the player specified in the configuration of the plugin.
	 */
	boolean isAfk(ServerPlayer player);

	/**
	 * Assigning AFK status to a player.
	 */
	void setAfkStatus(ServerPlayer player);

	/**
	 * Switching the player's status of listening to the commands of other players.<br>
	 * A player will not be added if he does not have permission to listen to commands.
	 */
	void switchSpyCommand(ServerPlayer player);

	/**
	 * Whether the player listens to other players' commands.
	 */
	boolean isSpyCommand(ServerPlayer player);

	/**
	 * Specify the current time as the activation time of invisibility in the player.
	 */
	void setVanishTime(ServerPlayer player);

	/**
	 * If true, the player is invisible and will be shown as offline in the `/seen` command.<br>
	 * In this case, the time of the last activity will be equal to the activation time of invisibility.
	 */
	boolean isVanished(ServerPlayer player);

	/**
	 * The invisibility activation time with the `/vanish` command.
	 */
	Optional<Long> getVanishEnabledTime(ServerPlayer player);

	/**
	 * Removing the invisibility activation time.
	 */
	void removeVanishEnabledTime(ServerPlayer player);

	/**
	 * Adding Audience for the /reply command
	 */
	void addReply(ServerPlayer player, Audience audience);

	/**
	 * Removing Audience for the /reply command
	 */
	void removeReply(ServerPlayer player);

	/**
	 * Getting Audience for the /reply command
	 */
	Optional<Audience> getReply(ServerPlayer player);

	Collection<String> getPlayerMods(ServerPlayer player);

}
