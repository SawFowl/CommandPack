package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.ban.Ban.Profile;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.BanIP;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementBanIP implements BanIP {

	public ImplementBanIP() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aYou banned &e" + Placeholders.PLAYER + "&a and his IP &e" + Placeholders.VALUE + "&a.");
	@Setting("Announcement")
	private Component announcement = TextUtils.deserializeLegacy("&e" + Placeholders.SOURCE + "&a bans player &e" + Placeholders.PLAYER + "&a by IP.\n&aEnd of ban &e" + Placeholders.TIME + "&a.\n&aReason: " + Placeholders.VALUE);
	@Setting("AnnouncementPermanent")
	private Component announcementPermanent = TextUtils.deserializeLegacy("&e" + Placeholders.SOURCE + "&a permanently bans player &e" + Placeholders.PLAYER + "&a by IP.\n&aReason: " + Placeholders.VALUE);
	@Setting("Disconnect")
	private Component disconnect = TextUtils.deserializeLegacy("&bYour IP is banned on the server.\n&bBy &e" + Placeholders.SOURCE + "\n&bReason: &e" + Placeholders.VALUE);

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
	public Component getDisconnect(Component source, Component reason) {
		return Text.of(disconnect).replace(Placeholders.SOURCE, source).replace(Placeholders.VALUE, reason).get();
	}

}
