package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Seen;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementSeen implements Seen {

	public ImplementSeen() {}

	@Setting("Title")
	private Component title = TextUtils.deserializeLegacy("&3Информация об игроке&f: " + Placeholders.PLAYER);
	@Setting("Online")
	private Component online = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + "&a сейчас &2онлайн");
	@Setting("Offline")
	private Component offline = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + "&a сейчас &4офлайн");
	@Setting("OnlineTime")
	private Component onlineTime = TextUtils.deserializeLegacy("&aВремя в онлайне&f: &e" + Placeholders.VALUE);
	@Setting("LastOnline")
	private Component lastOnline = TextUtils.deserializeLegacy("&aБыл(а) в сети&f: &e" + Placeholders.VALUE + "&a назад");
	@Setting("DisplayName")
	private Component displayName = TextUtils.deserializeLegacy("&aОтображаемое имя&f: &e" + Placeholders.VALUE);
	@Setting("UUID")
	private Component uuid = TextUtils.deserializeLegacy("&aUUID&f: &e" + Placeholders.VALUE);
	@Setting("IP")
	private Component ip = TextUtils.deserializeLegacy("&aIP&f: &e" + Placeholders.VALUE);
	@Setting("FirstPlayed")
	private Component firstPlayed = TextUtils.deserializeLegacy("&aПервый вход&f: " + Placeholders.VALUE);
	@Setting("WalkingSpeed")
	private Component walkingSpeed = TextUtils.deserializeLegacy("&aСкорость хотьбы&f: " + Placeholders.VALUE);
	@Setting("FlyingSpeed")
	private Component flyingSpeed = TextUtils.deserializeLegacy("&aСкорость полета&f: " + Placeholders.VALUE);
	@Setting("CurrentLocation")
	private Component currentLocation = TextUtils.deserializeLegacy("&aТекущая локация&f: " + Placeholders.VALUE);
	@Setting("CanFly")
	private Component canFly = TextUtils.deserializeLegacy("&aМожет летать&f: " + Placeholders.VALUE);
	@Setting("IsFlying")
	private Component isFlying = TextUtils.deserializeLegacy("&aЛетает&f: " + Placeholders.VALUE);
	@Setting("GameMode")
	private Component gameMode = TextUtils.deserializeLegacy("&aРежим игры&f: " + Placeholders.VALUE);
	@Setting("Vanished")
	private Component vanished = TextUtils.deserializeLegacy("&aНевидимость&f: " + Placeholders.VALUE);
	@Setting("Invulnerable")
	private Component invulnerable = TextUtils.deserializeLegacy("&aНеуязвимость&f: " + Placeholders.VALUE);
	@Setting("AFK")
	private Component afk = TextUtils.deserializeLegacy("&aAFK&f: " + Placeholders.VALUE);
	@Setting("Ban")
	private Component ban = TextUtils.deserializeLegacy("&aБан&f: " + Placeholders.VALUE);
	@Setting("Mute")
	private Component mute = TextUtils.deserializeLegacy("&aМут&f: " + Placeholders.VALUE);
	@Setting("Warns")
	private Component warns = TextUtils.deserializeLegacy("&aПредупреждения&f: " + Placeholders.VALUE);
	@Setting("Yes")
	private Component yes = TextUtils.deserializeLegacy("&eДа");
	@Setting("No")
	private Component no = TextUtils.deserializeLegacy("&eНет");
	@Setting("Padding")
	private Component padding = TextUtils.deserializeLegacy("&3=");

	@Override
	public Text getTitle() {
		return Text.of(title);
	}

	@Override
	public Text getOnline() {
		return Text.of(online);
	}

	@Override
	public Text getOffline() {
		return Text.of(offline);
	}

	@Override
	public Text getOnlineTime() {
		return Text.of(onlineTime);
	}

	@Override
	public Text getLastOnline() {
		return Text.of(lastOnline);
	}

	@Override
	public Text getDisplayName() {
		return Text.of(displayName);
	}

	@Override
	public Text getUUID() {
		return Text.of(uuid);
	}

	@Override
	public Text getIP() {
		return Text.of(ip);
	}

	@Override
	public Text getFirstPlayed() {
		return Text.of(firstPlayed);
	}

	@Override
	public Text getWalkingSpeed() {
		return Text.of(walkingSpeed);
	}

	@Override
	public Text getFlyingSpeed() {
		return Text.of(flyingSpeed);
	}

	@Override
	public Text getCurrentLocation() {
		return Text.of(currentLocation);
	}

	@Override
	public Text getCanFly() {
		return Text.of(canFly);
	}

	@Override
	public Text getIsFlying() {
		return Text.of(isFlying);
	}

	@Override
	public Text getGameMode() {
		return Text.of(gameMode);
	}

	@Override
	public Text getVanished() {
		return Text.of(vanished);
	}

	@Override
	public Text getInvulnerable() {
		return Text.of(invulnerable);
	}

	@Override
	public Text getAFK() {
		return Text.of(afk);
	}

	@Override
	public Text getBan() {
		return Text.of(ban);
	}

	@Override
	public Text getMute() {
		return Text.of(mute);
	}

	@Override
	public Text getWarns() {
		return Text.of(warns);
	}

	@Override
	public Component getYes() {
		return yes;
	}

	@Override
	public Component getNo() {
		return no;
	}

	@Override
	public Component getPadding() {
		return padding;
	}

}
