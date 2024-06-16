package sawfowl.commandpack.commands.abstractcommands.raw;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.commands.raw.RawPlayerCommand;

public abstract class AbstractPlayerCommand extends AbstractRawCommand implements RawPlayerCommand {

	public AbstractPlayerCommand(CommandPackInstance plugin) {
		super(plugin);
	}

}
