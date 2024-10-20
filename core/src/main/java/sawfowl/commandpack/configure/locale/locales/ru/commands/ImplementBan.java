package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.ban.Ban.Profile;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Ban;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementBan implements Ban {

	public ImplementBan() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aВы забанили &e" + Placeholders.PLAYER + "&a.");
	@Setting("Announcement")
	private Component announcement = TextUtils.deserializeLegacy("&e" + Placeholders.SOURCE + "&a банит игрока &e" + Placeholders.PLAYER + "&a.\n&aОкончание бана &e" + Placeholders.TIME + "\n&aПричина: " + Placeholders.VALUE);
	@Setting("AnnouncementPermanent")
	private Component announcementPermanent = TextUtils.deserializeLegacy("&e" + Placeholders.SOURCE + "&a перманентно банит &e" + Placeholders.PLAYER + "&a.\n&aПричина: " + Placeholders.VALUE);
	@Setting("Ignore")
	private Component ignore = TextUtils.deserializeLegacy("&cВы не можете забанить &e" + Placeholders.PLAYER + "&c.");
	@Setting("AlreadyBanned")
	private Component alreadyBanned = TextUtils.deserializeLegacy("&eИгрок &e" + Placeholders.PLAYER + "&e уже забанен.");
	@Setting("Disconnect")
	private Component disconnect = TextUtils.deserializeLegacy("&bВы забанены на сервере.\n&bБан выдал &e" + Placeholders.SOURCE + "\n&bПричина: &e" + Placeholders.VALUE + "&a.\n&bОкончание бана &e" + Placeholders.TIME);
	@Setting("DisconnectPermanent")
	private Component disconnectPermanent = TextUtils.deserializeLegacy("&bВы перманентно забанены на сервере.\n&bBy &e" + Placeholders.SOURCE + "\n&bПричина: &e" + Placeholders.VALUE);

	@Override
	public Component getSuccess(User player) {
		return Text.of(success).replace(Placeholders.PLAYER, player.name()).get();
	}

	@Override
	public Component getAnnouncement(Component source, Component expire, Profile ban) {
		return Text.of(announcement).replace(Placeholders.SOURCE, source).replace(Placeholders.PLAYER, ban.profile().name().orElse(ban.profile().examinableName())).replace(Placeholders.TIME, expire).replace(Placeholders.VALUE, ban.reason().orElse(Component.empty())).get();
	}

	@Override
	public Component getAnnouncementPermanent(Component source, Profile ban) {
		return Text.of(announcement).replace(Placeholders.SOURCE, source).replace(Placeholders.PLAYER, ban.profile().name().orElse(ban.profile().examinableName())).replace(Placeholders.VALUE, ban.reason().orElse(Component.empty())).get();
	}

	@Override
	public Component getIgnore(User player) {
		return Text.of(ignore).replace(Placeholders.PLAYER, player.name()).get();
	}

	@Override
	public Component getAlreadyBanned(User player) {
		return Text.of(alreadyBanned).replace(Placeholders.PLAYER, player.name()).get();
	}

	@Override
	public Component getDisconnect(Component source, Component reason) {
		return Text.of(disconnectPermanent).replace(Placeholders.SOURCE, source).replace(Placeholders.VALUE, reason).get();
	}

	@Override
	public Component getDisconnect(Component source, Component reason, Component expire) {
		return Text.of(disconnect).replace(Placeholders.SOURCE, source).replace(Placeholders.VALUE, reason).replace(Placeholders.TIME, expire).get();
	}

}
