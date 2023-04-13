package sawfowl.commandpack.api.data.commands.parameterized;

import java.util.Locale;

import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.Command.Builder;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public interface ParameterizedPlayerCommand extends ParameterizedCommand {

	void execute(CommandContext context, ServerPlayer src, Locale locale) throws CommandException;

	@Override
	default void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		execute(context, (ServerPlayer) src, locale);
	}

	@Override
	default Builder builder() {
		return getSettingsMap() != null && !getSettingsMap().values().isEmpty() ? 
				Command.builder()
					.executionRequirements(cause -> (
							cause.audience() instanceof ServerPlayer && cause.hasPermission(permission()))
						)
					.addParameters(getSettingsMap().values().stream().map(ParameterSettings::getParameterUnknownType).toArray(Value[]::new))
					.executor(this) :
				Command.builder()
					.executionRequirements(cause -> (
							cause.audience() instanceof ServerPlayer && cause.hasPermission(permission()))
						)
					.executor(this);
	}

	default CommandException exception(ServerPlayer player, Object... path) throws CommandException {
		return exception(getText(player, path));
	}

	default CommandException exception(ServerPlayer player, String[] keys, String[] values, Object... path) throws CommandException {
		return exception(player.locale(), keys, values, path);
	}

	default CommandException exception(ServerPlayer player, String[] keys, Component[] values, Object... path) throws CommandException {
		return exception(player.locale(), keys, values, path);
	}

}
