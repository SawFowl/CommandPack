package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Speed;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementSpeed implements Speed {

	public ImplementSpeed() {}

	@Setting("SetSelf")
	private Component setSelf = TextUtils.deserializeLegacy("&aYou have set your speed multiplier: &e" + Placeholders.VALUE + "&a.");
	@Setting("SetByStaff")
	private Component setByStaff = TextUtils.deserializeLegacy("&aYou have set a player speed multiplier &e" + Placeholders.PLAYER + "&a: &e" + Placeholders.VALUE + "&a.");
	@Setting("SetOther")
	private Component setOther = TextUtils.deserializeLegacy("&aYou have been set a multiplier for your speed: &e" + Placeholders.VALUE + "&a.");
	@Setting("InFly")
	private Component inFly = TextUtils.deserializeLegacy("\n&aThe speed has been changed for flight mode because of being in the air.");
	@Setting("SetDefault")
	private Component setDefault = TextUtils.deserializeLegacy("ruault");

	@Override
	public Component getSetSelf(double value) {
		return Text.of(setSelf).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getSetByStaff(ServerPlayer player, double value) {
		return Text.of(setByStaff).replace(Placeholders.PLAYER, player.name()).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getSetOther(double value) {
		return Text.of(setOther).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getInFly() {
		return inFly;
	}

	@Override
	public Component getSetDefault() {
		return setDefault;
	}

}
