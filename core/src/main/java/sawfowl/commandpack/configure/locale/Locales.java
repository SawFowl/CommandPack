package sawfowl.commandpack.configure.locale;

import java.util.Locale;
import java.util.stream.Collectors;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.ConfigurateException;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.configure.locale.locales.AbstractLocale;
import sawfowl.commandpack.configure.locale.locales.def.ImplementPluginLocale;
import sawfowl.commandpack.configure.locale.locales.ru.ImplementRuPluginLocale;
import sawfowl.localeapi.api.ConfigTypes;
import sawfowl.localeapi.api.LocaleService;
import sawfowl.localeapi.api.PluginLocale;

public class Locales {

	private final LocaleService localeService;
	private String[] localesTags;
	private String pluginid = "commandpack";
	public Locales(LocaleService localeService) {
		this.localeService = localeService;
		localeService.localesExist(pluginid);
		localeService.createPluginLocale(pluginid, ConfigTypes.HOCON, org.spongepowered.api.util.locale.Locales.DEFAULT);
		localeService.createPluginLocale(pluginid, ConfigTypes.HOCON, org.spongepowered.api.util.locale.Locales.RU_RU);
		localeService.setDefaultReference(CommandPackInstance.getInstance().getPluginContainer(), ImplementPluginLocale.class);
		generateDefault();
		generateRu();
		localeService.getPluginLocales(pluginid).values().forEach(locale -> {
			try {
				locale.setLocaleReference(ImplementPluginLocale.class);
			} catch (ConfigurateException e) {
				e.printStackTrace();
			}
		});
	}

	public LocaleService getLocaleService() {
		return localeService;
	}

	public String[] getLocalesTags() {
		if(localesTags != null) return localesTags;
		return localesTags = localeService.getLocalesList().stream().map(Locale::toLanguageTag).collect(Collectors.toList()).stream().toArray(String[]::new);
	}

	public AbstractLocale getLocale(ServerPlayer player) {
		return getLocale(player.locale());
	}

	public AbstractLocale getLocale(Locale locale) {
		return getPluginLocale(locale).asReference(AbstractLocale.class);
	}

	public AbstractLocale getSystemLocale() {
		return getPluginLocale(localeService.getSystemOrDefaultLocale()).asReference(AbstractLocale.class);
	}

	private void generateDefault() {
		try {
			getPluginLocale(org.spongepowered.api.util.locale.Locales.DEFAULT).setLocaleReference(ImplementPluginLocale.class);
			getPluginLocale(org.spongepowered.api.util.locale.Locales.DEFAULT).saveLocaleNode();
		} catch (ConfigurateException e) {
			e.printStackTrace();
		}
	}

	private void generateRu() {
		try {
			getPluginLocale(org.spongepowered.api.util.locale.Locales.RU_RU).setLocaleReference(ImplementRuPluginLocale.class);
			getPluginLocale(org.spongepowered.api.util.locale.Locales.RU_RU).saveLocaleNode();
		} catch (ConfigurateException e) {
			e.printStackTrace();
		}
	}

	private PluginLocale getPluginLocale(Locale locale) {
		return localeService.getPluginLocales(pluginid).getOrDefault(locale, localeService.getPluginLocales(pluginid).get(org.spongepowered.api.util.locale.Locales.DEFAULT));
	}

}
