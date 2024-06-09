package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;

public interface Warns {

	Component getAllTime(int size);

	Component getAllTimeTarget(String player, int size);

	Component getTitle(String player);

	Component getReason(Component reason);

	Component getTimes(Component created, Component expire);

}
