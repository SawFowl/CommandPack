package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;

public interface Warps {

	Component getEmpty();

	Component getList(int size, Component list);

	Component getWait();

	Component getHeader(Component server, Component player);

	Component getServer();

	Component getPlayer();

}
