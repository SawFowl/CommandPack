package sawfowl.commandpack.api.commands.raw;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;

public interface RawPlayerCommand extends RawCommand {

	void process(CommandCause cause, ServerPlayer src, Locale locale, String[] args) throws CommandException;

	List<String> complete(CommandCause cause, ServerPlayer src, List<String> args, String plainArg) throws CommandException;

	@Override
	default void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args) throws CommandException {
		process(cause, (ServerPlayer) cause.audience(), locale, args);
	}

	@Override
	default List<String> complete(CommandCause cause, List<String> args, String plainArg) throws CommandException {
		return complete(cause, (ServerPlayer) cause.audience(), args, plainArg);
	}

	@Override
	default boolean canExecute(CommandCause cause) {
		return cause.hasPermission(permission()) && cause.audience() instanceof ServerPlayer;
	}

}
