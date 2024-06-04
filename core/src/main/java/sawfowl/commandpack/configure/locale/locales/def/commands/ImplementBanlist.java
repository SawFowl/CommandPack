package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Banlist;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.banlist.Info;
import sawfowl.commandpack.configure.locale.locales.def.commands.banlist.ImplementInfo;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementBanlist implements Banlist {

	public ImplementBanlist() {}
	@Setting("Info")
	private ImplementInfo info = new ImplementInfo();
	@Setting("Empty")
	private Component empty = TextUtils.deserializeLegacy("&aThere are no bans to display.");
	@Setting("Title")
	private Component title = TextUtils.deserializeLegacy("%profile% &3|| %ip%");
	@Setting("Profile")
	private Component profile = TextUtils.deserializeLegacy("&7[&ePlayers&7]");
	@Setting("IP")
	private Component ip = TextUtils.deserializeLegacy("&7[&eIP&7]");
	@Setting("Element")
	private Component element = TextUtils.deserializeLegacy("&e" + Placeholders.VALUE);

	@Override
	public Info getInfo() {
		return info;
	}

	@Override
	public Component getEmpty() {
		return empty;
	}

	@Override
	public Text getTitle() {
		return Text.of(title);
	}

	@Override
	public Component getProfile() {
		return profile;
	}

	@Override
	public Component getIP() {
		return ip;
	}

	@Override
	public Component getElement(String element) {
		return Text.of(this.element).replace(Placeholders.VALUE, element).get();
	}

}
