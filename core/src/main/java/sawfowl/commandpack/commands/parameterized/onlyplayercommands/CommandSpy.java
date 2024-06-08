package sawfowl.commandpack.commands.parameterized.onlyplayercommands;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class CommandSpy extends AbstractPlayerCommand {

	public CommandSpy(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		plugin.getPlayersData().getTempData().switchSpyCommand(src);
		src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getCommandSpy().getValue(plugin.getPlayersData().getTempData().isSpyCommand(src)));
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.COMMANDSPY_STAFF;
	}

	@Override
	public String command() {
		return "commandspy";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

}
