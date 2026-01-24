package sawfowl.commandpack.configure.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.locales.abstractlocale.commands.banlist.Info;
import sawfowl.localeapi.api.Text;

public interface Banlist {

	Info getInfo();

	Component getEmpty();

	Text getTitle();

	Component getProfile();

	Component getIP();

	Component getElement(String element);

}
