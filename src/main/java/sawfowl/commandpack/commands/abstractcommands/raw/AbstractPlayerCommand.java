package sawfowl.commandpack.commands.abstractcommands.raw;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;

public abstract class AbstractPlayerCommand extends AbstractRawCommand {

	public AbstractPlayerCommand(CommandPack plugin, String command, CommandSettings commandSettings) {
		super(plugin, command, commandSettings);
	}

	protected abstract void process(CommandCause cause, ServerPlayer src, Locale locale, String[] args) throws CommandException;

	protected abstract List<String> complete(CommandCause cause, ServerPlayer src, List<String> args, String plainArg) throws CommandException;

	@Override
	protected void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args) throws CommandException {
		process(cause, (ServerPlayer) cause.audience(), locale, args);
	}

	@Override
	protected List<String> complete(CommandCause cause, List<String> args, String plainArg) throws CommandException {
		return complete(cause, (ServerPlayer) cause.audience(), args, plainArg);
	}

	@Override
	protected Predicate<AbstractRawCommand> canExecuteRaw(CommandCause cause) {
		return predicate -> cause.audience() instanceof ServerPlayer;
	}

}
