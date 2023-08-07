package sawfowl.commandpack.configure.locale;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.localeapi.api.ConfigTypes;
import sawfowl.localeapi.api.LocaleService;
import sawfowl.localeapi.api.TextUtils;
import sawfowl.localeapi.utils.AbstractLocaleUtil;

public class Locales {

	private final LocaleService localeService;
	private final boolean json;
	private String[] localesTags;
	private String pluginid = "commandpack";
	public Locales(LocaleService localeService, boolean json) {
		this.localeService = localeService;
		this.json = json;
		localeService.localesExist(pluginid);
		localeService.createPluginLocale(pluginid, ConfigTypes.HOCON, org.spongepowered.api.util.locale.Locales.DEFAULT);
		localeService.createPluginLocale(pluginid, ConfigTypes.HOCON, org.spongepowered.api.util.locale.Locales.RU_RU);
		generateDefault();
		generateRu();
	}

	public LocaleService getLocaleService() {
		return localeService;
	}

	public String[] getLocalesTags() {
		if(localesTags != null) return localesTags;
		return localesTags = localeService.getLocalesList().stream().map(Locale::toLanguageTag).collect(Collectors.toList()).stream().toArray(String[]::new);
	}

	public Component getText(Locale locale, Object... path) {
		return getAbstractLocaleUtil(locale).getComponent(json, path);
	}

	public List<Component> getListTexts(Locale locale, Object... path) {
		return getAbstractLocaleUtil(locale).getListComponents(json, path);
	}

	public String getString(Locale locale, Object... path) {
		return TextUtils.serializeLegacy(getText(locale, path));
	}

	public Component getTextWithReplaced(Locale locale, Map<String, String> map, Object... path) {
		return replace(getText(locale, path), map);
	}

	public Component getTextReplaced(Locale locale, Map<String, Component> map, Object... path) {
		return replaceComponent(getText(locale, path), map);
	}

	public Component getTextFromDefault(Object... path) {
		return getAbstractLocaleUtil(org.spongepowered.api.util.locale.Locales.DEFAULT).getComponent(json, path);
	}

	private void generateDefault() {
		Locale locale = org.spongepowered.api.util.locale.Locales.DEFAULT;

		boolean save = check(locale, toText("&eThere is no economy plugin on the server. Some of the functionality will not be available."), null, LocalesPaths.ECONOMY_NOT_FOUND);
		save = check(locale, toText("Failed to add game currency to player " + Placeholders.PLAYER + "."), null, LocalesPaths.ECONOMY_ERROR_GIVE_MONEY) || save;
		save = check(locale, toText("Failed to take game currency from player " + Placeholders.PLAYER + "."), null, LocalesPaths.ECONOMY_ERROR_TAKE_MONEY) || save;

		save = check(locale, toText("d"), null, LocalesPaths.TIME_DAYS) || save;
		save = check(locale, toText("h"), null, LocalesPaths.TIME_HOUR) || save;
		save = check(locale, toText("m"), null, LocalesPaths.TIME_MINUTE) || save;
		save = check(locale, toText("s"), null, LocalesPaths.TIME_SECOND) || save;

		save = check(locale, toText("&7[&cRemove&7] "), null, LocalesPaths.REMOVE) || save;
		save = check(locale, toText("&7[&aTeleport&7] "), null, LocalesPaths.TELEPORT) || save;
		save = check(locale, toText("&7[&eTeleport&7] "), null, LocalesPaths.TELEPORTCLICKABLE) || save;
		save = checkList(locale, Arrays.asList(
				toText("&b================================"),
				toText("&dGreetings &e" + Placeholders.PLAYER + "&d. Welcome to the server."),
				toText("&dWe hope you like it here."),
				toText("&b================================")
				), null, LocalesPaths.MOTD) || save;
		save = check(locale, toText("&7[&a+&7]&r " + Placeholders.PREFIX + " " + Placeholders.PLAYER + " " + Placeholders.SUFFIX), null, LocalesPaths.JOIN_MESSAGE) || save;
		save = check(locale, toText("&7[&2+&7]&r " + Placeholders.PREFIX + " " + Placeholders.PLAYER + " " + Placeholders.SUFFIX), null, LocalesPaths.FIRST_JOIN_MESSAGE) || save;
		save = check(locale, toText("&7[&c-&7]&r " + Placeholders.PREFIX + " " + Placeholders.PLAYER + " " + Placeholders.SUFFIX), null, LocalesPaths.LEAVE_MESSAGE) || save;
		save = check(locale, toText("&aYour inventory will be saved. The chance of saving each item is &e" + Placeholders.VALUE + "%&a. Items will be returned to you after respawn."), null, LocalesPaths.KEEP_INVENTORY) || save;
		save = check(locale, toText("&aYour experience will be saved to the extent of &e" + Placeholders.VALUE + "%&a. Experience will be returned to you after respawn."), null, LocalesPaths.KEEP_EXP) || save;
		save = check(locale, "The player " + Placeholders.PLAYER + " login to the server with the mods: " + Placeholders.VALUE + ".", null, LocalesPaths.PLAYER_MODS_LIST) || save;
		save = check(locale, toText("&cThe server has a ban on the use of some of the mods you have&f:\n&c" + Placeholders.VALUE + "."), null, LocalesPaths.ILLEGAL_MODS_LIST) || save;

		save = check(locale, toText("Server"), null, LocalesPaths.NAME_SYSTEM) || save;
		save = check(locale, toText("PluginCommand block"), null, LocalesPaths.NAME_COMMANDBLOCK) || save;
		save = check(locale, toText("Minecart with command block"), null, LocalesPaths.NAME_COMMANDBLOCK_MINECART) || save;
		save = check(locale, toText("Unknown"), null, LocalesPaths.NAME_UNKNOWN) || save;
		save = check(locale, toText("&6&lBackpack"), null, LocalesPaths.NAME_BACKPACK) || save;

		save = check(locale, toText("&cPlayer " + Placeholders.PLAYER + "&c is offline."), null, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_IS_OFFLINE) || save;
		save = check(locale, toText("&cYou need to specify the nickname of the player who is now online or was previously on the server."), null, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT) || save;
		save = check(locale, toText("&cYou need to specify the nickname of the player online."), null, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT) || save;
		save = check(locale, toText("&cYou can't point to yourself."), null, LocalesPaths.COMMANDS_EXCEPTION_TARGET_SELF) || save;
		save = check(locale, toText("&cYou must specify a name."), null, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT) || save;
		save = check(locale, toText("&cYou must specify a type."), null, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT) || save;
		save = check(locale, toText("&cYou must specify a value."), null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT) || save;
		save = check(locale, toText("&cSpecify the duration in ISO time format."), null, LocalesPaths.COMMANDS_EXCEPTION_DURATION_NOT_PRESENT) || save;
		save = check(locale, toText("&cIt is necessary to specify the command argument as boolean. More information about the arguments of the entered command can be found in the documentation of the plugin."), null, LocalesPaths.COMMANDS_EXCEPTION_BOOLEAN_NOT_PRESENT) || save;
		save = check(locale, toText("&cYou need to specify the coordinates."), null, LocalesPaths.COMMANDS_EXCEPTION_LOCATION_NOT_PRESENT) || save;
		save = check(locale, toText("&cYou have to specify the world."), null, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT) || save;
		save = check(locale, toText("&cWait &e" + Placeholders.DELAY + "&c before using this command again."), null, LocalesPaths.COMMANDS_COOLDOWN) || save;
		save = check(locale, toText("&aCommand activation via &e" + Placeholders.DELAY + "&a."), null, LocalesPaths.COMMANDS_WAIT) || save;
		save = check(locale, toText(Placeholders.SOURCE + " ran the command: /" + Placeholders.COMMAND + Placeholders.ARGS + ""), null, LocalesPaths.COMMANDS_LOG) || save;
		save = check(locale, toText("Settings \"" + Placeholders.COMMAND + "\" is not registered for tracking."), null, LocalesPaths.COMMANDS_NOT_TRACKING) || save;
		save = check(locale, toText("&cExecution of command &e\"&5" + Placeholders.COMMAND + "&e\"&c has stopped because you entered another command."), null, LocalesPaths.COMMANDS_STOP_TRACKING_COMMAND) || save;
		save = check(locale, toText("&cExecution of command &e\"&5" + Placeholders.COMMAND + "&e\"&c is stopped because you have moved."), null, LocalesPaths.COMMANDS_STOP_TRACKING_MOVING) || save;;
		save = check(locale, toText("&eThe amount of money written off is &2" + Placeholders.MONEY + "&e for command &e\"&5" + Placeholders.COMMAND + "&e\"&e."), null, LocalesPaths.COMMANDS_TAKE_MONEY) || save;
		save = check(locale, toText("&cYou need to have a balance of &2" + Placeholders.MONEY + "&c to execute command &e\"&5" + Placeholders.COMMAND + "&e\"&c."), null, LocalesPaths.COMMANDS_ERROR_TAKE_MONEY) || save;
		save = check(locale, toText("&cYou have to hold an item in your main hand."), null, LocalesPaths.COMMANDS_HAT_NO_ITEM) || save;
		save = check(locale, toText("&cThis item cannot be put on your head."), null, LocalesPaths.COMMANDS_HAT_BLACKLIST_ITEM) || save;
		save = check(locale, toText("&cThe player's inventory &e" + Placeholders.PLAYER + "&c is full. Click on this message if you want to replace an item on a player's head. The item on the player's head will be lost!"), null, LocalesPaths.COMMANDS_HAT_BLACKLIST_ITEM) || save;
		save = check(locale, toText("&aYou put an item on the head of player " + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_HAT_SUCCESS_OTHER) || save;
		save = check(locale, toText("&aThe spawn point has been set."), null, LocalesPaths.COMMANDS_SETSPAWN_SUCCESS) || save;
		save = check(locale, toText("&aYou teleported to the spawn point."), null, LocalesPaths.COMMANDS_SPAWN_SUCCESS) || save;
		save = check(locale, toText("&cYour limit does not allow you to set a new home point."), null, LocalesPaths.COMMANDS_SETHOME_LIMIT) || save;
		save = check(locale, toText("&aYou have set the home point &e" + Placeholders.HOME + "&a."), null, LocalesPaths.COMMANDS_SETHOME_SUCCESS) || save;
		save = check(locale, toText("&cA home point with a name of &e" + Placeholders.HOME + "&c does not exist."), null, LocalesPaths.COMMANDS_HOME_NOT_FOUND) || save;
		save = check(locale, toText("&cNo home point is found. Use the &e/sethome&c command to set a home point."), null, LocalesPaths.COMMANDS_HOME_NOT_SET) || save;
		save = check(locale, toText("&cHome point is not available. Perhaps the world is not loaded."), null, LocalesPaths.COMMANDS_HOME_TELEPORT_ERROR) || save;
		save = check(locale, toText("&aYou teleported to home &e" + Placeholders.HOME + "&a."), null, LocalesPaths.COMMANDS_HOME_SUCCESS) || save;
		save = check(locale, toText("&bHomes"), null, LocalesPaths.COMMANDS_HOME_LIST) || save;
		save = check(locale, toText("&cWarp with the specified name was not found."), null, LocalesPaths.COMMANDS_WARP_NOT_FOUND) || save;
		save = check(locale, toText("&aYou have moved to warp " + Placeholders.WARP + "&a."), null, LocalesPaths.COMMANDS_WARP_SUCCESS) || save;
		save = check(locale, toText("&aYou've been transferred to warp " + Placeholders.WARP + "&a."), null, LocalesPaths.COMMANDS_WARP_SUCCESS_OTHER) || save;
		save = check(locale, toText("&aYou moved &e" + Placeholders.PLAYER + " to warp " + Placeholders.WARP + "&a."), null, LocalesPaths.COMMANDS_WARP_SUCCESS_STAFF) || save;
		save = check(locale, toText("&cA warp with the specified name already exists."), null, LocalesPaths.COMMANDS_SETWARP_EXIST) || save;
		save = check(locale, toText("&cYour limit does not allow you to set a new warp point."), null, LocalesPaths.COMMANDS_SETWARP_LIMIT) || save;
		save = check(locale, toText("&aWarp is set."), null, LocalesPaths.COMMANDS_SETWARP_SUCCESS) || save;
		save = check(locale, toText("&aAdmin warp is set."), null, LocalesPaths.COMMANDS_SETWARP_SUCCESS_ADMIN) || save;
		save = check(locale, toText("&cThe list of warps is empty."), null, LocalesPaths.COMMANDS_WARPS_EMPTY) || save;
		save = check(locale, toText("&eWarps: "), null, LocalesPaths.COMMANDS_WARPS_LIST) || save;
		save = check(locale, toText("&eThe list of warps is being compiled. Please wait."), null, LocalesPaths.COMMANDS_WARPS_WAIT) || save;
		save = check(locale, toText("&bWarps: ============ " + Placeholders.SERVER + " | " + Placeholders.PLAYER + " ================"), null, LocalesPaths.COMMANDS_WARPS_HEADER) || save;
		save = check(locale, toText("&7[&4Server&7]"), null, LocalesPaths.COMMANDS_WARPS_SERVER_GROUP) || save;
		save = check(locale, toText("&7[&ePlayers&7]"), null, LocalesPaths.COMMANDS_WARPS_PLAYERS_GROUP) || save;
		save = check(locale, toText("&aYou can now accept teleportation requests."), null, LocalesPaths.COMMANDS_TPTOGGLE_ENABLE) || save;
		save = check(locale, toText("&aIncoming teleportation requests will now be automatically rejected."), null, LocalesPaths.COMMANDS_TPTOGGLE_DISABLE) || save;
		save = check(locale, toText("&cThis player does not accept teleportation requests."), null, LocalesPaths.COMMANDS_TPA_DISABLE_TP_REQUESTS) || save;
		save = check(locale, toText("&cThe request is not valid. The player is offline."), null, LocalesPaths.COMMANDS_TPA_SOURCE_OFFLINE) || save;
		save = check(locale, toText("&aYou sent a teleportation request."), null, LocalesPaths.COMMANDS_TPA_SUCCESS) || save;
		save = check(locale, toText("&aThe teleportation request is accepted."), null, LocalesPaths.COMMANDS_TPA_ACCEPTED) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a is requesting permission to teleport to you. Click this message to accept the request."), null, LocalesPaths.COMMANDS_TPA_REQUEST_MESSAGE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a asks you to teleport to him/her. Click this message to accept the request."), null, LocalesPaths.COMMANDS_TPA_REQUEST_HERE_MESSAGE) || save;
		save = check(locale, toText("&cFailed to find a position in the world '" + Placeholders.WORLD + "' for teleportation. The search attempt limit has been reached: " + Placeholders.LIMIT + "."), null, LocalesPaths.COMMANDS_RANDOM_TELEPORT_POSITION_SEARCH_ERROR_STAFF) || save;
		save = check(locale, toText("&cUnable to find teleportation position. Try again."), null, LocalesPaths.COMMANDS_RANDOM_TELEPORT_POSITION_SEARCH_ERROR) || save;
		save = check(locale, toText("&aPlayer &e" + Placeholders.PLAYER + "&a teleported to location &e<" + Placeholders.WORLD + "> " + Placeholders.LOCATION + "&a."), null, LocalesPaths.COMMANDS_RANDOM_TELEPORT_SUCCES_STAFF) || save;
		save = check(locale, toText("&aYou teleported to location &e<" + Placeholders.WORLD + "> " + Placeholders.LOCATION + "&a."), null, LocalesPaths.COMMANDS_RANDOM_TELEPORT_SUCCES) || save;
		save = check(locale, toText("&eThe search for a proper location has started. Please wait."), null, LocalesPaths.COMMANDS_RANDOM_TELEPORT_WAIT) || save;
		save = check(locale, toText("&eTeleportation has been canceled. Try again."), null, LocalesPaths.COMMANDS_RANDOM_TELEPORT_CANCELLED) || save;
		save = check(locale, toText("&cThe previous location was not found."), null, LocalesPaths.COMMANDS_BACK_EMPTY) || save;
		save = check(locale, toText("&cThe world in which the previous location is located is not loaded."), null, LocalesPaths.COMMANDS_BACK_NOT_LOADED_WORLD) || save;
		save = check(locale, toText("&cAn unacceptable location is specified."), null, LocalesPaths.COMMANDS_TPPOS_INVALID_LOCATION) || save;
		save = check(locale, toText("&aYou have cleared your inventory."), null, LocalesPaths.COMMANDS_CLEAR_SUCCES) || save;
		save = check(locale, toText("&eYour inventory has been cleared."), null, LocalesPaths.COMMANDS_CLEAR_SUCCES_OTHER) || save;
		save = check(locale, toText("&aYou cleared &e" + Placeholders.PLAYER + "&a inventory."), null, LocalesPaths.COMMANDS_CLEAR_SUCCES_STAFF) || save;
		save = check(locale, toText("&aRepairs has been completed."), null, LocalesPaths.COMMANDS_REPAIR_SUCCES) || save;
		save = check(locale, toText("&aYour item(s) has been repaired."), null, LocalesPaths.COMMANDS_REPAIR_SUCCES_OTHER) || save;
		save = check(locale, toText("&aYou repaired the item(s) to player &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_REPAIR_SUCCES_STAFF) || save;
		save = check(locale, toText("&cFailed to determine the position for the jump."), null, LocalesPaths.COMMANDS_JUMP_EXCEPTION) || save;
		save = check(locale, toText("&aNow you can fly."), null, LocalesPaths.COMMANDS_FLY_ENABLE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a can now fly."), null, LocalesPaths.COMMANDS_FLY_ENABLE_STAFF) || save;
		save = check(locale, toText("&aFlight disabled."), null, LocalesPaths.COMMANDS_FLY_DISABLE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a can't fly anymore."), null, LocalesPaths.COMMANDS_FLY_DISABLE_STAFF) || save;
		save = check(locale, toText("&aNow you are invulnerable."), null, LocalesPaths.COMMANDS_GODMODE_ENABLE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a now has invulnerability."), null, LocalesPaths.COMMANDS_GODMODE_ENABLE_STAFF) || save;
		save = check(locale, toText("&aInvulnerability is disabled."), null, LocalesPaths.COMMANDS_GODMODE_DISABLE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a no longer has invulnerability."), null, LocalesPaths.COMMANDS_GODMODE_DISABLE_STAFF) || save;
		save = check(locale, toText("&aYou have set your speed multiplier: &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_SPEED_SELF) || save;
		save = check(locale, toText("&aYou have set a player speed multiplier &e" + Placeholders.PLAYER + "&a: &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_SPEED_STAFF) || save;
		save = check(locale, toText("&aYou have been set a multiplier for your speed: &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_SPEED_OTHER) || save;
		save = check(locale, toText("\n&aThe speed has been changed for flight mode because of being in the air."), null, LocalesPaths.COMMANDS_SPEED_FLY) || save;
		save = check(locale, toText("default"), null, LocalesPaths.COMMANDS_SPEED_DEFAULT) || save;
		save = check(locale, toText("&a&lTrash"), null, LocalesPaths.COMMANDS_DISPOSAL_TITLE) || save;
		save = check(locale, toText("&aYour game mode has been changed to" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_GAMEMODE_SUCCESS) || save;
		save = check(locale, toText("&aPlayer &e" + Placeholders.PLAYER + " &ais set to play mode " + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_GAMEMODE_OTHER) || save;
		save = check(locale, toText("&dcreative"), null, LocalesPaths.COMMANDS_GAMEMODE_CREATIVE) || save;
		save = check(locale, toText("&7spectator"), null, LocalesPaths.COMMANDS_GAMEMODE_SPECTATOR) || save;
		save = check(locale, toText("&2survival"), null, LocalesPaths.COMMANDS_GAMEMODE_SURVIVAL) || save;
		save = check(locale, toText("&aadventure"), null, LocalesPaths.COMMANDS_GAMEMODE_ADVENTURE) || save;
		save = check(locale, toText("&aYou have set a clear weather in the world  &e" + Placeholders.WORLD + "&a."), null, LocalesPaths.COMMANDS_WEATHER_SUN) || save;
		save = check(locale, toText("&aYou have set the rainy weather in the world  &e" + Placeholders.WORLD + "&a."), null, LocalesPaths.COMMANDS_WEATHER_RAIN) || save;
		save = check(locale, toText("&aYou have set a thunder weather in the world &e" + Placeholders.WORLD + "&a."), null, LocalesPaths.COMMANDS_WEATHER_THUNDER) || save;
		save = check(locale, toText("&aYou have set the morning in the world &e" + Placeholders.WORLD + "&a."), null, LocalesPaths.COMMANDS_TIME_MORNING) || save;
		save = check(locale, toText("&aYou have set the day in the world &e" + Placeholders.WORLD + "&a."), null, LocalesPaths.COMMANDS_TIME_DAY) || save;
		save = check(locale, toText("&aYou have set an evening in the world &e" + Placeholders.WORLD + "&a."), null, LocalesPaths.COMMANDS_TIME_EVENING) || save;
		save = check(locale, toText("&aYou have set the night in the world &e" + Placeholders.WORLD + "&a."), null, LocalesPaths.COMMANDS_TIME_NIGHT) || save;
		save = check(locale, toText("&aYou have changed the time in the world &e" + Placeholders.WORLD + "&a."), null, LocalesPaths.COMMANDS_TIME_ADD) || save;
		save = check(locale, toText("&cYou have to hold the item you want to enchant in your hand."), null, LocalesPaths.COMMANDS_ENCHANT_ITEM_IS_NOT_PRESENT) || save;
		save = check(locale, toText("&aYou have added an enchantment to the item in your hand."), null, LocalesPaths.COMMANDS_ENCHANT_SUCCES) || save;
		save = check(locale, toText("&aYou opened the anvil inventory to player &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_ANVIL) || save;
		save = check(locale, toText("&aYou have opened the workbench inventory to the player &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_CRAFTING_TABLE) || save;
		save = check(locale, toText("&aYou have opened the enchantment table inventory to a player &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_ENCHANTMENT_TABLE) || save;
		save = check(locale, toText("&aYour food level is now maximum."), null, LocalesPaths.COMMANDS_FEED_SELF) || save;
		save = check(locale, toText("&aYou saturated the player &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_FEED_OTHER) || save;
		save = check(locale, toText("&aYour heals level is now maximum."), null, LocalesPaths.COMMANDS_HEAL_SELF) || save;
		save = check(locale, toText("&aYou healed player &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_HEAL_OTHER) || save;
		save = check(locale, toText("&9[&2Broadcast&9]&r "), null, LocalesPaths.COMMANDS_BROADCAST) || save;
		save = check(locale, toText("&cCommand not found."), null, LocalesPaths.COMMANDS_SUDO_COMMAND_NOT_FOUND) || save;
		save = check(locale, toText("&cYou cannot use this command, or the target player has no rights to execute it. Forcing execution is not available."), null, LocalesPaths.COMMANDS_SUDO_EXECUTE_NOT_ALLOWED) || save;
		save = check(locale, toText("&eAn attempt is made to forcibly execute a command by the player &6" + Placeholders.PLAYER + "&e."), null, LocalesPaths.COMMANDS_SUDO_SUCCESS) || save;
		save = check(locale, toText("&aYou are now invisible."), null, LocalesPaths.COMMANDS_VANISH_VASHISHED) || save;
		save = check(locale, toText("&aYou are visible again."), null, LocalesPaths.COMMANDS_VANISH_UNVASHISHED) || save;
		save = check(locale, toText("&aThe player &e" + Placeholders.PLAYER + "&a is now invisible."), null, LocalesPaths.COMMANDS_VANISH_VASHISHED_STAFF) || save;
		save = check(locale, toText("&aThe player &e" + Placeholders.PLAYER + "&a is visible again."), null, LocalesPaths.COMMANDS_VANISH_UNVASHISHED_STAFF) || save;
		save = check(locale, toText("&aYour nickname has been changed to &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_NICK_SET_SELF) || save;
		save = check(locale, toText("&aYour nickname has been changed to the original."), null, LocalesPaths.COMMANDS_NICK_CLEAR_SELF) || save;
		save = check(locale, toText("&aYou have changed the nickname of the player &e" + Placeholders.PLAYER + "&a to &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_NICK_SET_STAFF) || save;
		save = check(locale, toText("&aYou restored the original nickname of player &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_NICK_CLEAR_STAFF) || save;
		save = check(locale, toText("&aYou have established a new name for the item: &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_ITEM_SET_NAME) || save;
		save = check(locale, toText("&aYou have restored the original name of the item."), null, LocalesPaths.COMMANDS_ITEM_CLEAR_NAME) || save;
		save = check(locale, toText("&cYou have to hold the item in your hand."), null, LocalesPaths.COMMANDS_ITEM_EMPTY_HAND) || save;
		save = check(locale, toText("&aYou changed the lore of the item."), null, LocalesPaths.COMMANDS_ITEM_SET_LORE) || save;
		save = check(locale, toText("&aYou have cleared the item lore."), null, LocalesPaths.COMMANDS_ITEM_CLEAR_LORE) || save;
		save = check(locale, toText("&3&lServer info"), null, LocalesPaths.COMMANDS_SERVERSTAT_HEADER) || save;
		save = check(locale, toText("&7[&4System&7]"), null, LocalesPaths.COMMANDS_SERVERSTAT_BUTTON_SYSTEM) || save;
		save = check(locale, toText("&7[&eWorlds&7]"), null, LocalesPaths.COMMANDS_SERVERSTAT_BUTTON_WORLDS) || save;
		save = check(locale, toText("&7[&aPlugins&7]"), null, LocalesPaths.COMMANDS_SERVERSTAT_BUTTON_PLUGINS) || save;
		save = check(locale, toText("&7[&2Mods&7]"), null, LocalesPaths.COMMANDS_SERVERSTAT_BUTTON_MODS) || save;
		save = check(locale, toText("&aTPS(Current, 1m, 5m, 10m)&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_TPS) || save;
		save = check(locale, toText("&aUptime / JVM&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_UPTIME) || save;
		save = check(locale, toText("&aMax(JVM) RAM&f: &e" + Placeholders.VALUE + "Mb"), null, LocalesPaths.COMMANDS_SERVERSTAT_MEMORY_MAX) || save;
		save = check(locale, toText("&aAllocated RAM&f: &e" + Placeholders.VALUE + "Mb"), null, LocalesPaths.COMMANDS_SERVERSTAT_MEMORY_ALLOCATED) || save;
		save = check(locale, toText("&aUtilised RAM&f: &e" + Placeholders.VALUE + "Mb(&6"+ Placeholders.FROM_ALLOCATED + "%&e of used, &6" + Placeholders.FROM_MAX + "%&e of max)"), null, LocalesPaths.COMMANDS_SERVERSTAT_MEMORY_UTILISED) || save;
		save = check(locale, toText("&aFree (but allocated) memory&f: &e" + Placeholders.VALUE + "Mb"), null, LocalesPaths.COMMANDS_SERVERSTAT_MEMORY_FREE) || save;
		save = check(locale, toText("&aServer time&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_TIME) || save;
		save = check(locale, toText("&3&lSystem information"), null, LocalesPaths.COMMANDS_SERVERSTAT_OS_HEADER) || save;
		save = check(locale, toText("&aSystem&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_OS) || save;
		save = check(locale, toText("&aJava&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_JAVA) || save;
		save = check(locale, toText("&e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_JAVAHOME) || save;
		save = check(locale, toText("&3&lWorlds information"), null, LocalesPaths.COMMANDS_SERVERSTAT_WORLDS_INFO_HEADER) || save;
		save = check(locale, toText("&aWorld&f: &e" + Placeholders.WORLD + "&a. Chunks loaded&f: &e" + Placeholders.CHUNKS_SIZE + "&a. Entities&f: &e" + Placeholders.ENTITIES_SIZE + "&a. TPS&f: &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_SERVERSTAT_WORLDINFO) || save;
		save = check(locale, toText("&ePlugins &7(&b" + Placeholders.VALUE + "&7)&e"), null, LocalesPaths.COMMANDS_SERVERSTAT_HEADER_PLUGINS) || save;
		save = check(locale, toText("&eMods &7(&b" + Placeholders.VALUE + "&7)&e"), null, LocalesPaths.COMMANDS_SERVERSTAT_HEADER_MODS) || save;
		save = check(locale, toText("&7[&6Refresh&7]"), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_REFRESH_BUTTON) || save;
		save = check(locale, toText("&eAn attempt is made to reboot the plugin. If the plugin does not implement listening to the reboot event, there will be no effect."), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_REFRESH_MESSAGE) || save;
		save = check(locale, "MM.dd.yyyy HH:mm:ss", null, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT) || save;
		save = check(locale, toText("&3About plugin"), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_HEADER) || save;
		save = check(locale, toText("&eID&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_ID) || save;
		save = check(locale, toText("&eName&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_NAME) || save;
		save = check(locale, toText("&eVersion&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_VERSION) || save;
		save = check(locale, toText("&eEntrypoint&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_ENTRYPOINT) || save;
		save = check(locale, toText("&eDescription&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_DESCRIPTION) || save;
		save = check(locale, toText("&eDependencies&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_DEPENDENCIES) || save;
		save = check(locale, toText("&eContributors&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_CONTRIBUTORS) || save;
		save = check(locale, toText("&eLinks&f:\n    &eHome&f: &b" + Placeholders.HOME_LINK + "\n    &eSource&f: &b" + Placeholders.SOURCE_LINK + "\n    &eIssues&f: &b" + Placeholders.ISSUES_LINK), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_LINKS) || save;
		save = check(locale, toText("&3About mod"), null, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_HEADER) || save;
		save = check(locale, toText("&2ID&f: &2" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_ID) || save;
		save = check(locale, toText("&2Name&f: &2" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_NAME) || save;
		save = check(locale, toText("&2Version&f: &2" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_VERSION) || save;
		save = check(locale, toText("&2Description&f: &2" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_DESCRIPTION) || save;
		save = check(locale, toText("&2Dependencies&f: &2" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_DEPENDENCIES) || save;
		save = check(locale, toText("&2Links&f:\n    &2Issues&f: &b" + Placeholders.ISSUES_LINK + "\n    &2Updates&f: &b" + Placeholders.UPDATE_LINK), null, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_LINKS) || save;
		save = check(locale, toText("&cA kit with this id already exists."), null, LocalesPaths.COMMANDS_KITS_CREATE_EXIST) || save;
		save = check(locale, toText("&aA kit &e" + Placeholders.VALUE + "&a has been saved."), null, LocalesPaths.COMMANDS_KITS_SAVED) || save;
		save = check(locale, toText("&3Kits"), null, LocalesPaths.COMMANDS_KITS_LIST_HEADER) || save;
		save = check(locale, toText("&cThere are no kits created."), null, LocalesPaths.COMMANDS_KITS_NO_KITS) || save;
		save = check(locale, toText("&cThe time is not correct. Specify the duration in ISO time format."), null, LocalesPaths.COMMANDS_EXCEPTION_COOLDOWN_INCORRECT_TIME) || save;
		save = check(locale, toText("&aThe time to restore access to the kit &e" + Placeholders.VALUE + "&a is set."), null, LocalesPaths.COMMANDS_KITS_COOLDOWN_SUCCESS) || save;
		save = check(locale, toText("&aThe localized kit name is set."), null, LocalesPaths.COMMANDS_KITS_SET_NAME) || save;
		save = check(locale, toText("&aA rule for the issuance of a kit has been set."), null, LocalesPaths.COMMANDS_KITS_GIVE_RULE) || save;
		save = check(locale, toText("&aA description has been added to the kit for the default localization. To change it, edit the kit configuration file."), null, LocalesPaths.COMMANDS_KITS_CREATE_LORE) || save;
		save = check(locale, toText("&aThe limit for a player to get a kit has been set."), null, LocalesPaths.COMMANDS_KITS_GIVE_LIMIT) || save;
		save = check(locale, toText("&aNow the kit will be automatically issued to the player at one time."), null, LocalesPaths.COMMANDS_KITS_FIRST_TIME_ENABLE) || save;
		save = check(locale, toText("&aThe kit will no longer be automatically given to the player."), null, LocalesPaths.COMMANDS_KITS_FIRST_TIME_DISABLE) || save;
		save = check(locale, toText("&aThe server will now try to automatically give the player a kit when he login."), null, LocalesPaths.COMMANDS_KITS_GIVE_ON_JOIN_ENABLE) || save;
		save = check(locale, toText("&aThe kit will no longer be automatically given to the player."), null, LocalesPaths.COMMANDS_KITS_GIVE_ON_JOIN_DISABLE) || save;
		save = check(locale, toText("&aPermission is now required to obtain a kit."), null, LocalesPaths.COMMANDS_KITS_NEEDPERM_ENABLE) || save;
		save = check(locale, toText("&aPermission is no longer required to obtain a kit."), null, LocalesPaths.COMMANDS_KITS_NEEDPERM_DISABLE) || save;
		save = check(locale, toText("&aA command was added to the kit."), null, LocalesPaths.COMMANDS_KITS_ADD_COMMAND) || save;
		save = check(locale, toText("&cThe kit does not contain this command."), null, LocalesPaths.COMMANDS_KITS_COMMANDS_REMOVE_FAIL) || save;
		save = check(locale, toText("&aCommand deleted."), null, LocalesPaths.COMMANDS_KITS_COMMANDS_REMOVE_SUCCESS) || save;
		save = check(locale, toText("&aThe kit does not contain any commands."), null, LocalesPaths.COMMANDS_KITS_COMMANDS_EMPTY) || save;
		save = check(locale, toText("&3&lCommands"), null, LocalesPaths.COMMANDS_KITS_COMMANDS_HEADER) || save;
		save = check(locale, toText("&aA new price has been set to get a kit."), null, LocalesPaths.COMMANDS_KITS_SET_PRICE) || save;
		save = check(locale, toText("&3Kits"), null, LocalesPaths.COMMANDS_KIT_LIST_HEADER) || save;
		save = check(locale, toText("&7<&b&l⊙&7>&f⏊&7<&b&l⊙&7>"), null, LocalesPaths.COMMANDS_KIT_VIEW) || save;
		save = check(locale, toText("&cThere are no kits created."), null, LocalesPaths.COMMANDS_KIT_NO_KITS) || save;
		save = check(locale, toText("&cYou don't have permission to get this kit."), null, LocalesPaths.COMMANDS_KIT_NO_PERM) || save;
		save = check(locale, toText("&cYou will have to wait &e" + Placeholders.VALUE + "&c before you can get this kit."), null, LocalesPaths.COMMANDS_KIT_WAIT) || save;
		save = check(locale, toText("&eThere is not enough free space in your inventory. Are you sure you want this kit? Some of the items will fall to the ground. Click on this message to confirm."), null, LocalesPaths.COMMANDS_KIT_INVENTORY_FULL) || save;
		save = check(locale, toText("&cYou have reached the limit for getting this kit."), null, LocalesPaths.COMMANDS_KIT_GIVE_LIMIT) || save;
		save = check(locale, toText("&cYou don't have enough money to buy this kit. You need at least  &e" + Placeholders.VALUE + "&c."), null, LocalesPaths.COMMANDS_KIT_NO_MONEY) || save;
		save = check(locale, toText("&aYou received a kit &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_KIT_SUCCESS) || save;
		save = check(locale, toText("&aYou gave a kit &e" + Placeholders.VALUE + "&a to the player &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_KIT_SUCCESS_STAFF) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + " &fis now afk."), null, LocalesPaths.COMMANDS_AFK_ENABLE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + " &fis no longer afk."), null, LocalesPaths.COMMANDS_AFK_DISABLE) || save;
		save = check(locale, toText("&4&lAFK"), null, LocalesPaths.COMMANDS_AFK_TITLE) || save;
		save = check(locale, toText("&eYou will be kicked in " + Placeholders.VALUE + "&e."), null, LocalesPaths.COMMANDS_AFK_SUBTITLE) || save;
		save = check(locale, toText("&eYou have gone AFK, but you are currently vanished, so this has not been broadcasted."), null, LocalesPaths.COMMANDS_AFK_ENABLE_IN_VANISH) || save;
		save = check(locale, toText("&eYou have returned from AFK, but you are currently vanished, so this has not been broadcasted."), null, LocalesPaths.COMMANDS_AFK_DISABLE_IN_VANISH) || save;
		save = check(locale, toText("&eYou have been kicked for being AFK for too long."), null, LocalesPaths.COMMANDS_AFK_KICK) || save;
		save = check(locale, toText("&aThe world &e\"" + Placeholders.WORLD + "\"&a is created.\n&eWorlds are created in asynchronous mode. Without server restart the world will be static."), null, LocalesPaths.COMMANDS_WORLD_CREATE) || save;
		save = check(locale, toText("&aYou teleported into the world &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_TELEPORT) || save;
		save = check(locale, toText("&aYou teleported the player &e" + Placeholders.PLAYER + "&a to the world &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_TELEPORT_OTHER) || save;
		save = check(locale, toText("&aThe world &e\"" + Placeholders.WORLD + "\"&a is deleted."), null, LocalesPaths.COMMANDS_WORLD_DELETE) || save;
		save = check(locale, toText("&aThe world &e\"" + Placeholders.WORLD + "\"&a is unloaded."), null, LocalesPaths.COMMANDS_WORLD_UNLOAD) || save;
		save = check(locale, toText("&cThe world &e\"" + Placeholders.WORLD + "\"&c is not loaded."), null, LocalesPaths.COMMANDS_WORLD_UNLOADED) || save;
		save = check(locale, toText("&aThe world &e\"" + Placeholders.WORLD + "\"&a is loaded."), null, LocalesPaths.COMMANDS_WORLD_LOAD) || save;
		save = check(locale, toText("&cThe world &e\"" + Placeholders.WORLD + "\"&c is already loaded."), null, LocalesPaths.COMMANDS_WORLD_LOADED) || save;
		save = check(locale, toText("&aSpawn point is set in the world &e\"" + Placeholders.WORLD + "\"&a at the coordinates &e" + Placeholders.LOCATION + "&a."), null, LocalesPaths.COMMANDS_WORLD_SETSPAWN) || save;
		save = check(locale, toText("&aThe diameter of world &e\"" + Placeholders.WORLD + "\"&a border is set to the value &e" + Placeholders.VALUE + "&a with the center at the coordinates &e" + Placeholders.LOCATION + "&a."), null, LocalesPaths.COMMANDS_WORLD_SETBORDER) || save;
		save = check(locale, toText("&aThe world &e\"" + Placeholders.WORLD + "\"&a will be loaded when the server starts up."), null, LocalesPaths.COMMANDS_WORLD_ENABLE) || save;
		save = check(locale, toText("&aThe world &e\"" + Placeholders.WORLD + "\"&a will not load when the server starts."), null, LocalesPaths.COMMANDS_WORLD_DISABLE) || save;
		save = check(locale, toText("&aPvP enabled in the world &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_ENABLE_PVP) || save;
		save = check(locale, toText("&aPvP disabled in the world &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_DISABLE_PVP) || save;
		save = check(locale, toText("&aA peaceful difficulty in the world of&e\"" + Placeholders.WORLD + "\"&a has been set."), null, LocalesPaths.COMMANDS_WORLD_DIFFICULTY_PEACEFUL) || save;
		save = check(locale, toText("&aThe low difficulty in the world &e\"" + Placeholders.WORLD + "\"&a has been set."), null, LocalesPaths.COMMANDS_WORLD_DIFFICULTY_EASY) || save;
		save = check(locale, toText("&aThe normal difficulty in the world &e\"" + Placeholders.WORLD + "\"&a has been set."), null, LocalesPaths.COMMANDS_WORLD_DIFFICULTY_NORMAL) || save;
		save = check(locale, toText("&aThe hard difficulty in the world &e\"" + Placeholders.WORLD + "\"&a has been set."), null, LocalesPaths.COMMANDS_WORLD_DIFFICULTY_HARD) || save;
		save = check(locale, toText("&aSurvival mode is set in world &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_GAMEMODE_SURVIVAL) || save;
		save = check(locale, toText("&aThe creative mode is set in world &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_GAMEMODE_CREATIVE) || save;
		save = check(locale, toText("&aThe adventure mode is set in world &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_GAMEMODE_ADVENTURE) || save;
		save = check(locale, toText("&aSpectator mode is set to world &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_GAMEMODE_SPECTATOR) || save;
		save = check(locale, toText("&aThe view distance in world &e\"" + Placeholders.WORLD + "\"&a is set to &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_WORLD_VIEWDISTANCE) || save;
		save = check(locale, toText("&aGame rule &e" + Placeholders.RULE + "&a in world &e\"" + Placeholders.WORLD + "\"&a is set to &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_WORLD_GAMERULE_SUCCESS) || save;
		save = check(locale, toText("&6Game rules&f: &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_WORLD_GAMERULES) || save;
		save = check(locale, toText("&eInvalid value specified for game rule &e" + Placeholders.RULE + "&a."), null, LocalesPaths.COMMANDS_WORLD_GAMERULE_INCORECT_VALUE) || save;
		save = check(locale, toText("&cFailed to determine the type of game rule &e" + Placeholders.RULE + "&c. No changes have been made."), null, LocalesPaths.COMMANDS_WORLD_GAMERULE_UNKNOWN_TYPE) || save;
		save = check(locale, "The generation of chunks in world \"" + Placeholders.WORLD + "\" in progress. Done: " + Placeholders.VALUE + "%. The last generated chunk: " + Placeholders.LOCATION + ".", null, LocalesPaths.COMMANDS_WORLD_DEBUG_GENERATE) || save;
		save = check(locale, toText("&cThe task of generating chunks in world &e\"" + Placeholders.WORLD + "\"&c is missing."), null, LocalesPaths.COMMANDS_WORLD_NOT_STARTED_GENERATE) || save;
		save = check(locale, toText("&cYou cannot delete an active generation task. Put it on pause first."), null, LocalesPaths.COMMANDS_WORLD_NOT_PAUSED_GENERATE) || save;
		save = check(locale, toText("&aChunk generation in world &e\"" + Placeholders.WORLD + "\" &ahas started.\n If you stop the server, when you start this operation again, generation will start from the beginning."), null, LocalesPaths.COMMANDS_WORLD_START_GENERATE) || save;
		save = check(locale, toText("&aChunk generation in world &e\"" + Placeholders.WORLD + "\"&a is suspended."), null, LocalesPaths.COMMANDS_WORLD_PAUSE_GENERATE) || save;
		save = check(locale, toText("&aThe generation of chunks in world&e\"" + Placeholders.WORLD + "\"&a has been stopped. The task is deleted."), null, LocalesPaths.COMMANDS_WORLD_STOP_GENERATE) || save;
		save = check(locale, toText("&aEnabled the logic of spawn in the world &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_SPAWN_LOGIC_ENABLE) || save;
		save = check(locale, toText("&aDisabled the logic of spawn in the world &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_SPAWN_LOGIC_DISABLE) || save;
		save = check(locale, toText("&aNow you will see what commands other players are using."), null, LocalesPaths.COMMANDS_COMMANDSPY_ENABLE) || save;
		save = check(locale, toText("&aYou will no longer see what commands other players are using."), null, LocalesPaths.COMMANDS_COMMANDSPY_DISABLE) || save;
		save = check(locale, toText("&8[&bCommand&9Spy&8] &e" + Placeholders.PLAYER + "&7 uses the command&f: &d" + Placeholders.COMMAND), null, LocalesPaths.COMMANDS_COMMANDSPY_SPY) || save;
		save = check(locale, toText("&aPing&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_PING_SUCCESS) || save;
		save = check(locale, toText("&aPing of player &e" + Placeholders.PLAYER + "&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_PING_SUCCESS_STAFF) || save;
		save = check(locale, toText("&2&lInventory&f&l: &e&l" + Placeholders.PLAYER), null, LocalesPaths.COMMANDS_INVSEE_TITLE) || save;
		save = check(locale, toText("&aOnline(" + Placeholders.VALUE + ")&f: &e"), null, LocalesPaths.COMMANDS_LIST_SUCCESS) || save;
		save = check(locale, toText("&7" + Placeholders.PLAYER), null, LocalesPaths.COMMANDS_LIST_VANISHED) || save;
		save = check(locale, toText("&3Player Info&f: " + Placeholders.PLAYER), null, LocalesPaths.COMMANDS_SEEN_TITLE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a is currently &2online"), null, LocalesPaths.COMMANDS_SEEN_ONLINE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a is currently &4offline"), null, LocalesPaths.COMMANDS_SEEN_OFFLINE) || save;
		save = check(locale, toText("&aOnline for&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_ONLINE_TIME) || save;
		save = check(locale, toText("&aLast online&f: &e" + Placeholders.VALUE + "&a ago"), null, LocalesPaths.COMMANDS_SEEN_LAST_ONLINE) || save;
		save = check(locale, toText("&aDisplay name&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_DISPLAY_NAME) || save;
		save = check(locale, toText("&aUUID&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_UUID) || save;
		save = check(locale, toText("&aIP&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_IP) || save;
		save = check(locale, toText("&aFirst played&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_FIRST_PLAYED) || save;
		save = check(locale, toText("&aWalking speed&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_WALKING_SPEED) || save;
		save = check(locale, toText("&aFlying speed&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_FLYING_SPEED) || save;
		save = check(locale, toText("&aCurrent location&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_CURRENT_LOCATION) || save;
		save = check(locale, toText("&aCan fly&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_CAN_FLY) || save;
		save = check(locale, toText("&aIs flying&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_FLYING) || save;
		save = check(locale, toText("&aGamemode&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_GAMEMODE) || save;
		save = check(locale, toText("&aVanished&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_VANISHED) || save;
		save = check(locale, toText("&aInvulnerable&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_GODMODE) || save;
		save = check(locale, toText("&aAFK&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_AFK) || save;
		save = check(locale, toText("&aBan&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_BAN) || save;
		save = check(locale, toText("&aMute&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_MUTE) || save;
		save = check(locale, toText("&aWarns&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_WARNS) || save;
		save = check(locale, toText("&eYes"), null, LocalesPaths.COMMANDS_SEEN_YES) || save;
		save = check(locale, toText("&eNo"), null, LocalesPaths.COMMANDS_SEEN_NO) || save;
		save = check(locale, toText("&3="), null, LocalesPaths.COMMANDS_SEEN_PADDING) || save;
		save = check(locale, toText("&3Help"), null, LocalesPaths.COMMANDS_HELP_TITLE) || save;
		save = checkList(locale, Arrays.asList(
				toText("&eThe commands will be available if you have permission for them."),
				toText("&2/spawn &f- &eteleport to a spawnpoint"),
				toText("&2/home &f- &eteleporting to your home")
				), null, LocalesPaths.COMMANDS_HELP_LIST) || save;
		save = check(locale, toText("&aNow you're glowing."), null, LocalesPaths.COMMANDS_GLOW_ENABLE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a is now glowing."), null, LocalesPaths.COMMANDS_GLOW_ENABLE_STAFF) || save;
		save = check(locale, toText("&aYou don't glow anymore."), null, LocalesPaths.COMMANDS_GLOW_DISABLE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a is no longer glowing."), null, LocalesPaths.COMMANDS_GLOW_DISABLE_STAFF) || save;
		save = check(locale, toText("&aYou are burning."), null, LocalesPaths.COMMANDS_FLAME_SUCCESS) || save;
		save = check(locale, toText("&4You are burning!"), null, LocalesPaths.COMMANDS_FLAME_SUCCESS_DAMAGE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a is now burning."), null, LocalesPaths.COMMANDS_FLAME_SUCCESS_STAFF) || save;
		save = check(locale, toText("&aFire extinguished."), null, LocalesPaths.COMMANDS_EXTINGUISH_SUCCESS) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a is extinguished."), null, LocalesPaths.COMMANDS_EXTINGUISH_SUCCESS_STAFF) || save;
		save = check(locale, toText("&aYou've banned &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_BAN_SUCCESS) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a bans player &e" + Placeholders.PLAYER + "&a.\n&aEnd of ban &e" + Placeholders.TIME + "\n&aReason: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_BAN_ANNOUNCEMENT) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a permanently bans player &e" + Placeholders.PLAYER + "&a.\n&aReason: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_BAN_ANNOUNCEMENT_PERMANENT) || save;
		save = check(locale, toText("&cIt's impossible to ban &e" + Placeholders.PLAYER + "&c."), null, LocalesPaths.COMMANDS_BAN_IGNORE) || save;
		save = check(locale, toText("&ePlayer &e" + Placeholders.PLAYER + "&e has already banned on this server."), null, LocalesPaths.COMMANDS_BAN_ALREADY_BANNED) || save;
		save = check(locale, toText("&bYou are banned on the server.\n&bBy &e" + Placeholders.SOURCE + "\n&bReason: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_BAN_DISCONNECT) || save;
		save = check(locale, toText("&aYou unbanned &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_UNBAN_SUCCESS) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a unbans &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_UNBAN_ANNOUNCEMENT) || save;
		save = check(locale, toText("&aYou unbanned &e" + Placeholders.PLAYER + "&a and his IP &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_BANIP_SUCCESS) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a bans player &e" + Placeholders.PLAYER + "&a by IP.\n&aEnd of ban &e" + Placeholders.TIME + "&a.\n&aReason: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_BANIP_ANNOUNCEMENT) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a permanently bans player &e" + Placeholders.PLAYER + "&a by IP.\n&aReason: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_BANIP_ANNOUNCEMENT_PERMANENT) || save;
		save = check(locale, toText("&bYour IP is banned on the server.\n&bBy &e" + Placeholders.SOURCE + "\n&bReason: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_BANIP_DISCONNECT) || save;
		save = check(locale, toText("&aYou unbanned IP &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_UNBANIP_SUCCESS) || save;
		save = check(locale, toText("&aYou kicked &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_KICK_SUCCESS) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&b kicked you from the server.\n&bReason: &e" + Placeholders.VALUE + "."), null, LocalesPaths.COMMANDS_KICK_DISCONNECT) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a kicks out from the server  " + Placeholders.PLAYER + ".\n&aReason: &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_KICK_ANNOUNCEMENT) || save;
		save = check(locale, toText("&cYou can't kick &e" + Placeholders.PLAYER + "&c."), null, LocalesPaths.COMMANDS_KICK_SUCCESS) || save;
		save = check(locale, toText("&aYou blocked the chat to the player &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_MUTE_SUCCESS) || save;
		save = check(locale, toText("&cYou are blocked from writing in chat blocked until &e" + Placeholders.TIME + "&c.\nReason: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_MUTE_SUCCESS_TARGET) || save;
		save = check(locale, toText("&cYou are permanently blocked from writing in the chat.\nReason: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_MUTE_SUCCESS_TARGET_PERMANENT) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a mute player &e" + Placeholders.PLAYER + "&a.\nEnd of mute &e" + Placeholders.TIME + "&a.\n&aReason: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_MUTE_ANNOUNCEMENT) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a permanently mute player &e" + Placeholders.PLAYER + "&a.\n&aReason: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_MUTE_ANNOUNCEMENT_PERMANENT) || save;
		save = check(locale, toText("&cUnable to block chat for player &e" + Placeholders.PLAYER + "&c."), null, LocalesPaths.COMMANDS_MUTE_IGNORE) || save;
		save = check(locale, toText("&ePlayer &e" + Placeholders.PLAYER + "&e already cannot write in chat."), null, LocalesPaths.COMMANDS_MUTE_ALREADY_MUTED) || save;
		save = check(locale, toText("&aYou unmuted &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_UNMUTE_SUCCESS) || save;
		save = check(locale, toText("&aYou can write in the chat again."), null, LocalesPaths.COMMANDS_UNMUTE_SUCCESS_TARGET) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a unmute &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_UNMUTE_ANNOUNCEMENT) || save;
		save = check(locale, toText("&aYou warned the player &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_WARN_SUCCESS) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a warned you.\nReason: &e\"" + Placeholders.VALUE + "&e\"&a.\nValid until &e" + Placeholders.TIME), null, LocalesPaths.COMMANDS_WARN_SUCCESS_TARGET) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a has issued a permanent warning to you.\nReason:  &e\"" + Placeholders.VALUE + "&e\"&a."), null, LocalesPaths.COMMANDS_WARN_SUCCESS_TARGET_PERMANENT) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a has warned player &e" + Placeholders.PLAYER + "&a.\nValid until &e" + Placeholders.TIME + "&a.\n&aReason: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_WARN_ANNOUNCEMENT) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a issues a permanent warning to player &e" + Placeholders.PLAYER + "&a.\n&aReason: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_WARN_ANNOUNCEMENT_PERMANENT) || save;
		save = check(locale, toText("&cIt is not possible to give a warning to &e" + Placeholders.PLAYER + "&c."), null, LocalesPaths.COMMANDS_WARN_IGNORE) || save;
		save = check(locale, toText("&4Automatic issuance of punishments"), null, LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH) || save;
		save = check(locale, toText("&cWarning limit reached before ban &e" + Placeholders.VALUE + "&c"), null, LocalesPaths.COMMANDS_WARN_BAN_LIMIT) || save;
		save = check(locale, toText("&cWarning limit reached before mute &e" + Placeholders.VALUE + "&c"), null, LocalesPaths.COMMANDS_WARN_MUTE_LIMIT) || save;
		save = check(locale, toText("&cWarning limit reached before kick &e" + Placeholders.VALUE + "&c"), null, LocalesPaths.COMMANDS_WARN_KICK_LIMIT) || save;
		save = check(locale, toText("&aWarnings in all time &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_WARNS_ALLTIME) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a warnings in all time &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_WARNS_ALLTIME_TARGET) || save;
		save = check(locale, toText("&3" + Placeholders.PLAYER + " warnings"), null, LocalesPaths.COMMANDS_WARNS_TITLE) || save;
		save = check(locale, toText("&aReason: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_WARNS_REASON) || save;
		save = check(locale, toText("&e" + Placeholders.TIME + " &a➢ &e" + Placeholders.LIMIT), null, LocalesPaths.COMMANDS_WARNS_TIMES) || save;
		save = check(locale, toText("&aThere are no bans to display."), null, LocalesPaths.COMMANDS_BANLIST_EMPTY) || save;
		save = check(locale, toText("&3%profile% &3|| %ip%"), null, LocalesPaths.COMMANDS_BANLIST_TITLE) || save;
		save = check(locale, toText("&7[&ePlayers&7]"), null, LocalesPaths.COMMANDS_BANLIST_PROFILE) || save;
		save = check(locale, toText("&7[&eIP&7]"), null, LocalesPaths.COMMANDS_BANLIST_IP) || save;
		save = check(locale, toText("&e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_BANLIST_ELEMENT) || save;
		save = check(locale, toText("&aPlayer&f: &e" + Placeholders.PLAYER + "\n&aSource&f: &e" + Placeholders.SOURCE + "\n&aCreated&f: &e" + Placeholders.CREATED + "\n&aExpire&f: &e" + Placeholders.EXPIRE + "\n&aReason&f: &e" + Placeholders.REASON), null, LocalesPaths.COMMANDS_BANLIST_BANINFO_PLAYER) || save;
		save = check(locale, toText("&aIP&f: &e" + Placeholders.VALUE + "\n&aSource&f: &e" + Placeholders.SOURCE + "\n&aCreated&f: &e" + Placeholders.CREATED + "\n&aExpire&f: &e" + Placeholders.EXPIRE + "\n&aReason&f: &e" + Placeholders.REASON), null, LocalesPaths.COMMANDS_BANLIST_BANINFO_IP) || save;
		save = check(locale, toText("&cPermanent"), null, LocalesPaths.COMMANDS_BANINFO_PERMANENT) || save;
		save = check(locale, toText("&cYou need to specify the nickname of the banned player."), null, LocalesPaths.COMMANDS_BANINFO_NOT_PRESENT) || save;
		save = check(locale, toText("&aSource&f: &e" + Placeholders.SOURCE + "\n&aCreated&f: &e" + Placeholders.CREATED + "\n&aExpire&f: &e" + Placeholders.EXPIRE + "\n&aReason&f: &e" + Placeholders.REASON), null, LocalesPaths.COMMANDS_BANINFO_SUCCESS) || save;
		save = check(locale, toText("&3Ban Info " + Placeholders.PLAYER), null, LocalesPaths.COMMANDS_BANINFO_TITLE) || save;
		save = check(locale, toText("&cPermanent"), null, LocalesPaths.COMMANDS_MUTEINFO_PERMANENT) || save;
		save = check(locale, toText("&cYou need to specify the nickname of the player with blocked chat."), null, LocalesPaths.COMMANDS_MUTEINFO_NOT_PRESENT) || save;
		save = check(locale, toText("&aSource&f: &e" + Placeholders.SOURCE + "\n&aCreated&f: &e" + Placeholders.CREATED + "\n&aExpire&f: &e" + Placeholders.EXPIRE + "\n&aReason&f: &e" + Placeholders.REASON), null, LocalesPaths.COMMANDS_MUTEINFO_SUCCESS) || save;
		save = check(locale, toText("&3Mute Info " + Placeholders.PLAYER), null, LocalesPaths.COMMANDS_MUTEINFO_TITLE) || save;
		save = check(locale, toText("&cThere are no mutes to display."), null, LocalesPaths.COMMANDS_MUTELIST_EMPTY) || save;
		save = check(locale, toText("&3Mutes (" + Placeholders.VALUE + ")"), null, LocalesPaths.COMMANDS_MUTELIST_TITLE) || save;
		save = check(locale, toText("&e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_MUTELIST_ELEMENT) || save;
		save = check(locale, toText("&aPlayer&f: &e" + Placeholders.PLAYER + "\n&aSource&f: &e" + Placeholders.SOURCE + "\n&aCreated&f: &e" + Placeholders.CREATED + "\n&aExpire&f: &e" + Placeholders.EXPIRE + "\n&aReason&f: &e" + Placeholders.REASON), null, LocalesPaths.COMMANDS_MUTELIST_INFO) || save;

		if(save) save(locale);
	}

	private void generateRu() {
		Locale locale = org.spongepowered.api.util.locale.Locales.RU_RU;

		boolean save = check(locale, toText("Не найден плагин экономики. Часть функций может быть не доступна."), null, LocalesPaths.ECONOMY_NOT_FOUND);
		save = check(locale, toText("Не удалось начислить игровую валюту игроку " + Placeholders.PLAYER + "."), null, LocalesPaths.ECONOMY_ERROR_GIVE_MONEY) || save;
		save = check(locale, toText("Не удалось списать игровую валюту у игрока " + Placeholders.PLAYER + "."), null, LocalesPaths.ECONOMY_ERROR_TAKE_MONEY) || save;

		save = check(locale, toText("д"), null, LocalesPaths.TIME_DAYS) || save;
		save = check(locale, toText("ч"), null, LocalesPaths.TIME_HOUR) || save;
		save = check(locale, toText("м"), null, LocalesPaths.TIME_MINUTE) || save;
		save = check(locale, toText("с"), null, LocalesPaths.TIME_SECOND) || save;

		save = check(locale, toText("&7[&cУдалить&7] "), null, LocalesPaths.REMOVE) || save;
		save = check(locale, toText("&7[&aТелепорт&7] "), null, LocalesPaths.TELEPORT) || save;
		save = check(locale, toText("&7[&eТелепорт&7] "), null, LocalesPaths.TELEPORTCLICKABLE) || save;
		save = checkList(locale, Arrays.asList(
				toText("&b================================"),
				toText("&dПриветствуем &e" + Placeholders.PLAYER + "&d. Добро пожаловать на сервер."),
				toText("&dНадеемся тебе у нас понравится."),
				toText("&b================================")
				), null, LocalesPaths.MOTD) || save;
		save = check(locale, toText("&7[&a+&7]&r " + Placeholders.PREFIX + " " + Placeholders.PLAYER + " " + Placeholders.SUFFIX), null, LocalesPaths.JOIN_MESSAGE) || save;
		save = check(locale, toText("&7[&2+&7]&r " + Placeholders.PREFIX + " " + Placeholders.PLAYER + " " + Placeholders.SUFFIX), null, LocalesPaths.FIRST_JOIN_MESSAGE) || save;
		save = check(locale, toText("&7[&c-&7]&r " + Placeholders.PREFIX + " " + Placeholders.PLAYER + " " + Placeholders.SUFFIX), null, LocalesPaths.LEAVE_MESSAGE) || save;
		save = check(locale, toText("&aВаш инвентарь будет сохранен. Шанс сохранения каждого предмета равен &e" + Placeholders.VALUE + "%&a. Вещи будут вам возвращены после респавна."), null, LocalesPaths.KEEP_INVENTORY) || save;
		save = check(locale, toText("&aВаш опыт будет сохранен в объеме &e" + Placeholders.VALUE + "%&a. Опыт будет вам возвращен после респавна."), null, LocalesPaths.KEEP_EXP) || save;
		save = check(locale, "Игрок " + Placeholders.PLAYER + " входит на сервер с модами: " + Placeholders.VALUE + ".", null, LocalesPaths.PLAYER_MODS_LIST) || save;
		save = check(locale, toText("&cНа сервере установлен запрет на использование некоторых из имеющихся у вас модов&f:\n&c" + Placeholders.VALUE + "."), null, LocalesPaths.ILLEGAL_MODS_LIST) || save;

		save = check(locale, toText("Сервер"), null, LocalesPaths.NAME_SYSTEM) || save;
		save = check(locale, toText("Командный блок"), null, LocalesPaths.NAME_COMMANDBLOCK) || save;
		save = check(locale, toText("Вагонетка с командным блоком"), null, LocalesPaths.NAME_COMMANDBLOCK_MINECART) || save;
		save = check(locale, toText("Неизвестный"), null, LocalesPaths.NAME_UNKNOWN) || save;
		save = check(locale, toText("&6&lРюкзак"), null, LocalesPaths.NAME_BACKPACK) || save;

		save = check(locale, toText("&cИгрок " + Placeholders.PLAYER + "&c оффлайн."), null, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_IS_OFFLINE) || save;
		save = check(locale, toText("&cНужно указать ник игрока, который сейчас онлайн или ранее был на сервере."), null, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT) || save;
		save = check(locale, toText("&cНужно указать ник игрока онлайн."), null, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT) || save;
		save = check(locale, toText("&cНельзя указывать на себя."), null, LocalesPaths.COMMANDS_EXCEPTION_TARGET_SELF) || save;
		save = check(locale, toText("&cНужно указать имя."), null, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT) || save;
		save = check(locale, toText("&cНужно указать тип."), null, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT) || save;
		save = check(locale, toText("&cНужно указать значение."), null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT) || save;
		save = check(locale, toText("&cНужно указать время в формате ISO."), null, LocalesPaths.COMMANDS_EXCEPTION_DURATION_NOT_PRESENT) || save;
		save = check(locale, toText("&cНужно указать аргумент команды вида boolean. Подробнее об аргументах введенной команды можно узнать в документации плагина."), null, LocalesPaths.COMMANDS_EXCEPTION_BOOLEAN_NOT_PRESENT) || save;
		save = check(locale, toText("&cНужно указать координаты."), null, LocalesPaths.COMMANDS_EXCEPTION_LOCATION_NOT_PRESENT) || save;
		save = check(locale, toText("&cНужно указать мир."), null, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT) || save;
		save = check(locale, toText("&cПодождите &e" + Placeholders.DELAY + "&c прежде чем снова использовать эту команду."), null, LocalesPaths.COMMANDS_COOLDOWN) || save;
		save = check(locale, toText("&aАктивация команды через &e" + Placeholders.DELAY + "&a."), null, LocalesPaths.COMMANDS_WAIT) || save;
		save = check(locale, toText(Placeholders.SOURCE + " выполняет команду: /" + Placeholders.COMMAND + Placeholders.ARGS + ""), null, LocalesPaths.COMMANDS_LOG) || save;
		save = check(locale, toText("Команда \"" + Placeholders.COMMAND + "\" не зарегистрированна на отслеживание."), null, LocalesPaths.COMMANDS_NOT_TRACKING) || save;
		save = check(locale, toText("&cОстановленно выполнение команды &e\"&5" + Placeholders.COMMAND + "&e\"&c так как вы ввели другую команду."), null, LocalesPaths.COMMANDS_STOP_TRACKING_COMMAND) || save;
		save = check(locale, toText("&cОстановленно выполнение команды &e\"&5" + Placeholders.COMMAND + "&e\"&c так как вы сдвинулись с места."), null, LocalesPaths.COMMANDS_STOP_TRACKING_MOVING) || save;
		save = check(locale, toText("&eСписано денег &2" + Placeholders.MONEY + "&e за выполнение команды &e\"&5" + Placeholders.COMMAND + "&e\"&e."), null, LocalesPaths.COMMANDS_TAKE_MONEY) || save;
		save = check(locale, toText("&cВам необходимо иметь на балансе &2" + Placeholders.MONEY + "&c для выполнения команды &e\"&5" + Placeholders.COMMAND + "&e\"&c."), null, LocalesPaths.COMMANDS_ERROR_TAKE_MONEY) || save;
		save = check(locale, toText("&cВы должны держать предмет в основной руке."), null, LocalesPaths.COMMANDS_HAT_NO_ITEM) || save;
		save = check(locale, toText("&cНельзя надеть на голову данный предмет."), null, LocalesPaths.COMMANDS_HAT_BLACKLIST_ITEM) || save;
		save = check(locale, toText("&cИнвентарь игрока &e" + Placeholders.PLAYER + "&c заполнен. Кликните на это сообщение, если хотите заменить предмет на голове игрока. Предмет на голове игрока будет потерян!"), null, LocalesPaths.COMMANDS_HAT_BLACKLIST_ITEM) || save;
		save = check(locale, toText("&aВы надели предмет на голову игроку " + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_HAT_SUCCESS_OTHER) || save;
		save = check(locale, toText("&aТочка спавна установленна."), null, LocalesPaths.COMMANDS_SETSPAWN_SUCCESS) || save;
		save = check(locale, toText("&aВы переместились на спавн."), null, LocalesPaths.COMMANDS_SPAWN_SUCCESS) || save;
		save = check(locale, toText("&cВаш лимит не позволяет установить новую точку дома."), null, LocalesPaths.COMMANDS_SETHOME_LIMIT) || save;
		save = check(locale, toText("&aВы установили точку дома &e" + Placeholders.HOME + "&a."), null, LocalesPaths.COMMANDS_SETHOME_SUCCESS) || save;
		save = check(locale, toText("&cТочка дома с именем &e" + Placeholders.HOME + "&c не существует."), null, LocalesPaths.COMMANDS_HOME_NOT_FOUND) || save;
		save = check(locale, toText("&cНе найденно ни одной точки дома. Используйте команду &e/sethome&c для установки точки дома."), null, LocalesPaths.COMMANDS_HOME_NOT_SET) || save;
		save = check(locale, toText("&cТочка дома не доступна. Возможно не загружен мир."), null, LocalesPaths.COMMANDS_HOME_TELEPORT_ERROR) || save;
		save = check(locale, toText("&aВы переместились точку дома &e" + Placeholders.HOME + "&a."), null, LocalesPaths.COMMANDS_HOME_SUCCESS) || save;
		save = check(locale, toText("&bДома"), null, LocalesPaths.COMMANDS_HOME_LIST) || save;
		save = check(locale, toText("&cВарп с указанным именем не найден."), null, LocalesPaths.COMMANDS_WARP_NOT_FOUND) || save;
		save = check(locale, toText("&aВы переместились на варп " + Placeholders.WARP + "&a."), null, LocalesPaths.COMMANDS_WARP_SUCCESS) || save;
		save = check(locale, toText("&aВас переместили на варп " + Placeholders.WARP + "&a."), null, LocalesPaths.COMMANDS_WARP_SUCCESS_OTHER) || save;
		save = check(locale, toText("&aВы переместили &e" + Placeholders.PLAYER + " на варп " + Placeholders.WARP + "&a."), null, LocalesPaths.COMMANDS_WARP_SUCCESS_STAFF) || save;
		save = check(locale, toText("&cВарп с указанным именем уже существует."), null, LocalesPaths.COMMANDS_SETWARP_EXIST) || save;
		save = check(locale, toText("&cВаш лимит не позволяет установить новую варп точку."), null, LocalesPaths.COMMANDS_SETWARP_LIMIT) || save;
		save = check(locale, toText("&aВарп установлен."), null, LocalesPaths.COMMANDS_SETWARP_SUCCESS) || save;
		save = check(locale, toText("&aАдминистративный варп установлен."), null, LocalesPaths.COMMANDS_SETWARP_SUCCESS_ADMIN) || save;
		save = check(locale, toText("&cСписок варпов пуст."), null, LocalesPaths.COMMANDS_WARPS_EMPTY) || save;
		save = check(locale, toText("&eВарпы: "), null, LocalesPaths.COMMANDS_WARPS_LIST) || save;
		save = check(locale, toText("&eСписок варпов составляется. Пожалуйста подождите."), null, LocalesPaths.COMMANDS_WARPS_WAIT) || save;
		save = check(locale, toText("&bВарпы: ============ " + Placeholders.SERVER + " | " + Placeholders.PLAYER + " ================"), null, LocalesPaths.COMMANDS_WARPS_HEADER) || save;
		save = check(locale, toText("&7[&4Сервер&7]"), null, LocalesPaths.COMMANDS_WARPS_SERVER_GROUP) || save;
		save = check(locale, toText("&7[&eИгроки&7]"), null, LocalesPaths.COMMANDS_WARPS_PLAYERS_GROUP) || save;
		save = check(locale, toText("&aТеперь вы можете принимать запросы на телепортацию."), null, LocalesPaths.COMMANDS_TPTOGGLE_ENABLE) || save;
		save = check(locale, toText("&aВходящие запросы на телепортацию теперь будут автоматически отклоняться."), null, LocalesPaths.COMMANDS_TPTOGGLE_DISABLE) || save;
		save = check(locale, toText("&cДанный игрок не принимает запросы на телепортацию."), null, LocalesPaths.COMMANDS_TPA_DISABLE_TP_REQUESTS) || save;
		save = check(locale, toText("&cЗапрос не действителен. Игрок вышел из сети."), null, LocalesPaths.COMMANDS_TPA_SOURCE_OFFLINE) || save;
		save = check(locale, toText("&aВы отправили запрос на телепортацю."), null, LocalesPaths.COMMANDS_TPA_SUCCESS) || save;
		save = check(locale, toText("&aЗапрос на телепортацию принят."), null, LocalesPaths.COMMANDS_TPA_ACCEPTED) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a запрашивает разрешение на телепортацию к вам. Кликните на это сообщение для принятия запроса."), null, LocalesPaths.COMMANDS_TPA_REQUEST_MESSAGE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a просит вас телепортироваться к нему/ней. Кликните на это сообщение для принятия запроса."), null, LocalesPaths.COMMANDS_TPA_REQUEST_HERE_MESSAGE) || save;
		save = check(locale, toText("&cНе удалось найти позицию в мире '" + Placeholders.WORLD + "' для телепортации. Достигнут лимит попыток поиска: " + Placeholders.LIMIT + "."), null, LocalesPaths.COMMANDS_RANDOM_TELEPORT_POSITION_SEARCH_ERROR_STAFF) || save;
		save = check(locale, toText("&cНе удалось найти позицию для телепортации. Попробуйте еще раз."), null, LocalesPaths.COMMANDS_RANDOM_TELEPORT_POSITION_SEARCH_ERROR) || save;
		save = check(locale, toText("&aИгрок &e" + Placeholders.PLAYER + "&a телепортирован в локацию &e<" + Placeholders.WORLD + "> " + Placeholders.LOCATION + "&a."), null, LocalesPaths.COMMANDS_RANDOM_TELEPORT_SUCCES_STAFF) || save;
		save = check(locale, toText("&aВы телепортировались в локацию &e<" + Placeholders.WORLD + "> " + Placeholders.LOCATION + "&a."), null, LocalesPaths.COMMANDS_RANDOM_TELEPORT_SUCCES) || save;
		save = check(locale, toText("&eЗапущен поиск подходящей локации. Пожалуйста подождите."), null, LocalesPaths.COMMANDS_RANDOM_TELEPORT_WAIT) || save;
		save = check(locale, toText("&eТелепортация отменена. Попробуйте еще раз."), null, LocalesPaths.COMMANDS_RANDOM_TELEPORT_CANCELLED) || save;
		save = check(locale, toText("&cПредыдущая локация не найдена."), null, LocalesPaths.COMMANDS_BACK_EMPTY) || save;
		save = check(locale, toText("&cМир в котором располагается предыдущая локация не загружен."), null, LocalesPaths.COMMANDS_BACK_NOT_LOADED_WORLD) || save;
		save = check(locale, toText("&cУказана не допустимая локация."), null, LocalesPaths.COMMANDS_TPPOS_INVALID_LOCATION) || save;
		save = check(locale, toText("&aВы очистили свой инвентарь."), null, LocalesPaths.COMMANDS_CLEAR_SUCCES) || save;
		save = check(locale, toText("&eВаш инвентарь был очищен."), null, LocalesPaths.COMMANDS_CLEAR_SUCCES_OTHER) || save;
		save = check(locale, toText("&aВы очистили инвентарь игрока &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_CLEAR_SUCCES_STAFF) || save;
		save = check(locale, toText("&aРемонт выполнен."), null, LocalesPaths.COMMANDS_REPAIR_SUCCES) || save;
		save = check(locale, toText("&aВам восстановили прочность предмета(ов)."), null, LocalesPaths.COMMANDS_REPAIR_SUCCES_OTHER) || save;
		save = check(locale, toText("&aВы восстановили прочность предмета(ов) игроку &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_REPAIR_SUCCES_STAFF) || save;
		save = check(locale, toText("&cНе удалось определить позицию для прыжка."), null, LocalesPaths.COMMANDS_JUMP_EXCEPTION) || save;
		save = check(locale, toText("&aТеперь вы можете летать."), null, LocalesPaths.COMMANDS_FLY_ENABLE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a теперь может летать."), null, LocalesPaths.COMMANDS_FLY_ENABLE_STAFF) || save;
		save = check(locale, toText("&aПолет отключен."), null, LocalesPaths.COMMANDS_FLY_DISABLE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a больше не может летать."), null, LocalesPaths.COMMANDS_FLY_DISABLE_STAFF) || save;
		save = check(locale, toText("&aТеперь вы неуязвимы."), null, LocalesPaths.COMMANDS_GODMODE_ENABLE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a теперь имеет неуязвимость."), null, LocalesPaths.COMMANDS_GODMODE_ENABLE_STAFF) || save;
		save = check(locale, toText("&aНеуязвимость отключена."), null, LocalesPaths.COMMANDS_GODMODE_DISABLE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a больше не имеет неуязвимости."), null, LocalesPaths.COMMANDS_GODMODE_DISABLE_STAFF) || save;
		save = check(locale, toText("&aВы установили множитель вашей скорости: &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_SPEED_SELF) || save;
		save = check(locale, toText("&aВы установили множитель скорости игрока &e" + Placeholders.PLAYER + "&a: &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_SPEED_STAFF) || save;
		save = check(locale, toText("&aВам установили множитель вашей скорости: &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_SPEED_OTHER) || save;
		save = check(locale, toText("\n&aСкорость изменена для режима полета из-за нахождения в воздухе."), null, LocalesPaths.COMMANDS_SPEED_FLY) || save;
		save = check(locale, toText("по умолчанию"), null, LocalesPaths.COMMANDS_SPEED_DEFAULT) || save;
		save = check(locale, toText("&a&lМусорка"), null, LocalesPaths.COMMANDS_DISPOSAL_TITLE) || save;
		save = check(locale, toText("&aВаш игровой режим изменен на " + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_GAMEMODE_SUCCESS) || save;
		save = check(locale, toText("&aИгроку &e" + Placeholders.PLAYER + " &aустановлен режим игры " + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_GAMEMODE_OTHER) || save;
		save = check(locale, toText("&dкреатив"), null, LocalesPaths.COMMANDS_GAMEMODE_CREATIVE) || save;
		save = check(locale, toText("&7наблюдатель"), null, LocalesPaths.COMMANDS_GAMEMODE_SPECTATOR) || save;
		save = check(locale, toText("&2выживание"), null, LocalesPaths.COMMANDS_GAMEMODE_SURVIVAL) || save;
		save = check(locale, toText("&aприключение"), null, LocalesPaths.COMMANDS_GAMEMODE_ADVENTURE) || save;
		save = check(locale, toText("&aВы установили ясную погоду в мире &e" + Placeholders.WORLD + "&a."), null, LocalesPaths.COMMANDS_WEATHER_SUN) || save;
		save = check(locale, toText("&aВы установили дождливую погоду в мире &e" + Placeholders.WORLD + "&a."), null, LocalesPaths.COMMANDS_WEATHER_RAIN) || save;
		save = check(locale, toText("&aВы установили штормовую погоду в мире &e" + Placeholders.WORLD + "&a."), null, LocalesPaths.COMMANDS_WEATHER_THUNDER) || save;
		save = check(locale, toText("&aВы установили утро в мире &e" + Placeholders.WORLD + "&a."), null, LocalesPaths.COMMANDS_TIME_MORNING) || save;
		save = check(locale, toText("&aВы установили день в мире &e" + Placeholders.WORLD + "&a."), null, LocalesPaths.COMMANDS_TIME_DAY) || save;
		save = check(locale, toText("&aВы установили вечер в мире &e" + Placeholders.WORLD + "&a."), null, LocalesPaths.COMMANDS_TIME_EVENING) || save;
		save = check(locale, toText("&aВы установили ночь в мире &e" + Placeholders.WORLD + "&a."), null, LocalesPaths.COMMANDS_TIME_NIGHT) || save;
		save = check(locale, toText("&aВы изменили время в мире &e" + Placeholders.WORLD + "&a."), null, LocalesPaths.COMMANDS_TIME_ADD) || save;
		save = check(locale, toText("&cВы должны держать в руке предмет который хотите зачаровать."), null, LocalesPaths.COMMANDS_ENCHANT_ITEM_IS_NOT_PRESENT) || save;
		save = check(locale, toText("&aВы добавили зачарование к предмету в вашей руке."), null, LocalesPaths.COMMANDS_ENCHANT_SUCCES) || save;
		save = check(locale, toText("&aВы открыли инвентарь наковальни игроку &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_ANVIL) || save;
		save = check(locale, toText("&aВы открыли инвентарь верстака игроку &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_CRAFTING_TABLE) || save;
		save = check(locale, toText("&aВы открыли инвентарь стола зачарования игроку " + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_ENCHANTMENT_TABLE) || save;
		save = check(locale, toText("&aВаш уровень сытости стал максимальным."), null, LocalesPaths.COMMANDS_FEED_SELF) || save;
		save = check(locale, toText("&aВы насытили игрока &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_FEED_OTHER) || save;
		save = check(locale, toText("&aВаш уровень здоровья стал максимальным."), null, LocalesPaths.COMMANDS_HEAL_SELF) || save;
		save = check(locale, toText("&aВы вылечили игрока &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_HEAL_OTHER) || save;
		save = check(locale, toText("&9[&2Объявление&9]&r "), null, LocalesPaths.COMMANDS_BROADCAST) || save;
		save = check(locale, toText("&cКоманда не найдена."), null, LocalesPaths.COMMANDS_SUDO_COMMAND_NOT_FOUND) || save;
		save = check(locale, toText("&cВы не можете использовать эту команду, либо у целевого игрока нет прав на ее выполнение. Принуждение к выполнению не доступно."), null, LocalesPaths.COMMANDS_SUDO_EXECUTE_NOT_ALLOWED) || save;
		save = check(locale, toText("&eВыполняется попытка принуждения к выполнению команды игроком &6" + Placeholders.PLAYER + "&e."), null, LocalesPaths.COMMANDS_SUDO_SUCCESS) || save;
		save = check(locale, toText("&aТеперь вы не видимы."), null, LocalesPaths.COMMANDS_VANISH_VASHISHED) || save;
		save = check(locale, toText("&aВы снова видимы."), null, LocalesPaths.COMMANDS_VANISH_UNVASHISHED) || save;
		save = check(locale, toText("&aИгрок &e" + Placeholders.PLAYER + "&a теперь не видим."), null, LocalesPaths.COMMANDS_VANISH_VASHISHED_STAFF) || save;
		save = check(locale, toText("&aИгрок &e" + Placeholders.PLAYER + "&a снова видим."), null, LocalesPaths.COMMANDS_VANISH_UNVASHISHED_STAFF) || save;
		save = check(locale, toText("&aВаш ник изменен на &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_NICK_SET_SELF) || save;
		save = check(locale, toText("&aВаш ник изменен на изначальный."), null, LocalesPaths.COMMANDS_NICK_CLEAR_SELF) || save;
		save = check(locale, toText("&aВы узменили ник игроку &e" + Placeholders.PLAYER + "&a на &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_NICK_SET_STAFF) || save;
		save = check(locale, toText("&aВы восстановили изначальный ник игрока &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_NICK_CLEAR_STAFF) || save;
		save = check(locale, toText("&aВы установили предмету новое имя: &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_ITEM_SET_NAME) || save;
		save = check(locale, toText("&aВы восстановили изначальное имя предмета."), null, LocalesPaths.COMMANDS_ITEM_CLEAR_NAME) || save;
		save = check(locale, toText("&cВы должны держать предмет в руке."), null, LocalesPaths.COMMANDS_ITEM_EMPTY_HAND) || save;
		save = check(locale, toText("&aВы изменили описание предмета."), null, LocalesPaths.COMMANDS_ITEM_SET_LORE) || save;
		save = check(locale, toText("&aВы очистили описание предмета."), null, LocalesPaths.COMMANDS_ITEM_CLEAR_LORE) || save;
		save = check(locale, toText("&3&lО сервере"), null, LocalesPaths.COMMANDS_SERVERSTAT_HEADER) || save;
		save = check(locale, toText("&7[&4Система&7]"), null, LocalesPaths.COMMANDS_SERVERSTAT_BUTTON_SYSTEM) || save;
		save = check(locale, toText("&7[&eМиры&7]"), null, LocalesPaths.COMMANDS_SERVERSTAT_BUTTON_WORLDS) || save;
		save = check(locale, toText("&7[&aПлагины&7]"), null, LocalesPaths.COMMANDS_SERVERSTAT_BUTTON_PLUGINS) || save;
		save = check(locale, toText("&7[&2Моды&7]"), null, LocalesPaths.COMMANDS_SERVERSTAT_BUTTON_MODS) || save;
		save = check(locale, toText("&aТПС(Текущий, 1м, 5м, 10м)&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_TPS) || save;
		save = check(locale, toText("&aАптайм сервер / JVM&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_UPTIME) || save;
		save = check(locale, toText("&aВыделено(JVM) RAM&f: &e" + Placeholders.VALUE + "Мб"), null, LocalesPaths.COMMANDS_SERVERSTAT_MEMORY_MAX) || save;
		save = check(locale, toText("&aЗаразервировано RAM&f: &e" + Placeholders.VALUE + "Мб"), null, LocalesPaths.COMMANDS_SERVERSTAT_MEMORY_ALLOCATED) || save;
		save = check(locale, toText("&aИспользованно RAM&f:&e " + Placeholders.VALUE + "Мб(&6"+ Placeholders.FROM_ALLOCATED + "%&e от резерва, &6" + Placeholders.FROM_MAX + "%&e от максимума)"), null, LocalesPaths.COMMANDS_SERVERSTAT_MEMORY_UTILISED) || save;
		save = check(locale, toText("&aСвободно(в резерве) RAM&f:&e " + Placeholders.VALUE + "Мб"), null, LocalesPaths.COMMANDS_SERVERSTAT_MEMORY_FREE) || save;
		save = check(locale, toText("&aВремя сервера&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_TIME) || save;
		save = check(locale, toText("&3&lСведения о системе"), null, LocalesPaths.COMMANDS_SERVERSTAT_OS_HEADER) || save;
		save = check(locale, toText("&aСистема&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_OS) || save;
		save = check(locale, toText("&aJava&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_JAVA) || save;
		save = check(locale, toText("&e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_JAVAHOME) || save;
		save = check(locale, toText("&3&lДанные по мирам"), null, LocalesPaths.COMMANDS_SERVERSTAT_WORLDS_INFO_HEADER) || save;
		save = check(locale, toText("&aМир&f: &e" + Placeholders.WORLD + "&a. Загружено чанков&f: &e" + Placeholders.CHUNKS_SIZE + "&a. Сущностей&f: &e" + Placeholders.ENTITIES_SIZE + "&a. TPS&f: &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_SERVERSTAT_WORLDINFO) || save;
		save = check(locale, toText("&eПлагины &7(&b" + Placeholders.VALUE + "&7)&e"), null, LocalesPaths.COMMANDS_SERVERSTAT_HEADER_PLUGINS) || save;
		save = check(locale, toText("&eМоды &7(&b" + Placeholders.VALUE + "&7)&e"), null, LocalesPaths.COMMANDS_SERVERSTAT_HEADER_MODS) || save;
		save = check(locale, toText("&7[&6Перезагрузить&7]"), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_REFRESH_BUTTON) || save;
		save = check(locale, toText("&eВыполняется попытка перезагрузки плагина. Если в плагине не реализована прослушка события перезагрузки, то эффекта не будет."), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_REFRESH_MESSAGE) || save;
		save = check(locale, "d.MM.yyyy HH:mm:ss", null, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT) || save;
		save = check(locale, toText("&3О плагине"), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_HEADER) || save;
		save = check(locale, toText("&eID&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_ID) || save;
		save = check(locale, toText("&eНазвание&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_NAME) || save;
		save = check(locale, toText("&eВерсия&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_VERSION) || save;
		save = check(locale, toText("&eГлавный класс&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_ENTRYPOINT) || save;
		save = check(locale, toText("&eОписание&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_DESCRIPTION) || save;
		save = check(locale, toText("&eЗависимости&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_DEPENDENCIES) || save;
		save = check(locale, toText("&eРазработчики&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_CONTRIBUTORS) || save;
		save = check(locale, toText("&eСсылки&f:\n    &eДомашняя страница&f: &b" + Placeholders.HOME_LINK + "\n    &eИсходный код&f: &b" + Placeholders.SOURCE_LINK + "\n    &eТрекер проблем&f: &b" + Placeholders.ISSUES_LINK), null, LocalesPaths.COMMANDS_SERVERSTAT_PLUGIN_INFO_LINKS) || save;
		save = check(locale, toText("&3О моде"), null, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_HEADER) || save;
		save = check(locale, toText("&2ID&f: &2" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_ID) || save;
		save = check(locale, toText("&2Название&f: &2" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_NAME) || save;
		save = check(locale, toText("&2Версия&f: &2" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_VERSION) || save;
		save = check(locale, toText("&2Описание&f: &2" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_DESCRIPTION) || save;
		save = check(locale, toText("&2Зависимости&f: &2" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_DEPENDENCIES) || save;
		save = check(locale, toText("&2Ссылки&f:\n    &2Трекер проблем&f: &b" + Placeholders.ISSUES_LINK + "\n    &2Обновления&f: &b" + Placeholders.UPDATE_LINK), null, LocalesPaths.COMMANDS_SERVERSTAT_MOD_INFO_LINKS) || save;
		save = check(locale, toText("&cНабор с этим id уже существует."), null, LocalesPaths.COMMANDS_KITS_CREATE_EXIST) || save;
		save = check(locale, toText("&aНабор &e" + Placeholders.VALUE + "&a сохранен."), null, LocalesPaths.COMMANDS_KITS_SAVED) || save;
		save = check(locale, toText("&3Наборы"), null, LocalesPaths.COMMANDS_KITS_LIST_HEADER) || save;
		save = check(locale, toText("&cНет ни одного созданного набора."), null, LocalesPaths.COMMANDS_KITS_NO_KITS) || save;
		save = check(locale, toText("&cВремя указанно не корректно. Нужно указать время в формате ISO."), null, LocalesPaths.COMMANDS_EXCEPTION_COOLDOWN_INCORRECT_TIME) || save;
		save = check(locale, toText("&aУстановлено время восстановления доступа к набору &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_KITS_COOLDOWN_SUCCESS) || save;
		save = check(locale, toText("&aЛокализованное имя набора установлено."), null, LocalesPaths.COMMANDS_KITS_SET_NAME) || save;
		save = check(locale, toText("&aУстановлено правило выдачи набора."), null, LocalesPaths.COMMANDS_KITS_GIVE_RULE) || save;
		save = check(locale, toText("&aНабору добавлено описание для локализации по умолчанию. Для изменения отредактируйте файл конфигурации набора."), null, LocalesPaths.COMMANDS_KITS_CREATE_LORE) || save;
		save = check(locale, toText("&aУстановлен лимит получения набора игроком."), null, LocalesPaths.COMMANDS_KITS_GIVE_LIMIT) || save;
		save = check(locale, toText("&aТеперь набор будет автоматически выдаваться игроку единоразово."), null, LocalesPaths.COMMANDS_KITS_FIRST_TIME_ENABLE) || save;
		save = check(locale, toText("&aНабор больше не будет автоматически выдаваться игроку."), null, LocalesPaths.COMMANDS_KITS_FIRST_TIME_DISABLE) || save;
		save = check(locale, toText("&aТеперь будет производиться попытка автоматически выдать игроку набор при входе."), null, LocalesPaths.COMMANDS_KITS_GIVE_ON_JOIN_ENABLE) || save;
		save = check(locale, toText("&aНабор больше не будет автоматически выдаваться игроку."), null, LocalesPaths.COMMANDS_KITS_GIVE_ON_JOIN_DISABLE) || save;
		save = check(locale, toText("&aТеперь для получения набора требуется разрешение."), null, LocalesPaths.COMMANDS_KITS_NEEDPERM_ENABLE) || save;
		save = check(locale, toText("&aДля получения набора больше не требуется разрешение."), null, LocalesPaths.COMMANDS_KITS_NEEDPERM_DISABLE) || save;
		save = check(locale, toText("&aНабору добавлена команда."), null, LocalesPaths.COMMANDS_KITS_ADD_COMMAND) || save;
		save = check(locale, toText("&cНабор не содержит этой команды."), null, LocalesPaths.COMMANDS_KITS_COMMANDS_REMOVE_FAIL) || save;
		save = check(locale, toText("&aКоманда удалена."), null, LocalesPaths.COMMANDS_KITS_COMMANDS_REMOVE_SUCCESS) || save;
		save = check(locale, toText("&aНабор не содержит команд."), null, LocalesPaths.COMMANDS_KITS_COMMANDS_EMPTY) || save;
		save = check(locale, toText("&3&lКоманды"), null, LocalesPaths.COMMANDS_KITS_COMMANDS_HEADER) || save;
		save = check(locale, toText("&aУстановлена новая цена для получения набора."), null, LocalesPaths.COMMANDS_KITS_SET_PRICE) || save;
		save = check(locale, toText("&3Наборы"), null, LocalesPaths.COMMANDS_KIT_LIST_HEADER) || save;
		save = check(locale, toText("&7<&b&l⊙&7>&f⏊&7<&b&l⊙&7>"), null, LocalesPaths.COMMANDS_KIT_VIEW) || save;
		save = check(locale, toText("&cНет ни одного созданного набора."), null, LocalesPaths.COMMANDS_KIT_NO_KITS) || save;
		save = check(locale, toText("&cУ вас нет разрешения на получение этого набора."), null, LocalesPaths.COMMANDS_KIT_NO_PERM) || save;
		save = check(locale, toText("&cВы должны подождать &e" + Placeholders.VALUE + "&c прежде чем сможете получить этот набор."), null, LocalesPaths.COMMANDS_KIT_WAIT) || save;
		save = check(locale, toText("&eВ вашем инвентаре недостаточно места. Вы точно хотите получить этот набор? Часть предметов выпадут на землю. Кликните на это сообщение для подтверждения."), null, LocalesPaths.COMMANDS_KIT_INVENTORY_FULL) || save;
		save = check(locale, toText("&cВы исчерпали лимит получения данного набора."), null, LocalesPaths.COMMANDS_KIT_GIVE_LIMIT) || save;
		save = check(locale, toText("&cУ вас недостаточно денег для покупки этого набора. Необходимо как минимум &e" + Placeholders.VALUE + "&c."), null, LocalesPaths.COMMANDS_KIT_NO_MONEY) || save;
		save = check(locale, toText("&aВы получили набор &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_KIT_SUCCESS) || save;
		save = check(locale, toText("&aВы выдали набор &e" + Placeholders.VALUE + "&a игроку &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_KIT_SUCCESS_STAFF) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + " &fтеперь афк."), null, LocalesPaths.COMMANDS_AFK_ENABLE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + " &fбольше не афк."), null, LocalesPaths.COMMANDS_AFK_DISABLE) || save;
		save = check(locale, toText("&4&lАФК"), null, LocalesPaths.COMMANDS_AFK_TITLE) || save;
		save = check(locale, toText("&eВы будете кикнуты через " + Placeholders.VALUE + "&e."), null, LocalesPaths.COMMANDS_AFK_SUBTITLE) || save;
		save = check(locale, toText("&eВы перешли в АФК, но в настоящее время вы невидимы, так что это не транслировалось."), null, LocalesPaths.COMMANDS_AFK_ENABLE_IN_VANISH) || save;
		save = check(locale, toText("&eВы перешли из AFK, но в настоящее время вы невидимы, поэтому это не было передано."), null, LocalesPaths.COMMANDS_AFK_DISABLE_IN_VANISH) || save;
		save = check(locale, toText("&eВы кикнуты за то, что слишком долго были АФК."), null, LocalesPaths.COMMANDS_AFK_KICK) || save;
		save = check(locale, toText("&aМир &e\"" + Placeholders.WORLD + "\"&a создан.\n&eМиры создаются в асинхронном режиме. Без перезапуска сервера мир будет статичен."), null, LocalesPaths.COMMANDS_WORLD_CREATE) || save;
		save = check(locale, toText("&aВы телепортировались в мир &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_TELEPORT) || save;
		save = check(locale, toText("&aВы телепортировали игрока &e" + Placeholders.PLAYER + "&a в мир &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_TELEPORT_OTHER) || save;
		save = check(locale, toText("&aМир &e\"" + Placeholders.WORLD + "\"&a удален."), null, LocalesPaths.COMMANDS_WORLD_DELETE) || save;
		save = check(locale, toText("&aМир &e\"" + Placeholders.WORLD + "\"&a выгружен."), null, LocalesPaths.COMMANDS_WORLD_UNLOAD) || save;
		save = check(locale, toText("&cМир &e\"" + Placeholders.WORLD + "\"&c не загружен."), null, LocalesPaths.COMMANDS_WORLD_UNLOADED) || save;
		save = check(locale, toText("&aМир &e\"" + Placeholders.WORLD + "\"&a загружен."), null, LocalesPaths.COMMANDS_WORLD_LOAD) || save;
		save = check(locale, toText("&cМир &e\"" + Placeholders.WORLD + "\"&c уже загружен."), null, LocalesPaths.COMMANDS_WORLD_LOADED) || save;
		save = check(locale, toText("&aУстановлена точка спавна в мире &e\"" + Placeholders.WORLD + "\"&a по координатам &e" + Placeholders.LOCATION + "&a."), null, LocalesPaths.COMMANDS_WORLD_SETSPAWN) || save;
		save = check(locale, toText("&aУстановлен диаметр границы мира &e\"" + Placeholders.WORLD + "\"&a в значении &e" + Placeholders.VALUE + "&a с центром по координатам &e" + Placeholders.LOCATION + "&a."), null, LocalesPaths.COMMANDS_WORLD_SETBORDER) || save;
		save = check(locale, toText("&aМир &e\"" + Placeholders.WORLD + "\"&a будет загружаться при старте сервера."), null, LocalesPaths.COMMANDS_WORLD_ENABLE) || save;
		save = check(locale, toText("&aМир &e\"" + Placeholders.WORLD + "\"&a не будет загружаться при старте сервера."), null, LocalesPaths.COMMANDS_WORLD_DISABLE) || save;
		save = check(locale, toText("&aPvP включено в мире &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_ENABLE_PVP) || save;
		save = check(locale, toText("&aPvP выключено в мире &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_DISABLE_PVP) || save;
		save = check(locale, toText("&aУстановлена мирная сложность в мире &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_DIFFICULTY_PEACEFUL) || save;
		save = check(locale, toText("&aУстановлена низкая сложность в мире &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_DIFFICULTY_EASY) || save;
		save = check(locale, toText("&aУстановлена средняя сложность в мире &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_DIFFICULTY_NORMAL) || save;
		save = check(locale, toText("&aУстановлена высокая сложность в мире &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_DIFFICULTY_HARD) || save;
		save = check(locale, toText("&aУстановлен режим выживания в мире &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_GAMEMODE_SURVIVAL) || save;
		save = check(locale, toText("&aУстановлен творческий режим в мире &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_GAMEMODE_CREATIVE) || save;
		save = check(locale, toText("&aУстановлен режим приключения в мире &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_GAMEMODE_ADVENTURE) || save;
		save = check(locale, toText("&aУстановлен режим наблюдателя в мире &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_GAMEMODE_SPECTATOR) || save;
		save = check(locale, toText("&aУстановлена дистанция видимости в мире &e\"" + Placeholders.WORLD + "\"&a в значении &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_WORLD_VIEWDISTANCE) || save;
		save = check(locale, toText("&aИгровое правило &e" + Placeholders.RULE + "&a в мире &e\"" + Placeholders.WORLD + "\"&a установленно в значении &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_WORLD_GAMERULE_SUCCESS) || save;
		save = check(locale, toText("&6Игровые правила&f: &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_WORLD_GAMERULES) || save;
		save = check(locale, toText("&cУказано недопустимое значение для игрового правила &e" + Placeholders.RULE + "&a."), null, LocalesPaths.COMMANDS_WORLD_GAMERULE_INCORECT_VALUE) || save;
		save = check(locale, toText("&cНе удалось определить тип игрового правила &e" + Placeholders.RULE + "&c. Изменения не внесены."), null, LocalesPaths.COMMANDS_WORLD_GAMERULE_UNKNOWN_TYPE) || save;
		save = check(locale, "Выполняется генерация чанков в мире: " + Placeholders.WORLD + ". Выполненно: " + Placeholders.VALUE + "%. Последний сгенерированный чанк: " + Placeholders.LOCATION + ".", null, LocalesPaths.COMMANDS_WORLD_DEBUG_GENERATE) || save;
		save = check(locale, toText("&cЗадача генерации чанков в мире &e\"" + Placeholders.WORLD + "\"&c отсутствует."), null, LocalesPaths.COMMANDS_WORLD_NOT_STARTED_GENERATE) || save;
		save = check(locale, toText("&cНельзя удалить активную задачу генерации. Сначала поставьте ее на паузу."), null, LocalesPaths.COMMANDS_WORLD_NOT_PAUSED_GENERATE) || save;
		save = check(locale, toText("&aЗапущена генерация чанков в мире &e\"" + Placeholders.WORLD + "\"&a.\n&eЕсли сервер будет остановлен, то в следующий раз процедура будет запущенна с самого начала."), null, LocalesPaths.COMMANDS_WORLD_START_GENERATE) || save;
		save = check(locale, toText("&aГенерация чанков в мире &e\"" + Placeholders.WORLD + "\"&a приостановлена."), null, LocalesPaths.COMMANDS_WORLD_PAUSE_GENERATE) || save;
		save = check(locale, toText("&aГенерация чанков в мире &e\"" + Placeholders.WORLD + "\"&a прекращена. Задача удалена."), null, LocalesPaths.COMMANDS_WORLD_STOP_GENERATE) || save;
		save = check(locale, toText("&aВключена логика спавна в мире &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_SPAWN_LOGIC_ENABLE) || save;
		save = check(locale, toText("&aВыключена логика спавна в мире &e\"" + Placeholders.WORLD + "\"&a."), null, LocalesPaths.COMMANDS_WORLD_SPAWN_LOGIC_DISABLE) || save;
		save = check(locale, toText("&aТеперь вы будете видеть какие команды используют другие игроки."), null, LocalesPaths.COMMANDS_COMMANDSPY_ENABLE) || save;
		save = check(locale, toText("&aВы больше не будете видеть какие команды используют другие игроки."), null, LocalesPaths.COMMANDS_COMMANDSPY_DISABLE) || save;
		save = check(locale, toText("&8[&bCommand&9Spy&8] &e" + Placeholders.PLAYER + "&7 использует команду&f: &d" + Placeholders.COMMAND), null, LocalesPaths.COMMANDS_COMMANDSPY_SPY) || save;
		save = check(locale, toText("&aПинг&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_PING_SUCCESS) || save;
		save = check(locale, toText("&aПинг игрока &e" + Placeholders.PLAYER + "&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_PING_SUCCESS_STAFF) || save;
		save = check(locale, toText("&2&lИнвентарь&f&l: &e&l" + Placeholders.PLAYER), null, LocalesPaths.COMMANDS_INVSEE_TITLE) || save;
		save = check(locale, toText("&aОнлайн(" + Placeholders.VALUE + ")&f: &e"), null, LocalesPaths.COMMANDS_LIST_SUCCESS) || save;
		save = check(locale, toText("&7" + Placeholders.PLAYER), null, LocalesPaths.COMMANDS_LIST_VANISHED) || save;
		save = check(locale, toText("&3О игроке&f: " + Placeholders.PLAYER), null, LocalesPaths.COMMANDS_SEEN_TITLE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a сейчас &2онлайн"), null, LocalesPaths.COMMANDS_SEEN_ONLINE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a сейчас &4оффлайн"), null, LocalesPaths.COMMANDS_SEEN_OFFLINE) || save;
		save = check(locale, toText("&aИграет уже&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_ONLINE_TIME) || save;
		save = check(locale, toText("&aБыл(а) в сети&f: &e" + Placeholders.VALUE + "&a назад"), null, LocalesPaths.COMMANDS_SEEN_LAST_ONLINE) || save;
		save = check(locale, toText("&aОтображаемое имя&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_DISPLAY_NAME) || save;
		save = check(locale, toText("&aUUID&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_UUID) || save;
		save = check(locale, toText("&aIP&f: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_IP) || save;
		save = check(locale, toText("&aПервый вход&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_FIRST_PLAYED) || save;
		save = check(locale, toText("&aСкорость хотьбы&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_WALKING_SPEED) || save;
		save = check(locale, toText("&aСкорость полета&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_FLYING_SPEED) || save;
		save = check(locale, toText("&aТекущая локация&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_CURRENT_LOCATION) || save;
		save = check(locale, toText("&aДоступность полета&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_CAN_FLY) || save;
		save = check(locale, toText("&aПолет&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_FLYING) || save;
		save = check(locale, toText("&aРежим игры&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_GAMEMODE) || save;
		save = check(locale, toText("&aНевидимость&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_VANISHED) || save;
		save = check(locale, toText("&aНеуязвимость&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_GODMODE) || save;
		save = check(locale, toText("&aАФК&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_AFK) || save;
		save = check(locale, toText("&aБан&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_BAN) || save;
		save = check(locale, toText("&aМут&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_MUTE) || save;
		save = check(locale, toText("&aПредупреждений&f: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_SEEN_WARNS) || save;
		save = check(locale, toText("&eДа"), null, LocalesPaths.COMMANDS_SEEN_YES) || save;
		save = check(locale, toText("&eНет"), null, LocalesPaths.COMMANDS_SEEN_NO) || save;
		save = check(locale, toText("&3="), null, LocalesPaths.COMMANDS_SEEN_PADDING) || save;
		save = check(locale, toText("&3Справка"), null, LocalesPaths.COMMANDS_HELP_TITLE) || save;
		save = checkList(locale, Arrays.asList(
				toText("&eКоманды будут доступны при наличии разрешения на них."),
				toText("&2/spawn &f- &eпереместиться на спавн"),
				toText("&2/home &f- &eтелепортация домой")
				), null, LocalesPaths.COMMANDS_HELP_LIST) || save;
		save = check(locale, toText("&aТеперь вы светитесь."), null, LocalesPaths.COMMANDS_GLOW_ENABLE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a теперь светится."), null, LocalesPaths.COMMANDS_GLOW_ENABLE_STAFF) || save;
		save = check(locale, toText("&aВы больше не светитесь."), null, LocalesPaths.COMMANDS_GLOW_DISABLE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a больше не светится."), null, LocalesPaths.COMMANDS_GLOW_DISABLE_STAFF) || save;
		save = check(locale, toText("&aВы горите."), null, LocalesPaths.COMMANDS_FLAME_SUCCESS) || save;
		save = check(locale, toText("&4Вы горите!"), null, LocalesPaths.COMMANDS_FLAME_SUCCESS_DAMAGE) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a теперь горит."), null, LocalesPaths.COMMANDS_FLAME_SUCCESS_STAFF) || save;
		save = check(locale, toText("&aОгонь потушен."), null, LocalesPaths.COMMANDS_EXTINGUISH_SUCCESS) || save;
		save = check(locale, toText("&e" + Placeholders.PLAYER + "&a не горит."), null, LocalesPaths.COMMANDS_EXTINGUISH_SUCCESS_STAFF) || save;
		save = check(locale, toText("&aВы забанили &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_BAN_SUCCESS) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a банит игрока &e" + Placeholders.PLAYER + "&a.\nОкончание бана &e" + Placeholders.TIME + "&a.\n&aПричина: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_BAN_ANNOUNCEMENT) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a навсегда банит игрока &e" + Placeholders.PLAYER + "&a.\n&aПричина: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_BAN_ANNOUNCEMENT_PERMANENT) || save;
		save = check(locale, toText("&cНевозможно забанить &e" + Placeholders.PLAYER + "&c."), null, LocalesPaths.COMMANDS_BAN_IGNORE) || save;
		save = check(locale, toText("&eИгрок &e" + Placeholders.PLAYER + "&e уже имеет бан на этом сервере."), null, LocalesPaths.COMMANDS_BAN_ALREADY_BANNED) || save;
		save = check(locale, toText("&bВы забанены на сервере до &e" + Placeholders.TIME + ".\n&bБан выдан: &e" + Placeholders.SOURCE + "\n&bПричина: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_BAN_DISCONNECT) || save;
		save = check(locale, toText("&bВы на всегда забанены на сервере.\n&bБан выдан: &e" + Placeholders.SOURCE + "\n&bПричина: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_BAN_DISCONNECT_PERMANENT) || save;
		save = check(locale, toText("&aВы разбанили &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_UNBAN_SUCCESS) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a снимает бан с &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_UNBAN_ANNOUNCEMENT) || save;
		save = check(locale, toText("&aВы забанили игрока &e" + Placeholders.PLAYER + "&a и его IP &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_BANIP_SUCCESS) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a банит по IP игрока &e" + Placeholders.PLAYER + "&a.\nОкончание бана &e" + Placeholders.TIME + "&a.\n&aПричина: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_BANIP_ANNOUNCEMENT) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a навсегда банит по IP игрока &e" + Placeholders.PLAYER + "&a.\n&aПричина: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_BANIP_ANNOUNCEMENT_PERMANENT) || save;
		save = check(locale, toText("&bВаш IP забанен на сервере.\n&bБан выдан: &e" + Placeholders.SOURCE + "\n&bПричина: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_BANIP_DISCONNECT) || save;
		save = check(locale, toText("&aВы разбанили IP &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_UNBANIP_SUCCESS) || save;
		save = check(locale, toText("&aВы кикнули &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_KICK_SUCCESS) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&b кикает вас с сервера.\n&bПричина: &e" + Placeholders.VALUE + "."), null, LocalesPaths.COMMANDS_KICK_DISCONNECT) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a кикает с сервера" + Placeholders.PLAYER + ".\n&aПричина: &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_KICK_ANNOUNCEMENT) || save;
		save = check(locale, toText("&cНельзя кикнуть &e" + Placeholders.PLAYER + "&c."), null, LocalesPaths.COMMANDS_KICK_SUCCESS) || save;
		save = check(locale, toText("&aВы заблокировали чат игроку &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_MUTE_SUCCESS) || save;
		save = check(locale, toText("&cВам заблокирована возможность писать в чат до &e" + Placeholders.TIME + "&c.\nПричина: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_MUTE_SUCCESS_TARGET) || save;
		save = check(locale, toText("&cВам на всегда заблокирована возможность писать в чат.\nПричина: &e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_MUTE_SUCCESS_TARGET_PERMANENT) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a блокирует возможность писать в чат игроку &e" + Placeholders.PLAYER + "&a.\nОкончание мута &e" + Placeholders.TIME + "&a.\n&aПричина: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_MUTE_ANNOUNCEMENT) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a навсегда блокирует возможность писать в чат игроку &e" + Placeholders.PLAYER + "&a.\n&aПричина: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_MUTE_ANNOUNCEMENT_PERMANENT) || save;
		save = check(locale, toText("&cНевозможно заблокировать чат игроку &e" + Placeholders.PLAYER + "&c."), null, LocalesPaths.COMMANDS_MUTE_IGNORE) || save;
		save = check(locale, toText("&eИгрок &e" + Placeholders.PLAYER + "&e уже не может писать в чат."), null, LocalesPaths.COMMANDS_MUTE_ALREADY_MUTED) || save;
		save = check(locale, toText("&aВы размутили &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_UNMUTE_SUCCESS) || save;
		save = check(locale, toText("&aВы снова можете писать в чат."), null, LocalesPaths.COMMANDS_UNMUTE_SUCCESS_TARGET) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a снимает мут с &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_UNMUTE_ANNOUNCEMENT) || save;
		save = check(locale, toText("&aВы сделали предупреждение игроку &e" + Placeholders.PLAYER + "&a."), null, LocalesPaths.COMMANDS_WARN_SUCCESS) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a сделал вам предупреждение.\n&aПричина: &e\"" + Placeholders.VALUE + "&e\"&a.\nДействительно до &e" + Placeholders.TIME), null, LocalesPaths.COMMANDS_WARN_SUCCESS_TARGET) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a сделал вам перманентное предупреждение.\n&aПричина: &e\"" + Placeholders.VALUE + "&e\"&a."), null, LocalesPaths.COMMANDS_WARN_SUCCESS_TARGET_PERMANENT) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a делает предупреждение игроку &e" + Placeholders.PLAYER + "&a.\nДействительно до &e" + Placeholders.TIME + "&a.\n&aПричина: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_WARN_ANNOUNCEMENT) || save;
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a делает перманентное предупреждение игроку &e" + Placeholders.PLAYER + "&a.\n&aПричина: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_WARN_ANNOUNCEMENT_PERMANENT) || save;
		save = check(locale, toText("&cНевозможно сделать предупреждение &e" + Placeholders.PLAYER + "&c."), null, LocalesPaths.COMMANDS_WARN_IGNORE) || save;
		save = check(locale, toText("&4Автоматическая выдача наказаний"), null, LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH) || save;
		save = check(locale, toText("&cДостигнут лимит предупреждений до бана &e" + Placeholders.VALUE + "&c"), null, LocalesPaths.COMMANDS_WARN_BAN_LIMIT) || save;
		save = check(locale, toText("&cДостигнут лимит предупреждений до мута &e" + Placeholders.VALUE + "&c"), null, LocalesPaths.COMMANDS_WARN_MUTE_LIMIT) || save;
		save = check(locale, toText("&cДостигнут лимит предупреждений до кика &e" + Placeholders.VALUE + "&c"), null, LocalesPaths.COMMANDS_WARN_KICK_LIMIT) || save;
		save = check(locale, toText("&aПредупреждений за все время &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_WARNS_ALLTIME) || save;
		save = check(locale, toText("&aПредупреждений у &e" + Placeholders.PLAYER + "&a за все время &e" + Placeholders.VALUE + "&a."), null, LocalesPaths.COMMANDS_WARNS_ALLTIME_TARGET) || save;
		save = check(locale, toText("&3Предупреждения " + Placeholders.PLAYER), null, LocalesPaths.COMMANDS_WARNS_TITLE) || save;
		save = check(locale, toText("&aПричина: " + Placeholders.VALUE), null, LocalesPaths.COMMANDS_WARNS_REASON) || save;
		save = check(locale, toText("&e" + Placeholders.TIME + " &a➢ &e" + Placeholders.LIMIT), null, LocalesPaths.COMMANDS_WARNS_TIMES) || save;
		save = check(locale, toText("&cНет банов для отображения."), null, LocalesPaths.COMMANDS_BANLIST_EMPTY) || save;
		save = check(locale, toText("&3%profile% &3|| %ip%"), null, LocalesPaths.COMMANDS_BANLIST_TITLE) || save;
		save = check(locale, toText("&7[&eИгроки&7]"), null, LocalesPaths.COMMANDS_BANLIST_PROFILE) || save;
		save = check(locale, toText("&7[&eIP&7]"), null, LocalesPaths.COMMANDS_BANLIST_IP) || save;
		save = check(locale, toText("&e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_BANLIST_ELEMENT) || save;
		save = check(locale, toText("&aИгрок&f: &e" + Placeholders.PLAYER + "\n&aЗабанил&f: &e" + Placeholders.SOURCE + "\n&aСоздан&f: &e" + Placeholders.CREATED + "\n&aИстекает&f: &e" + Placeholders.EXPIRE + "\n&aПричина&f: &e" + Placeholders.REASON), null, LocalesPaths.COMMANDS_BANLIST_BANINFO_PLAYER) || save;
		save = check(locale, toText("&aIP&f: &e" + Placeholders.VALUE + "\n&aЗабанивший&f: &e" + Placeholders.SOURCE + "\n&aСоздан&f: &e" + Placeholders.CREATED + "\n&aИстекает&f: &e" + Placeholders.EXPIRE + "\n&aПричина&f: &e" + Placeholders.REASON), null, LocalesPaths.COMMANDS_BANLIST_BANINFO_IP) || save;
		save = check(locale, toText("&cПерманентно"), null, LocalesPaths.COMMANDS_BANINFO_PERMANENT) || save;
		save = check(locale, toText("&cНужно указать ник забаненого игрока."), null, LocalesPaths.COMMANDS_BANINFO_NOT_PRESENT) || save;
		save = check(locale, toText("&aЗабанил&f: &e" + Placeholders.SOURCE + "\n&aСоздан&f: &e" + Placeholders.CREATED + "\n&aИстекает&f: &e" + Placeholders.EXPIRE + "\n&aПричина&f: &e" + Placeholders.REASON), null, LocalesPaths.COMMANDS_BANINFO_SUCCESS) || save;
		save = check(locale, toText("&3Информация о бане " + Placeholders.PLAYER), null, LocalesPaths.COMMANDS_BANINFO_TITLE) || save;
		save = check(locale, toText("&cПерманентно"), null, LocalesPaths.COMMANDS_MUTEINFO_PERMANENT) || save;
		save = check(locale, toText("&cНужно указать ник игрока с заблокированным чатом."), null, LocalesPaths.COMMANDS_MUTEINFO_NOT_PRESENT) || save;
		save = check(locale, toText("&aЗаглушил&f: &e" + Placeholders.SOURCE + "\n&aСоздан&f: &e" + Placeholders.CREATED + "\n&aИстекает&f: &e" + Placeholders.EXPIRE + "\n&aПричина&f: &e" + Placeholders.REASON), null, LocalesPaths.COMMANDS_MUTEINFO_SUCCESS) || save;
		save = check(locale, toText("&3Информация о муте " + Placeholders.PLAYER), null, LocalesPaths.COMMANDS_MUTEINFO_TITLE) || save;
		save = check(locale, toText("&cНет мутов для отображения."), null, LocalesPaths.COMMANDS_MUTELIST_EMPTY) || save;
		save = check(locale, toText("&3Список мутов (" + Placeholders.VALUE + ")"), null, LocalesPaths.COMMANDS_MUTELIST_TITLE) || save;
		save = check(locale, toText("&e" + Placeholders.VALUE), null, LocalesPaths.COMMANDS_MUTELIST_ELEMENT) || save;
		save = check(locale, toText("&aИгрок&f: &e" + Placeholders.PLAYER + "\n&aЗаглушил&f: &e" + Placeholders.SOURCE + "\n&aСоздан&f: &e" + Placeholders.CREATED + "\n&aИстекает&f: &e" + Placeholders.EXPIRE + "\n&aПричина&f: &e" + Placeholders.REASON), null, LocalesPaths.COMMANDS_MUTELIST_INFO) || save;

		if(save) save(locale);
	}

	private Component replace(Component component, Map<String, String> map) {
		return TextUtils.replace(component, map);
	}

	private Component replaceComponent(Component component, Map<String, Component> map) {
		return TextUtils.replaceToComponents(component, map);
	}

	private AbstractLocaleUtil getAbstractLocaleUtil(Locale locale) {
		return localeService.getPluginLocales(pluginid).getOrDefault(locale, localeService.getPluginLocales(pluginid).get(org.spongepowered.api.util.locale.Locales.DEFAULT));
	}

	private Component toText(String string) {
		return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
	}

	private boolean check(Locale locale, Component value, String comment, Object... path) {
		return getAbstractLocaleUtil(locale).checkComponent(json, value, comment, path);
	}

	private boolean check(Locale locale, String value, String comment, Object... path) {
		return getAbstractLocaleUtil(locale).checkString(value, comment, path);
	}

	private boolean checkList(Locale locale, List<Component> value, String comment, Object... path) {
		return getAbstractLocaleUtil(locale).checkListComponents(json, value, comment, path);
	}

	private void save(Locale locale) {
		getAbstractLocaleUtil(locale).saveLocaleNode();
	}

}
