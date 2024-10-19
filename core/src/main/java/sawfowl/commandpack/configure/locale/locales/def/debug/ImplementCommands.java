package sawfowl.commandpack.configure.locale.locales.def.debug;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Debug.Commands;

@ConfigSerializable
public class ImplementCommands implements Commands {

	public ImplementCommands() {}

	@Setting("Executors")
	private ImplementExecutors executors = new ImplementExecutors();
	@Setting("Log")
	private String log = Placeholders.SOURCE + " ran the command: /" + Placeholders.COMMAND + " " + Placeholders.ARGS;
	@Setting("NotTracking")
	private String notTracking = "Command \"" + Placeholders.COMMAND + "\" is not registered for tracking.";

	@Override
	public Executors getExecutors() {
		return executors;
	}

	@Override
	public String getLog(String source, String command, String args) {
		return log.replace(Placeholders.SOURCE, source).replace(Placeholders.COMMAND, command).replace(Placeholders.ARGS, args);
	}

	@Override
	public String getNotTracking(String command) {
		return notTracking.replace(Placeholders.COMMAND, command);
	}

}
