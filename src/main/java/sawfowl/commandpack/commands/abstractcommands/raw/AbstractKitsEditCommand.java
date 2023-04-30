package sawfowl.commandpack.commands.abstractcommands.raw;

import java.util.Optional;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.kits.Kit;

public abstract class AbstractKitsEditCommand extends AbstractPlayerCommand {

	public AbstractKitsEditCommand(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public String permission() {
		return Permissions.KIT_STAFF;
	}

	protected Optional<Kit> getKit(String[] args) {
		return args.length == 0 ? Optional.empty() : plugin.getKitService().getKit(args[0]);
	}

}
