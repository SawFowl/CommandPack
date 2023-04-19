package sawfowl.commandpack.commands.abstractcommands.raw;

import sawfowl.commandpack.CommandPackPlugin;
import sawfowl.commandpack.api.data.commands.raw.RawPlayerCommand;

public abstract class AbstractPlayerCommand extends AbstractRawCommand implements RawPlayerCommand {

	public AbstractPlayerCommand(CommandPackPlugin plugin) {
		super(plugin);
	}

}
