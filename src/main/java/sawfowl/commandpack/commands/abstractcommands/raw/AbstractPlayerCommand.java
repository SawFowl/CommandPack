package sawfowl.commandpack.commands.abstractcommands.raw;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawPlayerCommand;

public abstract class AbstractPlayerCommand extends AbstractRawCommand implements RawPlayerCommand {

	public AbstractPlayerCommand(CommandPack plugin) {
		super(plugin);
	}

}
