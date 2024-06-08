package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.ban.Ban.Profile;

import net.kyori.adventure.text.Component;

public interface Ban {

	Component getSuccess(User player);

	Component getAnnouncement(Component source, Component expire, Profile ban);

	Component getAnnouncementPermanent(Component source, Profile ban);

	Component getIgnore(User player);

	Component getAlreadyBanned(User player);

	Component getDisconnect(Component source, Component reason);

	Component getDisconnect(Component source, Component reason, Component expire);

	default Component getDisconnect(boolean permanent, Component source, Component reason, Component expire) {
		return permanent ? getDisconnect(source, reason) : getDisconnect(source, reason, expire);
	}

	default Component getAnnouncement(boolean permanent, Component source, Component expire, Profile ban) {
		return permanent ? getAnnouncementPermanent(source, ban) : getAnnouncement(source, expire, ban);
	}

}
