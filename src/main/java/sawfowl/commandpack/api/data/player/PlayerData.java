package sawfowl.commandpack.api.data.player;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.api.data.kits.Kit;

/**
 * This interface allows you to view and change plugin data for a particular player.
 * 
 * @author SawFowl
 */
public interface PlayerData {

	/**
	 * Gets the player's last known username.
	 */
	String getName();

	/*
	 * Gets the unique ID for this player.
	 */
	UUID getUniqueId();

	/**
	 * Gets a {@link ServerPlayer}.
	 */
	Optional<ServerPlayer> getPlayer();

	/**
	 * Gets a {@link User}.
	 */
	Optional<User> getUser();

	/**
	 * A collection of {@link Home} points.
	 */
	List<Home> getHomes();

	/**
	 * A collection of player {@link Warp}'s.
	 */
	List<Warp> getWarps();

	/**
	 * The number of existing points at the player's home.
	 */
	long getTotalHomes();

	/**
	 * The number of existing points at the player's warps.
	 */
	long getTotalWarps();

	/**
	 * Add {@link Home} point.
	 */
	boolean addHome(Home home, int limit);

	/**
	 * Remove {@link Home} point
	 */
	boolean removeHome(String name);

	/**
	 * Add {@link Warp} point.
	 */
	boolean addWarp(Warp warp, int limit);

	/**
	 * Remove {@link Warp} point
	 */
	boolean removeWarp(String name);

	/**
	 * Check for {@link Warp} exist by name.
	 */
	boolean containsWarp(String name);

	/**
	 * Gets default {@link Home} point.
	 */
	public Optional<Home> getDefaultHome();

	/**
	 * Gets default {@link Home} point.
	 */
	public Optional<Home> getHome(String name);

	/**
	 * Get {@link Warp} whith name
	 */
	Optional<Warp> getWarp(String name);

	/**
	 * Viewing and changing a player's backpack.
	 */
	Backpack getBackpack();

	/**
	 * Set a new backpack for the player.
	 */
	void setBackpack(Backpack backpack);

	/**
	 * Getting a clickable chat menu with a list of homes.
	 * 
	 * @param locale - Localization of chat buttons.
	 * @param allowRemove - Allow deleting home points through the chat menu.
	 */
	List<Component> homesListChatMenu(Locale locale, boolean allowRemove);

	/**
	 * Getting a clickable chat menu with a list of warps.
	 * 
	 * @param locale - Localization of chat buttons.
	 * @param allowRemove - Allow deleting warp points through the chat menu.
	 */
	List<Component> warpsListChatMenu(Locale locale, Predicate<Warp> allowRemove, Predicate<Warp> allowTeleport);

	/**
	 * Sending a message to a player if he is online.
	 */
	void sendMessage(Component component);

	/**
	 * Sending a message to a player if he is online.
	 */
	void sendMessage(String string);

	GivedKit getKitGivedData(Kit kit);

	boolean isGivedKit(Kit kit);

	long getKitGivedTime(Kit kit);

	@SuppressWarnings("hiding")
	<ServerPlayer> CommandResult runCommand(Locale sourceLocale, String command) throws CommandException;

	PlayerData save();

}
