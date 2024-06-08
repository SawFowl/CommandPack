package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Home;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementHome implements Home {

	public ImplementHome() {}

	@Setting("NotFound")
	private Component notFound = TextUtils.deserializeLegacy("&cA home point with a name of &e" + Placeholders.HOME + "&c does not exist.");
	@Setting("NotSet")
	private Component notSet = TextUtils.deserializeLegacy("&cNo home point is found. Use the &e/sethome&c command to set a home point.");
	@Setting("Error")
	private Component error = TextUtils.deserializeLegacy("&cHome point is not available. Perhaps the world is not loaded.");
	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aYou teleported to home &e" + Placeholders.HOME + "&a.");
	@Setting("ListTitle")
	private Component list = TextUtils.deserializeLegacy("&bHomes");

	@Override
	public Component getNotFound(String home) {
		return Text.of(notFound).replace(Placeholders.HOME, home).get();
	}

	@Override
	public Component getNotSet() {
		return notSet;
	}

	@Override
	public Component getError() {
		return error;
	}

	@Override
	public Component getSuccess(Component home) {
		return Text.of(success).replace(Placeholders.HOME, home).get();
	}

	@Override
	public Component getListTitle() {
		return list;
	}

}
