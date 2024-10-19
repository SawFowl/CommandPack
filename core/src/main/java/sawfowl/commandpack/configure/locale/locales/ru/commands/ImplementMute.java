package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Mute;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementMute implements Mute {

	public ImplementMute() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aВы заблокировали чат игроку &e" + Placeholders.PLAYER + "&a.");
	@Setting("SuccessTarget")
	@Comment("Можно добавить плейсхолдер: " + Placeholders.SOURCE)
	private Component successTarget = TextUtils.deserializeLegacy("&cВам запрещено писать в чат до &e" + Placeholders.TIME + "&c.\nПричина: " + Placeholders.VALUE);
	@Setting("SuccessTargetPermanent")
	private Component successTargetPermanent = TextUtils.deserializeLegacy("&cВам навсегда запрещено писать в чате.\nПричина: " + Placeholders.VALUE);
	@Setting("Announcement")
	private Component announcement = TextUtils.deserializeLegacy("&e" + Placeholders.SOURCE + "&a блокирует чат игроку &e" + Placeholders.PLAYER + "&a.\nОкончание мута &e" + Placeholders.TIME + "&a.\n&aПричина: " + Placeholders.VALUE);
	@Setting("AnnouncementPermanent")
	private Component announcementPermanent = TextUtils.deserializeLegacy("&e" + Placeholders.SOURCE + "&a навсегда блокирует чат игроку &e" + Placeholders.PLAYER + "&a.\n&aПричина: " + Placeholders.VALUE);
	@Setting("Ignore")
	private Component ignore = TextUtils.deserializeLegacy("&cВы не можете заблокировать чат &e" + Placeholders.PLAYER + "&c.");
	@Setting("AlreadyMuted")
	private Component alreadyMuted = TextUtils.deserializeLegacy("&cИгрок &e" + Placeholders.PLAYER + "&c уже не может писать в чат.");

	@Override
	public Component getSuccess(String player) {
		return Text.of(success).replace(Placeholders.PLAYER, player).get();
	}

	@Override
	public Component getSuccessTarget(Component source, Component time, Component reason) {
		return Text.of(successTarget).replace(Placeholders.SOURCE, source).replace(Placeholders.TIME, time).replace(Placeholders.VALUE, reason).get();
	}

	@Override
	public Component getSuccessTargetPermanent(Component source, Component reason) {
		return Text.of(successTargetPermanent).replace(Placeholders.SOURCE, source).replace(Placeholders.VALUE, reason).get();
	}

	@Override
	public Component getAnnouncement(Component source, String player, Component time, Component reason) {
		return Text.of(announcement).replace(Placeholders.SOURCE, source).replace(Placeholders.PLAYER, player).replace(Placeholders.TIME, time).replace(Placeholders.VALUE, reason).get();
	}

	@Override
	public Component getAnnouncementPermanent(Component source, String player, Component reason) {
		return Text.of(announcementPermanent).replace(Placeholders.SOURCE, source).replace(Placeholders.PLAYER, player).replace(Placeholders.VALUE, reason).get();
	}

	@Override
	public Component getIgnore(String player) {
		return Text.of(ignore).replace(Placeholders.PLAYER, player).get();
	}

	@Override
	public Component getAlreadyMuted(String player) {
		return Text.of(alreadyMuted).replace(Placeholders.PLAYER, player).get();
	}

}
