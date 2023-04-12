package sawfowl.commandpack.configure.locale;

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

		save = check(locale, toText("h"), null, LocalesPaths.TIME_HOUR) || save;
		save = check(locale, toText("m"), null, LocalesPaths.TIME_MINUTE) || save;
		save = check(locale, toText("s"), null, LocalesPaths.TIME_SECOND) || save;

		save = check(locale, toText("&7[&cRemove&7] "), null, LocalesPaths.REMOVE) || save;
		save = check(locale, toText("&7[&aTeleport&7] "), null, LocalesPaths.TELEPORT) || save;
		save = check(locale, toText("&7[&eTeleport&7] "), null, LocalesPaths.TELEPORTCLICKABLE) || save;

		save = check(locale, toText("Server"), null, LocalesPaths.NAME_SYSTEM) || save;
		save = check(locale, toText("Command block"), null, LocalesPaths.NAME_COMMANDBLOCK) || save;
		save = check(locale, toText("Minecart with command block"), null, LocalesPaths.NAME_COMMANDBLOCK_MINECART) || save;
		save = check(locale, toText("Unknown"), null, LocalesPaths.NAME_UNKNOWN) || save;

		save = check(locale, toText("&cYou need to specify the nickname of the player online."), null, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT) || save;
		save = check(locale, toText("&cYou can't point to yourself."), null, LocalesPaths.COMMANDS_EXCEPTION_TARGET_SELF) || save;
		save = check(locale, toText("&cYou must specify a name."), null, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT) || save;
		save = check(locale, toText("&cYou must specify a type."), null, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT) || save;
		save = check(locale, toText("&cYou must specify a value."), null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT) || save;
		save = check(locale, toText("&cIt is necessary to specify the command argument as boolean. More information about the arguments of the entered command can be found in the documentation of the plugin."), null, LocalesPaths.COMMANDS_EXCEPTION_BOOLEAN_NOT_PRESENT) || save;
		save = check(locale, toText("&cYou need to specify the coordinates."), null, LocalesPaths.COMMANDS_EXCEPTION_LOCATION_NOT_PRESENT) || save;
		save = check(locale, toText("&cYou have to specify the world."), null, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT) || save;
		save = check(locale, toText("&cWait &e" + Placeholders.DELAY + "&c before using this command again."), null, LocalesPaths.COMMANDS_COOLDOWN) || save;
		save = check(locale, toText("&aCommand activation via &e" + Placeholders.DELAY + "&a."), null, LocalesPaths.COMMANDS_WAIT) || save;
		save = check(locale, toText(Placeholders.SOURCE + " ran the command: /" + Placeholders.COMMAND + Placeholders.ARGS + ""), null, LocalesPaths.COMMANDS_LOG) || save;
		save = check(locale, toText("CommandSettings \"" + Placeholders.COMMAND + "\" is not registered for tracking."), null, LocalesPaths.COMMANDS_NOT_TRACKING) || save;
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
		save = check(locale, toText("&e" + Placeholders.SOURCE + "&a teleported you to the spawnpoint."), null, LocalesPaths.COMMANDS_SPAWN_SUCCESS_OTHER) || save;
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
		save = check(locale, toText("&cFailed to find a position in the world '" + Placeholders.WORLD + "' for teleportation. The search attempt limit has been reached: " + Placeholders.LIMIT + "."), null, LocalesPaths.COMMANDS_COMMAND_RANDOM_TELEPORT_POSITION_SEARCH_ERROR_STAFF) || save;
		save = check(locale, toText("&cUnable to find teleportation position. Try again."), null, LocalesPaths.COMMANDS_COMMAND_RANDOM_TELEPORT_POSITION_SEARCH_ERROR) || save;
		save = check(locale, toText("&aPlayer &e" + Placeholders.PLAYER + "&a is teleported."), null, LocalesPaths.COMMANDS_COMMAND_RANDOM_TELEPORT_SUCCES_STAFF) || save;
		save = check(locale, toText("&eTeleportation has been canceled. Try again."), null, LocalesPaths.COMMANDS_COMMAND_RANDOM_TELEPORT_CANCELLED) || save;
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
		save = check(locale, toText("&a&lTrash"), null, LocalesPaths.COMMANDS_DISPOSAL_TITLE) || save;

		if(save) save(locale);
	}

	private void generateRu() {
		Locale locale = org.spongepowered.api.util.locale.Locales.RU_RU;

		boolean save = check(locale, toText("Не найден плагин экономики. Часть функций может быть не доступна."), null, LocalesPaths.ECONOMY_NOT_FOUND);
		save = check(locale, toText("Не удалось начислить игровую валюту игроку " + Placeholders.PLAYER + "."), null, LocalesPaths.ECONOMY_ERROR_GIVE_MONEY) || save;
		save = check(locale, toText("Не удалось списать игровую валюту у игрока " + Placeholders.PLAYER + "."), null, LocalesPaths.ECONOMY_ERROR_TAKE_MONEY) || save;

		save = check(locale, toText("ч"), null, LocalesPaths.TIME_HOUR) || save;
		save = check(locale, toText("м"), null, LocalesPaths.TIME_MINUTE) || save;
		save = check(locale, toText("с"), null, LocalesPaths.TIME_SECOND) || save;

		save = check(locale, toText("&7[&cУдалить&7] "), null, LocalesPaths.REMOVE) || save;
		save = check(locale, toText("&7[&aТелепорт&7] "), null, LocalesPaths.TELEPORT) || save;
		save = check(locale, toText("&7[&eТелепорт&7] "), null, LocalesPaths.TELEPORTCLICKABLE) || save;

		save = check(locale, toText("Сервер"), null, LocalesPaths.NAME_SYSTEM) || save;
		save = check(locale, toText("Командный блок"), null, LocalesPaths.NAME_COMMANDBLOCK) || save;
		save = check(locale, toText("Вагонетка с командным блоком"), null, LocalesPaths.NAME_COMMANDBLOCK_MINECART) || save;
		save = check(locale, toText("Неизвестный"), null, LocalesPaths.NAME_UNKNOWN) || save;

		save = check(locale, toText("&cНужно указать ник игрока онлайн."), null, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT) || save;
		save = check(locale, toText("&cНельзя указывать на себя."), null, LocalesPaths.COMMANDS_EXCEPTION_TARGET_SELF) || save;
		save = check(locale, toText("&cНужно указать имя."), null, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT) || save;
		save = check(locale, toText("&cНужно указать тип."), null, LocalesPaths.COMMANDS_EXCEPTION_TYPE_NOT_PRESENT) || save;
		save = check(locale, toText("&cНужно указать значение."), null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT) || save;
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
		save = check(locale, toText("&e" + Placeholders.SOURCE + " &aпереместил вас на спавн."), null, LocalesPaths.COMMANDS_SPAWN_SUCCESS_OTHER) || save;
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
		save = check(locale, toText("&cНе удалось найти позицию в мире '" + Placeholders.WORLD + "' для телепортации. Достигнут лимит попыток поиска: " + Placeholders.LIMIT + "."), null, LocalesPaths.COMMANDS_COMMAND_RANDOM_TELEPORT_POSITION_SEARCH_ERROR_STAFF) || save;
		save = check(locale, toText("&cНе удалось найти позицию для телепортации. Попробуйте еще раз."), null, LocalesPaths.COMMANDS_COMMAND_RANDOM_TELEPORT_POSITION_SEARCH_ERROR) || save;
		save = check(locale, toText("&aИгрок &e" + Placeholders.PLAYER + "&a телепортирован"), null, LocalesPaths.COMMANDS_COMMAND_RANDOM_TELEPORT_SUCCES_STAFF) || save;
		save = check(locale, toText("&eТелепортация отменена. Попробуйте еще раз."), null, LocalesPaths.COMMANDS_COMMAND_RANDOM_TELEPORT_CANCELLED) || save;
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
		save = check(locale, toText("&a&lМусорка"), null, LocalesPaths.COMMANDS_DISPOSAL_TITLE) || save;

		if(save) save(locale);
	}

	private Component replace(Component component, Map<String, String> map) {
		return TextUtils.replace(component, map);
	}

	private Component replaceComponent(Component component, Map<String, Component> map) {
		return TextUtils.replaceToComponents(component, map);
	}

	private AbstractLocaleUtil getAbstractLocaleUtil(Locale locale) {
		return localeService.getPluginLocales(pluginid).getOrDefault(locale, localeService.getPluginLocales("wasted").get(org.spongepowered.api.util.locale.Locales.DEFAULT));
	}

	private Component toText(String string) {
		return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
	}

	private boolean check(Locale locale, Component value, String comment, Object... path) {
		return getAbstractLocaleUtil(locale).checkComponent(json, value, comment, path);
	}

	private void save(Locale locale) {
		getAbstractLocaleUtil(locale).saveLocaleNode();
	}

}
