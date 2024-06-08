package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat;

import net.kyori.adventure.text.Component;

public interface AboutMod {

	Component getTitle();

	Component getId(String value);

	Component getName(String value);

	Component getVersion(String value);

	Component getDescription(String value);

	Component getDependencies(Component value);

	Component getLinks(Component source, Component issues);

}
