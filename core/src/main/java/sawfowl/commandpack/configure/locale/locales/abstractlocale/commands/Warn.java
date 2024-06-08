package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;

public interface Warn {

	Component getSuccess(String player);

	Component getSuccessTarget(Component source, Component reason, Component time);

	Component getSuccessTargetPermanent(Component source, Component reason);

	Component getAnnouncement(Component source, String player, Component reason, Component time);

	Component getAnnouncementPermanent(Component source, String player, Component reason);

	Component getIgnore(String player);

	Component getAutopunish();

	Component getBanLimit(int limit);

	Component getMuteLimit(int limit);

	Component getKickLimit(int limit);

	default Component getAnnouncement(boolean permanent, Component source, String player, Component reason, Component time) {
		return permanent ? getAnnouncementPermanent(source, player, reason) : getAnnouncement(source, player, reason, time);
	}

	default Component getSuccessTarget(boolean permanent, Component source, Component reason, Component time) {
		return permanent ? getSuccessTargetPermanent(source, reason) : getSuccessTarget(source, reason, time);
	}

}
