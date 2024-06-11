package sawfowl.commandpack.configure.locale.locales.def;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.Comments;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.CommandsConfig;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.JoinCommands;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.MainConfig;
import sawfowl.commandpack.configure.locale.locales.def.comments.ImplementCommandsConfig;
import sawfowl.commandpack.configure.locale.locales.def.comments.ImplementJoinCommands;
import sawfowl.commandpack.configure.locale.locales.def.comments.ImplementMainConfig;

@ConfigSerializable
public class ImplementComments implements Comments {

	public ImplementComments() {}

	@Setting("CommandsConfig")
	private ImplementCommandsConfig commandsConfig = new ImplementCommandsConfig();
	@Setting("MainConfig")
	private ImplementMainConfig mainConfig = new ImplementMainConfig();
	@Setting("JoinCommands")
	private ImplementJoinCommands joinCommands = new ImplementJoinCommands();

	@Override
	public CommandsConfig getCommandsConfig() {
		return commandsConfig;
	}

	@Override
	public JoinCommands getJoinCommands() {
		return joinCommands;
	}

	@Override
	public MainConfig getMainConfig() {
		return mainConfig;
	}

}
