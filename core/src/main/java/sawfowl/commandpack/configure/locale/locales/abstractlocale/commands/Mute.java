package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;

public interface Mute {

	Component getSuccess(String player);

	Component getSuccessTarget(Component time, Component reason);

	Component getSuccessTargetPermanent(Component reason);

	Component getAnnouncement(Component source, String player, Component time, Component reason);

	Component getAnnouncementPermanent(Component source, String player, Component reason);

	Component getIgnore(String player);

	Component getAlreadyMuted(String player);

}
