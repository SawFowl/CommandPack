package sawfowl.commandpack.commands.parameterized;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.locale.LocalesPaths;

@Register
public class Help extends AbstractParameterizedCommand {

	public Help(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		Component title = getComponent(locale, LocalesPaths.COMMANDS_HELP_TITLE);
		sendPaginationList(src, title, Component.text("=").color(title.color()), 10, getListTexts(locale, LocalesPaths.COMMANDS_HELP_LIST));
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.HELP;
	}

	@Override
	public String command() {
		return "help";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

}
