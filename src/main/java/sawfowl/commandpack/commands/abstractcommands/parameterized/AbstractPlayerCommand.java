package sawfowl.commandpack.commands.abstractcommands.parameterized;

import sawfowl.commandpack.CommandPackPlugin;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterizedPlayerCommand;

public abstract class AbstractPlayerCommand extends AbstractParameterizedCommand implements ParameterizedPlayerCommand {

	public AbstractPlayerCommand(CommandPackPlugin plugin) {
		super(plugin);
	}

}
