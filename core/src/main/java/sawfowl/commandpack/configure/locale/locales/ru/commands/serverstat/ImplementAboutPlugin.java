package sawfowl.commandpack.configure.locale.locales.ru.commands.serverstat;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat.AboutPlugin;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementAboutPlugin implements AboutPlugin {

	public ImplementAboutPlugin() {}

	@Setting("Title")
	private Component title = TextUtils.deserializeLegacy("&3Информация о плагине");
	@Setting("Id")
	private Component id = TextUtils.deserializeLegacy("&eID&f: &e" + Placeholders.VALUE);
	@Setting("Name")
	private Component name = TextUtils.deserializeLegacy("&eИмя&f: &e" + Placeholders.VALUE);
	@Setting("Version")
	private Component version = TextUtils.deserializeLegacy("&eВерсия&f: &e" + Placeholders.VALUE);
	@Setting("EntryPoint")
	private Component entryPoint = TextUtils.deserializeLegacy("&eГлавный класс&f: &e" + Placeholders.VALUE);
	@Setting("Description")
	private Component description = TextUtils.deserializeLegacy("&eОписание&f: &e" + Placeholders.VALUE);
	@Setting("Dependencies")
	private Component dependencies = TextUtils.deserializeLegacy("&eЗависимости&f: &e" + Placeholders.VALUE);
	@Setting("Contributors")
	private Component contributors = TextUtils.deserializeLegacy("&eРазработчики&f: &e" + Placeholders.VALUE);
	@Setting("Links")
	private Component links = TextUtils.deserializeLegacy("&eСсылки&f:\n    &eДомашняя страница&f: &b" + Placeholders.HOME_LINK + "\n    &eИсходный код&f: &b" + Placeholders.SOURCE_LINK + "\n    &eТрекер проблем&f: &b" + Placeholders.ISSUES_LINK);

	@Override
	public Component getTitle() {
		return title;
	}

	@Override
	public Component getId(String value) {
		return Text.of(id).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getName(String value) {
		return Text.of(name).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getVersion(String value) {
		return Text.of(version).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getEntrypoint(String value) {
		return Text.of(entryPoint).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getDescription(String value) {
		return Text.of(description).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getDependencies(Component value) {
		return Text.of(dependencies).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getContributors(Component value) {
		return Text.of(contributors).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getLinks(Component home, Component source, Component issues) {
		return Text.of(links).replace(Placeholders.HOME_LINK, home).replace(Placeholders.SOURCE_LINK, source).replace(Placeholders.ISSUES_LINK, issues).get();
	}

}
