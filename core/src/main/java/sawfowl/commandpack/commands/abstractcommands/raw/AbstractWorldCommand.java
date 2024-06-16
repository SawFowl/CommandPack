package sawfowl.commandpack.commands.abstractcommands.raw;

import java.util.Locale;

import org.spongepowered.api.world.server.ServerWorld;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.World;

public abstract class AbstractWorldCommand extends AbstractRawCommand {

	public AbstractWorldCommand(CommandPackInstance plugin) {
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
		return RawArguments.createWorldArgument(RawBasicArgumentData.createWorld(null, 0, null, null), null, locale -> plugin.getLocales().getLocale(locale).getCommandExceptions().getWorldNotPresent());
	}

	@Override
	public Settings applyCommandSettings() {
		return null;
	}

	protected World getWorld(Locale locale) {
		return getCommands(locale).getWorld();
	}

}
