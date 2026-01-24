package sawfowl.commandpack.configure.locales.abstractlocale.commands;

import java.util.List;

import net.kyori.adventure.text.Component;

public interface Help {

	Component getTitle();

	List<Component> getList();

}
