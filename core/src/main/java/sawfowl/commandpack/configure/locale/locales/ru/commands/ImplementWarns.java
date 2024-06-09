package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Warns;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementWarns implements Warns {

	public ImplementWarns() {}

	@Setting("AllTime")
	private Component allTime = TextUtils.deserializeLegacy("&aWarnings in all time &e" + Placeholders.VALUE + "&a.");
	@Setting("AllTimeTarget")
	private Component allTimeTarget = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + "&a warnings in all time &e" + Placeholders.VALUE + "&a.");
	@Setting("Title")
	private Component title = TextUtils.deserializeLegacy("&3" + Placeholders.PLAYER + " warnings");
	@Setting("Reason")
	private Component reason = TextUtils.deserializeLegacy("&aReason: " + Placeholders.VALUE);
	@Setting("Times")
	private Component times = TextUtils.deserializeLegacy("&e" + Placeholders.TIME + " &a➢ &e" + Placeholders.LIMIT);

	@Override
	public Component getAllTime(int size) {
		return Text.of(allTime).replace(Placeholders.VALUE, size).get();
	}

	@Override
	public Component getAllTimeTarget(String player, int size) {
		return Text.of(allTimeTarget).replace(Placeholders.PLAYER, player).replace(Placeholders.VALUE, size).get();
	}

	@Override
	public Component getTitle(String player) {
		return Text.of(title).replace(Placeholders.PLAYER, player).get();
	}

	@Override
	public Component getReason(Component reason) {
		return Text.of(this.reason).replace(Placeholders.VALUE, reason).get();
	}

	@Override
	public Component getTimes(Component created, Component expire) {
		return Text.of(times).replace(Placeholders.TIME, created).replace(Placeholders.LIMIT, expire).get();
	}

}
