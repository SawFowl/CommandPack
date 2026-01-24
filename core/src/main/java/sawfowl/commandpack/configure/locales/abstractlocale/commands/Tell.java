package sawfowl.commandpack.configure.locales.abstractlocale.commands;

import net.kyori.adventure.text.Component;

public interface Tell {

	Component getSuccess(Component target, Component message);

	Component getSuccessTarget(Component source, Component message);

}
