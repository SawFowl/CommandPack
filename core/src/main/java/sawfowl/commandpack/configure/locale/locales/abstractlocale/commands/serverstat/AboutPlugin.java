package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat;

import net.kyori.adventure.text.Component;

public interface AboutPlugin {

	Component getTitle();

	Component getId(String value);

	Component getName(String value);

	Component getVersion(String value);

	Component getEntrypoint(String value);

	Component getDescription(String value);

	Component getDependencies(Component value);

	Component getContributors(Component value);

	Component getLinks(Component home, Component source, Component issues);

}
