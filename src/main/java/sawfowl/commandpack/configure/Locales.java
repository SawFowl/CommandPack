package sawfowl.commandpack.configure;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

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

		if(save) save(locale);
	}

	private void generateRu() {
		Locale locale = org.spongepowered.api.util.locale.Locales.RU_RU;
		//boolean save = check(locale, toText("&cЭту команду может использовать только игрок."), null, LocalesPaths.COMMANDS_ONLY_PLAYER);

		boolean save = check(locale, toText("Не найден плагин экономики. Часть функций может быть не доступна."), null, LocalesPaths.ECONOMY_NOT_FOUND);
		save = check(locale, toText("&cНе удалось начислить игровую валюту игроку " + Placeholders.PLAYER + "."), null, LocalesPaths.ECONOMY_ERROR_GIVE_MONEY) || save;
		save = check(locale, toText("&cНе удалось списать игровую валюту у игрока " + Placeholders.PLAYER + "."), null, LocalesPaths.ECONOMY_ERROR_TAKE_MONEY) || save;

		save = check(locale, toText("ч"), null, LocalesPaths.TIME_HOUR) || save;
		save = check(locale, toText("м"), null, LocalesPaths.TIME_MINUTE) || save;
		save = check(locale, toText("с"), null, LocalesPaths.TIME_SECOND) || save;

		save = check(locale, toText("&cПодождите &e" + Placeholders.DELAY + "&c прежде чем снова использовать эту команду."), null, LocalesPaths.COMMANDS_COOLDOWN) || save;
		save = check(locale, toText("&aАктивация команды через &e" + Placeholders.DELAY + "&a."), null, LocalesPaths.COMMANDS_WAIT) || save;
		save = check(locale, toText(Placeholders.SOURCE + " выполняет команду: " + Placeholders.COMMAND + ""), null, LocalesPaths.COMMANDS_LOG) || save;

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
