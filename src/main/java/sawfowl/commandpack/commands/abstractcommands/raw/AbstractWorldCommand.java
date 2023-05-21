package sawfowl.commandpack.commands.abstractcommands.raw;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;

public abstract class AbstractWorldCommand extends AbstractRawCommand {

	public AbstractWorldCommand(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public String permission() {
		return Permissions.WORLD;
	}

	@Override
	public String trackingName() {
		return "world";
	}

}
