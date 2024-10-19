package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;

public interface Home {

	Component getNotFound(String home);

	Component getNotSet();

	Component getError();

	Component getSuccess(Component home);

	Component getListTitle();

}
