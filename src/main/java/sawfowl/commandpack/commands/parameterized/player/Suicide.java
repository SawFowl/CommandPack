package sawfowl.commandpack.commands.parameterized.player;

import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractPlayerCommand;

public class Suicide extends AbstractPlayerCommand {

	public Suicide(CommandPack plugin, String command, String[] aliases) {
		super(plugin, command, aliases);
	}

	@Override
	public void execute(CommandContext context, ServerPlayer player, Locale locale) throws CommandException {
		if(continueEconomy(player)) player.offer(Keys.HEALTH, 0.0);
	}

	@Override
	public Parameterized build() {
		return builder().build();
	}

	@Override
	public String permission() {
		return Permissions.SUICIDE;
	}

}
