package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;

public interface HideBalance {

	Component getHide();

	Component getOpen();

	Component getHideStaff(String player);

	Component getOpenStaff(String player);

}
