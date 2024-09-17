package sawfowl.commandpack.api.commands.raw;

import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;

/**
 * This interface is designed to simplify the creation of RawSettings commands.<br>
 * Commands created using this interface can only be used by players.
 * 
 * @author SawFowl
 */
public interface RawPlayerCommand extends RawCommand {

	/**
	 * Command code execution.
	 */
	void process(CommandCause cause, ServerPlayer src, Locale locale, String[] args, Mutable arguments) throws CommandException;

	@Override
	default void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		process(cause, (ServerPlayer) cause.audience(), locale, args, arguments);
	}

	@Override
	default boolean canExecute(CommandCause cause) {
		return (permission() == null || cause.hasPermission(permission())) && cause.first(ServerPlayer.class).isPresent();
	}

}
