package sawfowl.commandpack.commands.parameterized.player.teleports;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.parameterized.settings.CommandParameters;
import sawfowl.commandpack.commands.parameterized.settings.ParameterSettings;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Teleport extends AbstractPlayerCommand {

	public Teleport(CommandPack plugin, String command, CommandSettings commandSettings) {
		super(plugin, command, commandSettings);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		ServerPlayer target = getPlayer(context).get();
		if(target.uniqueId().equals(src.uniqueId())) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_TARGET_SELF);
		delay(target, locale, consumer -> {
			src.setLocation(target.serverLocation());
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(new ParameterSettings(CommandParameters.createPlayer(false), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT));
	}

	@Override
	protected String permission() {
		return Permissions.TELEPORT_STAFF;
	}

}
