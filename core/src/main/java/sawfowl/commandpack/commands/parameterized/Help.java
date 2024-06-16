package sawfowl.commandpack.commands.parameterized;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Help extends AbstractParameterizedCommand {

	public Help(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		sendPaginationList(src, plugin.getLocales().getLocale(locale).getCommands().getHelp().getTitle(), Component.text("=").color(plugin.getLocales().getLocale(locale).getCommands().getHelp().getTitle().color()), 10, plugin.getLocales().getLocale(locale).getCommands().getHelp().getList());
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
