package sawfowl.commandpack.api.data.commands.parameterized;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.Command.Builder;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.server.ServerLocation;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.api.data.commands.PluginCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.commandpack.utils.tasks.CooldownTimerTask;

public interface ParameterizedCommand extends PluginCommand, CommandExecutor {

	void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException;

	Map<String, ParameterSettings> getSettingsMap();

	Command.Parameterized build();

	@Override
	default CommandResult execute(CommandContext context) throws CommandException {
		boolean isPlayer = context.cause().audience() instanceof ServerPlayer;
		Locale locale = getLocale(context.cause());
		if(getSettingsMap() != null && !getSettingsMap().isEmpty()) for(ParameterSettings settings : getSettingsMap().values()) {
			if(!settings.containsIn(context)) if(!settings.isOptional() || (!isPlayer && !settings.isOptionalForConsole())) exception(locale, settings.getPath());
		}
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) context.cause().audience();
			if(getCommandSettings() != null && getCooldowns() != null) {
				Long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
				if(!getCooldowns().containsKey(player.uniqueId())) {
					getCooldowns().put(player.uniqueId(), currentTime + getCommandSettings().getCooldown());
					Sponge.asyncScheduler().submit(Task.builder().plugin(getContainer()).interval(1, TimeUnit.SECONDS).execute(new CooldownTimerTask(player, getCommandSettings(), getCooldowns())).build());
				} else {
					if((getCooldowns().get(player.uniqueId())) - currentTime > 0) exception(locale, Placeholders.DELAY, timeFormat((getCooldowns().get(player.uniqueId())) - currentTime, locale), LocalesPaths.COMMANDS_COOLDOWN);
					getCooldowns().remove(player.uniqueId());
					getCooldowns().put(player.uniqueId(), currentTime + getCommandSettings().getCooldown());
				}
			}
			execute(context, player, locale, isPlayer);
		} else execute(context, context.cause().audience(), locale, isPlayer);
		return success();
	}

	default void register(RegisterCommandEvent<Parameterized> event) {
		if(getCommandSettings() == null) {
			event.register(getContainer(), build(), command());
		} else {
			if(!getCommandSettings().isEnable()) return;
			if(getCommandSettings().getAliases() != null && getCommandSettings().getAliases().length > 0) {
				event.register(getContainer(), build(), command(), getCommandSettings().getAliases());
			} else event.register(getContainer(), build(), command());
		}
	}

	default Builder builder() {
		return builderNoPerm().permission(permission());
	}

	default Builder builderNoPerm() {
		return getSettingsMap() != null && !getSettingsMap().isEmpty() ?
				Command.builder()
					.addParameters(getSettingsMap().values().stream().map(ParameterSettings::getParameterUnknownType).toArray(Value[]::new))
					.executor(this) :
				Command.builder()
					.executor(this);
	}

	default Command.Parameterized fastBuild() {
		return builder().build();
	}

	default Optional<ServerPlayer> getPlayer(CommandContext context) {
		return getArgument(context, ServerPlayer.class, "Player");
	}

	default Optional<ServerPlayer> getPlayer(CommandContext context, Audience src, boolean isPlayer) {
		return Optional.ofNullable(getPlayer(context).orElseGet(() -> (isPlayer ? (ServerPlayer) src : null)));
	}

	default Optional<String> getUser(CommandContext context) {
		return getArgument(context, String.class, "User");
	}

	default Optional<Boolean> getBoolean(CommandContext context, String arg) {
		return getArgument(context, Boolean.class, arg);
	}

	default boolean getBoolean(CommandContext context, String arg, boolean def) {
		return getBoolean(context, arg).orElse(def);
	}

	default Optional<String> getString(CommandContext context, String arg) {
		return getArgument(context, String.class, arg);
	}

	default String getString(CommandContext context, String arg, String def) {
		return getString(context, arg).orElse(def);
	}

	default Optional<ServerLocation> getLocation(CommandContext context) {
		return getArgument(context, ServerLocation.class, "Location");
	}

	default <T> Optional<T> getArgument(CommandContext context, Class<T> object, String arg) {
		return getSettingsMap() != null && getSettingsMap().containsKey(arg) ? getSettingsMap().get(arg).getParameterValue(object, context) : Optional.empty();
	}

}
