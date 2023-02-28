package sawfowl.commandpack.configure.configs;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class CommandsConfig {

	public CommandsConfig() {}

	@Setting("Suicide")
	private Command suicide = new Command();

	public Command getCommandConfig(String command) {
		switch (command.toLowerCase()) {
		case "suicide":
			return suicide;
		default:
			return Command.EMPTY;
		}
	}

}
