package sawfowl.commandpack.commands.parameterized.onlyplayercommands.teleports;

import java.util.Arrays;
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
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class TeleportHere extends AbstractPlayerCommand {

	public TeleportHere(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		ServerPlayer target = getPlayer(context).get();
		if(target.uniqueId().equals(src.uniqueId())) exception(plugin.getLocales().getLocale(locale).getCommandExceptions().getTargetSelf());
		delay(target, locale, consumer -> {
			plugin.getPlayersData().getTempData().setPreviousLocation(target);
			target.setLocation(src.serverLocation());
		});
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createPlayer(false), false, locale -> plugin.getLocales().getLocale(locale).getCommandExceptions().getPlayerNotPresent()));
	}

	@Override
	public String permission() {
		return Permissions.TELEPORT_HERE_STAFF;
	}

	@Override
	public String command() {
		return "teleporthere";
	}

}
