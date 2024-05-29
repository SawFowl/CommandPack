package sawfowl.commandpack.commands.abstractcommands.raw;

import org.spongepowered.api.world.server.ServerWorld;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public abstract class AbstractWorldCommand extends AbstractRawCommand {

	public AbstractWorldCommand(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public String permission() {
		return Permissions.WORLD_STAFF;
	}

	@Override
	public String trackingName() {
		return "world";
	}

	protected RawArgument<ServerWorld> createWorldArg() {
		return RawArguments.createWorldArgument(false, false, 0, null, null, null, null, createComponentSupplier(LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT));
	}

	@Override
	public Settings applyCommandSettings() {
		return null;
	}

}
