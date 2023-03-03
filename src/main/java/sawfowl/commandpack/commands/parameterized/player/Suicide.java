package sawfowl.commandpack.commands.parameterized.player;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.commands.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;

public class Suicide extends AbstractPlayerCommand {

	public Suicide(CommandPack plugin, String command, String[] aliases) {
		super(plugin, command, aliases);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException {
		if(continueEconomy(src)) src.offer(Keys.HEALTH, 0.0);
	}

	@Override
	public Parameterized build() {
		return builder().build();
	}

	@Override
	public String permission() {
		return Permissions.SUICIDE;
	}

	@Override
	public Optional<List<ParameterSettings<?>>> getParameterSettings() {
		return Optional.empty();
	}

}
