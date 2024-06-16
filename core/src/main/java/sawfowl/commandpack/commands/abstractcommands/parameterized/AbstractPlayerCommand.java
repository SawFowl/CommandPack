package sawfowl.commandpack.commands.abstractcommands.parameterized;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.commands.parameterized.ParameterizedPlayerCommand;

public abstract class AbstractPlayerCommand extends AbstractParameterizedCommand implements ParameterizedPlayerCommand {

	public AbstractPlayerCommand(CommandPackInstance plugin) {
		super(plugin);
	}

}
