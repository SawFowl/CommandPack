package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;

public interface Mute {

	Component getSuccess(String player);

	Component getSuccessTarget(Component source, Component time, Component reason);

	Component getSuccessTargetPermanent(Component source, Component reason);

	Component getAnnouncement(Component source, String player, Component time, Component reason);

	Component getAnnouncementPermanent(Component source, String player, Component reason);

	Component getIgnore(String player);

	Component getAlreadyMuted(String player);

	default Component getSuccessTarget(boolean permanent, Component source, Component time, Component reason) {
		return permanent ? getSuccessTargetPermanent(source, reason) : getSuccessTarget(source, time, reason);
	}

	default Component getAnnouncement(boolean permanent, Component source, String player, Component time, Component reason) {
		return permanent ? getAnnouncementPermanent(source, player, reason) : getAnnouncement(source, player, time, reason);
	}

}
