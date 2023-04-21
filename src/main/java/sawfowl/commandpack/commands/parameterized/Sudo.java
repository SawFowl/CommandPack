package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.manager.CommandMapping;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Sudo extends AbstractParameterizedCommand {

	public Sudo(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		String command = getString(context, "Command").get();
		if(!Sponge.server().commandManager().commandMapping(command.contains(" ") ? command.split(" ")[0] : command).isPresent()) exception(getText(locale, LocalesPaths.COMMANDS_SUDO_COMMAND_NOT_FOUND));
		ServerPlayer player = getPlayer(context).get();
		CommandMapping mapping = Sponge.server().commandManager().commandMapping(command.contains(" ") ? command.split(" ")[0] : command).get();
		if(isPlayer) {
			if(!mapping.registrar().canExecute(context.cause(), mapping)) exception(getText(locale, LocalesPaths.COMMANDS_SUDO_EXECUTE_NOT_ALLOWED));
			delay((ServerPlayer) src, locale, consumer -> {
				Sponge.server().commandManager().process(context.cause().subject(), player, command);
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_SUDO_SUCCESS));
			});
		} else {
			if(mapping.registrar().canExecute(context.cause(), mapping)) {
				Sponge.server().commandManager().process(context.cause().subject(), player, command);
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_SUDO_SUCCESS));
			} else {
				Sponge.server().commandManager().process(player, command);
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_SUDO_SUCCESS));
			}
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.SUDO_STAFF;
	}

	@Override
	public String command() {
		return "sudo";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.createPlayer(false), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.createString("Command", false), false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

}
