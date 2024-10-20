package sawfowl.commandpack.configure.locale.locales.def;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.CommandExceptions;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementCommandExceptions implements CommandExceptions {

	public ImplementCommandExceptions() {}

	@Setting("OnlyPlayer")
	private Component onlyPlayer = TextUtils.deserializeLegacy("&cThis command can only be executed by the player.");
	@Setting("PlayerIsOffline")
	private Component playerIsOffline = TextUtils.deserializeLegacy("&cPlayer " + Placeholders.PLAYER + "&c is offline.");
	@Setting("UserNotPresent")
	private Component userNotPresent = TextUtils.deserializeLegacy("&cYou need to specify the nickname of the player who is now online or was previously on the server.");
	@Setting("PlayerNotPresent")
	private Component playerNotPresent = TextUtils.deserializeLegacy("&cYou need to specify the nickname of the player online.");
	@Setting("NameNotPresent")
	private Component nameNotPresent = TextUtils.deserializeLegacy("&cYou must specify a name.");
	@Setting("TypeNotPresent")
	private Component typeNotPresent = TextUtils.deserializeLegacy("&cYou must specify a type.");
	@Setting("ValueNotPresent")
	private Component valueNotPresent = TextUtils.deserializeLegacy("&cYou must specify a value.");
	@Setting("DurationNotPresent")
	private Component durationNotPresent = TextUtils.deserializeLegacy("&cSpecify the duration in ISO time format.");
	@Setting("BooleanNotPresent")
	private Component booleanNotPresent = TextUtils.deserializeLegacy("&cIt is necessary to specify the command argument as boolean.");
	@Setting("LocationNotPresent")
	private Component locationNotPresent = TextUtils.deserializeLegacy("&cYou need to specify the coordinates.");
	@Setting("WorldNotPresent")
	private Component worldNotPresent = TextUtils.deserializeLegacy("&cYou must to specify the world.");
	@Setting("KitNotPresent")
	private Component kitNotPresent = TextUtils.deserializeLegacy("&cYou must to specify a kit.");
	@Setting("WarpNotPresent")
	private Component warpNotPresent = TextUtils.deserializeLegacy("&cYou must to specify warp.");
	@Setting("PluginNotPresent")
	private Component pluginNotPresent = TextUtils.deserializeLegacy("&cYou must specify the plugin.");
	@Setting("ModNotPresent")
	private Component modNotPresent = TextUtils.deserializeLegacy("&cYou must to specify the mod.");
	@Setting("MessageNotPresent")
	private Component messageNotPresent = TextUtils.deserializeLegacy("&cYou must enter a message.");
	@Setting("ReasonNotPresent")
	private Component reasonNotPresent = TextUtils.deserializeLegacy("&cYou need to specify a reason.");
	@Setting("LocaleNotPresent")
	private Component localeNotPresent = TextUtils.deserializeLegacy("&cYou must specify the locale.");
	@Setting("CurrencyNotPresent")
	private Component сurrencyNotPresent = TextUtils.deserializeLegacy("&cYou must specify the currency.");
	@Setting("TargetSelf")
	private Component targetSelf = TextUtils.deserializeLegacy("&cYou can't point to yourself.");
	@Setting("Cooldown")
	private Component cooldown = TextUtils.deserializeLegacy("&cWait &e" + Placeholders.DELAY + "&c before using this command again.");
	@Setting("Wait")
	private Component wait = TextUtils.deserializeLegacy("&aCommand activation via &e" + Placeholders.DELAY + "&a.");

	@Override
	public Component getOnlyPlayer() {
		return onlyPlayer;
	}

	@Override
	public Component getPlayerIsOffline(String player) {
		return Text.of(playerIsOffline).replace(Placeholders.PLAYER, player).get();
	}

	@Override
	public Component getUserNotPresent() {
		return userNotPresent;
	}

	@Override
	public Component getPlayerNotPresent() {
		return playerNotPresent;
	}

	@Override
	public Component getNameNotPresent() {
		return nameNotPresent;
	}

	@Override
	public Component getTypeNotPresent() {
		return typeNotPresent;
	}

	@Override
	public Component getValueNotPresent() {
		return valueNotPresent;
	}

	@Override
	public Component getDurationNotPresent() {
		return durationNotPresent;
	}

	@Override
	public Component getBooleanNotPresent() {
		return booleanNotPresent;
	}

	@Override
	public Component getLocationNotPresent() {
		return locationNotPresent;
	}

	@Override
	public Component getWorldNotPresent() {
		return worldNotPresent;
	}

	@Override
	public Component getTargetSelf() {
		return targetSelf;
	}

	@Override
	public Component getKitNotPresent() {
		return kitNotPresent;
	}

	@Override
	public Component getWarpNotPresent() {
		return warpNotPresent;
	}

	@Override
	public Component getPluginNotPresent() {
		return pluginNotPresent;
	}

	@Override
	public Component getModNotPresent() {
		return modNotPresent;
	}

	@Override
	public Component getMessageNotPresent() {
		return messageNotPresent;
	}

	@Override
	public Component getReasonNotPresent() {
		return reasonNotPresent;
	}

	@Override
	public Component getLocaleNotPresent() {
		return localeNotPresent;
	}

	@Override
	public Component getCurrencyNotPresent() {
		return сurrencyNotPresent;
	}

	@Override
	public Component getCooldown(Component delay) {
		return Text.of(cooldown).replace(Placeholders.DELAY, delay).get();
	}

	@Override
	public Component getWait(Component delay) {
		return Text.of(wait).replace(Placeholders.DELAY, delay).get();
	}

}
