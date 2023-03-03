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
		localeService.createPluginLocale(pluginid, ConfigTypes.HOCON, org.spongepowered.api.util.locale.Locales.DEFAULT);
		localeService.createPluginLocale(pluginid, ConfigTypes.HOCON, org.spongepowered.api.util.locale.Locales.RU_RU);
		generateDefault();
		generateRu();
		localeService.localesExist(pluginid);
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
		//boolean save = check(locale, toText("&cThis command can only be used by a player."), null, LocalesPaths.COMMANDS_ONLY_PLAYER);

		boolean save = check(locale, toText("&eThere is no economy plugin on the server. Some of the functionality will not be available."), null, LocalesPaths.ECONOMY_NOT_FOUND);
		save = check(locale, toText("Failed to add game currency to player " + Placeholders.PLAYER + "."), null, LocalesPaths.ECONOMY_ERROR_GIVE_MONEY) || save;
		save = check(locale, toText("Failed to take game currency from player " + Placeholders.PLAYER + "."), null, LocalesPaths.ECONOMY_ERROR_TAKE_MONEY) || save;

		save = check(locale, toText("h"), null, LocalesPaths.TIME_HOUR) || save;
		save = check(locale, toText("m"), null, LocalesPaths.TIME_MINUTE) || save;
		save = check(locale, toText("s"), null, LocalesPaths.TIME_SECOND) || save;

		save = check(locale, toText("&cWait &e" + Placeholders.DELAY + "&c before using this command again."), null, LocalesPaths.COMMANDS_COOLDOWN) || save;
		save = check(locale, toText("&aCommand activation via &e" + Placeholders.DELAY + "&a."), null, LocalesPaths.COMMANDS_WAIT) || save;
		save = check(locale, toText(Placeholders.SOURCE + " ran the command: " + Placeholders.COMMAND + ""), null, LocalesPaths.COMMANDS_LOG) || save;
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

		if(save) save(locale);
	}

	private void generateRu() {
		Locale locale = org.spongepowered.api.util.locale.Locales.RU_RU;
		//boolean save = check(locale, toText("&cЭту команду может использовать только игрок."), null, LocalesPaths.COMMANDS_ONLY_PLAYER);

		boolean save = check(locale, toText("Не найден плагин экономики. Часть функций может быть не доступна."), null, LocalesPaths.ECONOMY_NOT_FOUND);
		save = check(locale, toText("Не удалось начислить игровую валюту игроку " + Placeholders.PLAYER + "."), null, LocalesPaths.ECONOMY_ERROR_GIVE_MONEY) || save;
		save = check(locale, toText("Не удалось списать игровую валюту у игрока " + Placeholders.PLAYER + "."), null, LocalesPaths.ECONOMY_ERROR_TAKE_MONEY) || save;

		save = check(locale, toText("ч"), null, LocalesPaths.TIME_HOUR) || save;
		save = check(locale, toText("м"), null, LocalesPaths.TIME_MINUTE) || save;
		save = check(locale, toText("с"), null, LocalesPaths.TIME_SECOND) || save;

		save = check(locale, toText("&cПодождите &e" + Placeholders.DELAY + "&c прежде чем снова использовать эту команду."), null, LocalesPaths.COMMANDS_COOLDOWN) || save;
		save = check(locale, toText("&aАктивация команды через &e" + Placeholders.DELAY + "&a."), null, LocalesPaths.COMMANDS_WAIT) || save;
		save = check(locale, toText(Placeholders.SOURCE + " выполняет команду: " + Placeholders.COMMAND + ""), null, LocalesPaths.COMMANDS_LOG) || save;
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
