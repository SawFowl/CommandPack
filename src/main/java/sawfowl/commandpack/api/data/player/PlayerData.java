package sawfowl.commandpack.api.data.player;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.text.Component;

/**
 * This interface allows you to view and change plugin data for a particular player.
 */
@ConfigSerializable
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
	 * The number of existing points at the player's home.
	 */
	long getTotalHomes();

	/**
	 * Add {@link Home} point.
	 */
	boolean addHome(Home home, int limit);

	/**
	 * Remove home point
	 */
	boolean removeHome(String name);

	/**
	 * Gets default {@link Home} point.
	 */
	public Optional<Home> getDefaultHome();

	/**
	 * Gets default {@link Home} point.
	 */
	public Optional<Home> getHome(String name);

	/**
	 * Getting a clickable chat menu with a list of houses.
	 * 
	 * @param locale - Localization of chat buttons.
	 * @param allowRemove - Allow deleting house points through the chat menu.
	 */
	List<Component> homesListChatMenu(Locale locale, boolean allowRemove);

	/**
	 * Sending a message to a player if he is online.
	 */
	public void sendMessage(Component component);

	/**
	 * Sending a message to a player if he is online.
	 */
	public void sendMessage(String string);

	PlayerData save();

}
