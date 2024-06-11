package sawfowl.commandpack.configure.locale.locales.ru;

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
	private Component onlyPlayer = TextUtils.deserializeLegacy("&cЭта команда может быть выполнена только игроком.");
	@Setting("PlayerIsOffline")
	private Component playerIsOffline = TextUtils.deserializeLegacy("&cИгрок " + Placeholders.PLAYER + "&c оффлайн.");
	@Setting("UserNotPresent")
	private Component userNotPresent = TextUtils.deserializeLegacy("&cВам нужно указать ник игрока, который сейчас находится онлайн или ранее был на сервере.");
	@Setting("PlayerNotPresent")
	private Component playerNotPresent = TextUtils.deserializeLegacy("&cВам необходимо указать ник игрока онлайн.");
	@Setting("NameNotPresent")
	private Component nameNotPresent = TextUtils.deserializeLegacy("&cВы должны указать имя.");
	@Setting("TypeNotPresent")
	private Component typeNotPresent = TextUtils.deserializeLegacy("&cВы должны указать тип.");
	@Setting("ValueNotPresent")
	private Component valueNotPresent = TextUtils.deserializeLegacy("&cВы должны указать значение.");
	@Setting("DurationNotPresent")
	private Component durationNotPresent = TextUtils.deserializeLegacy("&cУкажите продолжительность в формате времени ISO.");
	@Setting("BooleanNotPresent")
	private Component booleanNotPresent = TextUtils.deserializeLegacy("&cНеобходимо указать аргумент как true или false");
	@Setting("LocationNotPresent")
	private Component locationNotPresent = TextUtils.deserializeLegacy("&cВам необходимо указать координаты.");
	@Setting("WorldNotPresent")
	private Component worldNotPresent = TextUtils.deserializeLegacy("&cВы должны указать мир.");
	@Setting("KitNotPresent")
	private Component kitNotPresent = TextUtils.deserializeLegacy("&cВы должны указать набор.");
	@Setting("WarpNotPresent")
	private Component warpNotPresent = TextUtils.deserializeLegacy("&cВы должны указать варп.");
	@Setting("PluginNotPresent")
	private Component pluginNotPresent = TextUtils.deserializeLegacy("&cВы должны указать плагин.");
	@Setting("ModNotPresent")
	private Component modNotPresent = TextUtils.deserializeLegacy("&cВы должны указать мод.");
	@Setting("MessageNotPresent")
	private Component messageNotPresent = TextUtils.deserializeLegacy("&cВы не ввели сообщение.");
	@Setting("ReasonNotPresent")
	private Component reasonNotPresent = TextUtils.deserializeLegacy("&cВам необходимо указать причину.");
	@Setting("LocaleNotPresent")
	private Component localeNotPresent = TextUtils.deserializeLegacy("&cВы должны указать локаль.");
	@Setting("CurrencyNotPresent")
	private Component сurrencyNotPresent = TextUtils.deserializeLegacy("&cВы должны указать валюту.");
	@Setting("TargetSelf")
	private Component targetSelf = TextUtils.deserializeLegacy("&cВы не можете указывать на себя.");
	@Setting("Cooldown")
	private Component cooldown = TextUtils.deserializeLegacy("&cПодождите &e" + Placeholders.DELAY + "&c перед повторным использованием этой команды.");
	@Setting("Wait")
	private Component wait = TextUtils.deserializeLegacy("&aАктивация команды через &e" + Placeholders.DELAY + "&a.");

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
