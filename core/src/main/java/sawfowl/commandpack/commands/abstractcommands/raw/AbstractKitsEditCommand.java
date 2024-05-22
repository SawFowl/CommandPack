package sawfowl.commandpack.commands.abstractcommands.raw;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public abstract class AbstractKitsEditCommand extends AbstractPlayerCommand {

	public AbstractKitsEditCommand(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public String permission() {
		return Permissions.KIT_STAFF;
	}

	protected RawArgument<Kit> kitArgument(int cursor, boolean optional, boolean optionalForConsole) {
		return RawArguments.createKitArgument(optional, optionalForConsole, cursor, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
	}

}
