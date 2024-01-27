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
public class Afk extends AbstractPlayerCommand {

	public Afk(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		if(plugin.getPlayersData().getTempData().isAfk(src)) {
			plugin.getPlayersData().getTempData().updateLastActivity(src);
		} else plugin.getPlayersData().getTempData().setAfkStatus(src);
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.AFK;
	}

	@Override
	public String command() {
		return "afk";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

}
