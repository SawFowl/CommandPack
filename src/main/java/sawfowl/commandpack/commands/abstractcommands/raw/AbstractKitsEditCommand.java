package sawfowl.commandpack.commands.abstractcommands.raw;

import java.util.Optional;
import java.util.stream.Stream;

import org.spongepowered.api.command.CommandCause;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawCompleterSupplier;
import sawfowl.commandpack.api.commands.raw.arguments.RawResultSupplier;
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
		return RawArgument.of(Kit.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(CommandCause cause, String[] args) {
				return plugin.getKitService().getKits().stream().map(Kit::id);
			}
		}, new RawResultSupplier<Kit>() {
			@Override
			public Optional<Kit> get(CommandCause cause, String[] args) {
				return args.length >= cursor + 1 && plugin.getKitService().kitExist(args[0]) ? plugin.getKitService().getKit(args[0]) : Optional.empty();
			}
		}, optional, optionalForConsole, 0, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
	}

	protected Optional<Kit> getKit(String[] args, int cursor) {
		return getArgument(Kit.class, args, cursor);
	}

}
