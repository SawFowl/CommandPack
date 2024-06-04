package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;

public interface MuteList {

	Component getEmpty();

	Component getTitle(int size);

	Component getElement(Component value);

	Component getInfo(String player, Component source, String created, String expire, Component reason);

}
