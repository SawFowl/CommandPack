package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;

public interface MuteList {

	Component getEmpty();

	Component getTitle(int size);

	Component getElement(String value);

	Component getInfo(String player, Component source, String created, Component expire, Component reason);

}
