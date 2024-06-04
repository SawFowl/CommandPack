package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;

public interface Warn {

	Component getSuccess(String player);

	Component getSuccessTarget(Component source, Component reason, String time);

	Component getSuccessTargetPermanent(Component source, Component reason);

	Component getAnnouncement(Component source, String player, Component reason, String time);

	Component getAnnouncementPermanent(Component source, String player, Component reason);

	Component getIgnore(String player);

	Component getAutopunish();

	Component getBanLimit(int limit);

	Component getMuteLimit(int limit);

	Component getKickLimit(int limit);

}
