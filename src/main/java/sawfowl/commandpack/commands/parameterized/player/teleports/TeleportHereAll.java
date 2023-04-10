package sawfowl.commandpack.commands.parameterized.player.teleports;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.ParameterSettings;

public class TeleportHereAll extends AbstractPlayerCommand {

	public TeleportHereAll(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		for(ServerPlayer target : Sponge.server().onlinePlayers()) {
			if(!target.uniqueId().equals(src.uniqueId())) delay(target, locale, consumer -> {
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
	protected String permission() {
		return Permissions.TELEPORT_HERE_ALL_STAFF;
	}

	@Override
	public String command() {
		return "teleporthereall";
	}

}
