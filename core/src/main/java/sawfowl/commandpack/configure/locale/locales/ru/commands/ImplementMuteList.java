package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.MuteList;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementMuteList implements MuteList {

	public ImplementMuteList() {}

	@Setting("Empty")
	private Component empty = TextUtils.deserializeLegacy("&cThere are no mutes to display.");
	@Setting("Title")
	private Component title = TextUtils.deserializeLegacy("&3Mutes (" + Placeholders.VALUE + ")");
	@Setting("Element")
	private Component element = TextUtils.deserializeLegacy("&e" + Placeholders.VALUE);
	@Setting("Info")
	private Component info = TextUtils.deserializeLegacy("&aPlayer&f: &e" + Placeholders.PLAYER + "\n&aSource&f: &e" + Placeholders.SOURCE + "\n&aCreated&f: &e" + Placeholders.CREATED + "\n&aExpire&f: &e" + Placeholders.EXPIRE + "\n&aReason&f: &e" + Placeholders.REASON);

	@Override
	public Component getEmpty() {
		return empty;
	}

	@Override
	public Component getTitle(int size) {
		return Text.of(title).replace(Placeholders.VALUE, size).get();
	}

	@Override
	public Component getElement(String value) {
		return Text.of(element).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getInfo(String player, Component source, String created, Component expire, Component reason) {
		return Text.of(info).replace(Placeholders.PLAYER, player).replace(Placeholders.SOURCE, source).replace(Placeholders.CREATED, created).replace(Placeholders.EXPIRE, expire).replace(Placeholders.REASON, reason).get();
	}

}
