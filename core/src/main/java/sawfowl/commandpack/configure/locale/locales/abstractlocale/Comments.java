package sawfowl.commandpack.configure.locale.locales.abstractlocale;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.CommandsConfig;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.JoinCommands;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.MainConfig;

public interface Comments {

	CommandsConfig getCommandsConfig();

	JoinCommands getJoinCommands();

	MainConfig getMainConfig();

}
