package sawfowl.commandpack.commands.abstractcommands.parameterized;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterizedPlayerCommand;

public abstract class AbstractPlayerCommand extends AbstractParameterizedCommand implements ParameterizedPlayerCommand {

	public AbstractPlayerCommand(CommandPack plugin) {
		super(plugin);
	}

}
