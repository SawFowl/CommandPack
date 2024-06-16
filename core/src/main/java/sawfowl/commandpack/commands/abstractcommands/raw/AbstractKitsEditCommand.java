package sawfowl.commandpack.commands.abstractcommands.raw;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawOptional;
import sawfowl.commandpack.api.data.kits.Kit;

public abstract class AbstractKitsEditCommand extends AbstractPlayerCommand {

	public AbstractKitsEditCommand(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public String permission() {
		return Permissions.KIT_STAFF;
	}

	protected RawArgument<Kit> kitArgument(int cursor, boolean optional, boolean optionalForConsole) {
		return RawArguments.createKitArgument(RawBasicArgumentData.createKit(cursor, null, null), new RawOptional(optional, optionalForConsole), locale -> plugin.getLocales().getLocale(locale).getCommandExceptions().getKitNotPresent());
	}

}
