package sawfowl.commandpack.commands.parameterized.onlyplayercommands.teleports;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPackPlugin;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;

public class TeleportHereAll extends AbstractPlayerCommand {

	public TeleportHereAll(CommandPackPlugin plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		for(ServerPlayer target : Sponge.server().onlinePlayers()) {
			if(!target.uniqueId().equals(src.uniqueId())) delay(target, locale, consumer -> {
				plugin.getPlayersData().getTempData().setPreviousLocation(target);
				target.setLocation(src.serverLocation());
			});
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

	@Override
	public String permission() {
		return Permissions.TELEPORT_HERE_ALL_STAFF;
	}

	@Override
	public String command() {
		return "teleporthereall";
	}

}
