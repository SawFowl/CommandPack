package sawfowl.commandpack.commands.abstractcommands.parameterized;

import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.CommandPack;

public abstract class AbstractPlayerCommand extends AbstractCommand {

	public AbstractPlayerCommand(CommandPack plugin, String command) {
		super(plugin, command);
	}

	public AbstractPlayerCommand(CommandPack plugin, String command, String[] aliases) {
		super(plugin, command, aliases);
	}

	@Override
	public void execute(CommandContext context, Audience audience, Locale locale) throws CommandException {
		execute(context, (ServerPlayer) audience, locale);
	}

	@Override
	public boolean canExecute(CommandCause cause) {
		return cause.audience() instanceof ServerPlayer && cause.hasPermission(permission());
	}

	public abstract void execute(CommandContext context, ServerPlayer player, Locale locale) throws CommandException;

}
