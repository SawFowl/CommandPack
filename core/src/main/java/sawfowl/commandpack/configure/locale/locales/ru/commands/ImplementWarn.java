package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Warn;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementWarn implements Warn {

	public ImplementWarn() {}

	@Setting("Success")
	private Component success = TextUtils.deserialize("&aYou warned the player &e" + Placeholders.PLAYER + "&a.");
	@Setting("SuccessTarget")
	private Component successTarget = TextUtils.deserialize("&e" + Placeholders.SOURCE + "&a warned you.\nReason: &e\"" + Placeholders.VALUE + "&e\"&a.\nValid until &e" + Placeholders.TIME);
	@Setting("SuccessTargetPermanent")
	private Component successTargetPermanent = TextUtils.deserialize("&e" + Placeholders.SOURCE + "&a has issued a permanent warning to you.\nReason:  &e\"" + Placeholders.VALUE + "&e\"&a.");
	@Setting("Announcement")
	private Component announcement = TextUtils.deserialize("&e" + Placeholders.SOURCE + "&a has warned player &e" + Placeholders.PLAYER + "&a.\nValid until &e" + Placeholders.TIME + "&a.\n&aReason: " + Placeholders.VALUE);
	@Setting("AnnouncementPermanent")
	private Component announcementPermanent = TextUtils.deserialize("&e" + Placeholders.SOURCE + "&a issues a permanent warning to player &e" + Placeholders.PLAYER + "&a.\n&aReason: " + Placeholders.VALUE);
	@Setting("Ignore")
	private Component ignore = TextUtils.deserialize("&cIt is not possible to give a warning to &e" + Placeholders.PLAYER + "&c.");
	@Setting("Autopunish")
	private Component autopunish = TextUtils.deserialize("&4Automatic issuance of punishments");
	@Setting("BanLimit")
	private Component banLimit = TextUtils.deserialize("&cWarning limit reached before ban &e" + Placeholders.VALUE + "&c");
	@Setting("MuteLimit")
	private Component muteLimit = TextUtils.deserialize("&cWarning limit reached before mute &e" + Placeholders.VALUE + "&c");
	@Setting("KickLimit")
	private Component kickLimit = TextUtils.deserialize("&cWarning limit reached before kick &e" + Placeholders.VALUE + "&c");

	@Override
	public Component getSuccess(String player) {
		return Text.of(success).replace(Placeholders.PLAYER, player).get();
	}

	@Override
	public Component getSuccessTarget(Component source, Component reason, String time) {
		return Text.of(successTarget).replace(Placeholders.SOURCE, source).replace(Placeholders.VALUE, reason).replace(Placeholders.TIME, time).get();
	}

	@Override
	public Component getSuccessTargetPermanent(Component source, Component reason) {
		return Text.of(successTargetPermanent).replace(Placeholders.SOURCE, source).replace(Placeholders.VALUE, reason).get();
	}

	@Override
	public Component getAnnouncement(Component source, String player, Component reason, String time) {
		return Text.of(announcement).replace(Placeholders.SOURCE, source).replace(Placeholders.PLAYER, player).replace(Placeholders.VALUE, reason).replace(Placeholders.TIME, time).get();
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
	public Component getAutopunish() {
		return Text.of(autopunish).get();
	}

	@Override
	public Component getBanLimit(int limit) {
		return Text.of(banLimit).replace(Placeholders.VALUE, limit).get();
	}

	@Override
	public Component getMuteLimit(int limit) {
		return Text.of(muteLimit).replace(Placeholders.VALUE, limit).get();
	}

	@Override
	public Component getKickLimit(int limit) {
		return Text.of(kickLimit).replace(Placeholders.VALUE, limit).get();
	}

}
