package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Nick;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementNick implements Nick {

	public ImplementNick() {}

	@Setting("Set")
	private Component set = TextUtils.deserializeLegacy("&aYour nickname has been changed to &e" + Placeholders.VALUE + "&a.");
	@Setting("Clear")
	private Component clear = TextUtils.deserializeLegacy("&aYour nickname has been changed to the original.");
	@Setting("SetStaff")
	private Component setStaff = TextUtils.deserializeLegacy("&aYou have changed the nickname of the player &e" + Placeholders.PLAYER + "&a to &e" + Placeholders.VALUE + "&a.");
	@Setting("ClearStaff")
	private Component clearStaff = TextUtils.deserializeLegacy("&aYou restored the original nickname of player &e" + Placeholders.PLAYER + "&a.");

	@Override
	public Component getSet(Component value) {
		return Text.of(set).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getClear() {
		return clear;
	}

	@Override
	public Component getSetStaff(ServerPlayer player, Component value) {
		return Text.of(setStaff).replace(Placeholders.PLAYER, player.name()).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getClearStaff(ServerPlayer player) {
		return Text.of(clearStaff).replace(Placeholders.PLAYER, player.name()).get();
	}

}
