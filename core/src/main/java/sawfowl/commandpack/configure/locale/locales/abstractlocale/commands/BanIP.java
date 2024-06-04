package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.ban.Ban.Profile;

import net.kyori.adventure.text.Component;

public interface BanIP {

	Component getSuccess(User player);

	Component getAnnouncement(Component source, Component expire, Profile ban);

	Component getAnnouncementPermanent(Component source, Profile ban);

	Component getDisconnect(Component source, Component reason);

}
