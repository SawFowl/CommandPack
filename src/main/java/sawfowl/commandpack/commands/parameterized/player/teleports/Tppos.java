package sawfowl.commandpack.commands.parameterized.player.teleports;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerLocation;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Tppos extends AbstractPlayerCommand {

	public Tppos(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		ServerLocation location = getLocation(context).get();
		delay(src, locale, consumer -> {
			if(!location.isValid()) exception(locale, LocalesPaths.COMMANDS_TPPOS_INVALID_LOCATION);
			plugin.getPlayersData().getTempData().setPreviousLocation(src);
			src.setLocation(location);
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createLocation(false), false, LocalesPaths.COMMANDS_EXCEPTION_LOCATION_NOT_PRESENT));
	}

	@Override
	public String permission() {
		return Permissions.TPPOS_STAFF;
	}

	@Override
	public String command() {
		return "tppos";
	}

}
