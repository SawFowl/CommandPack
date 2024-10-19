package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.serverstat;

import net.kyori.adventure.text.Component;

import sawfowl.localeapi.api.Text;

public interface Buttons {

	Component getSystem();

	Component getWorlds();

	Component getPlugins();

	Component getMods();

	Text getRefreshPlugin();

}
