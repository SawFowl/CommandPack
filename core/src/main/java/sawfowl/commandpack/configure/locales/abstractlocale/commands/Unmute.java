package sawfowl.commandpack.configure.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;

public interface Unmute {

	Component getSuccess(String player);

	Component getSuccessTarget();

	Component getAnnouncement(Component source, String player);

}
