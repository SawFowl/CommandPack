package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;

public interface Item {

	Component getSetName(Component value);

	Component getClearName();

	Component getEmptyHand();

	Component getSetLore();

	Component getClearLore();

}
