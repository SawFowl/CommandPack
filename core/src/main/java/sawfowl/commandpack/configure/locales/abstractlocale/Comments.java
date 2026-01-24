package sawfowl.commandpack.configure.locales.abstractlocale;

import sawfowl.commandpack.configure.locales.abstractlocale.comments.CommandsConfig;
import sawfowl.commandpack.configure.locales.abstractlocale.comments.JoinCommands;
import sawfowl.commandpack.configure.locales.abstractlocale.comments.MainConfig;

public interface Comments {

	CommandsConfig getCommandsConfig();

	JoinCommands getJoinCommands();

	MainConfig getMainConfig();

}
