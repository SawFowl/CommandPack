package sawfowl.commandpack.configure.locale.locales.def.commands.serverstat;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat.AboutMod;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementAboutMod implements AboutMod {

	public ImplementAboutMod() {}

	@Setting("Title")
	private Component title = TextUtils.deserializeLegacy("&3About mod");
	@Setting("Id")
	private Component id = TextUtils.deserializeLegacy("&2ID&f: &2" + Placeholders.VALUE);
	@Setting("Name")
	private Component name = TextUtils.deserializeLegacy("&2Name&f: &2" + Placeholders.VALUE);
	@Setting("Version")
	private Component version = TextUtils.deserializeLegacy("&2Version&f: &2" + Placeholders.VALUE);
	@Setting("Description")
	private Component description = TextUtils.deserializeLegacy("&2Description&f: &2" + Placeholders.VALUE);
	@Setting("Dependencies")
	private Component dependencies = TextUtils.deserializeLegacy("&2Dependencies&f: &2" + Placeholders.VALUE);
	@Setting("Links")
	private Component links = TextUtils.deserializeLegacy("&2Links&f:\n    &2Issues&f: &b" + Placeholders.ISSUES_LINK + "\n    &2Updates&f: &b" + Placeholders.UPDATE_LINK);

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
	public Component getDescription(String value) {
		return Text.of(description).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getDependencies(Component value) {
		return Text.of(dependencies).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getLinks(Component home, Component source, Component issues) {
		return Text.of(links).replace(Placeholders.HOME_LINK, home).replace(Placeholders.SOURCE_LINK, source).replace(Placeholders.ISSUES_LINK, issues).get();
	}

}
