package sawfowl.commandpack.configure.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;

public interface Kick {

	Component getSuccess(String player);

	Component getDisconnect(Component source, Component reason);

	Component getAnnouncement(Component source, String player, Component reason);

	Component getIgnore(String player);

}
