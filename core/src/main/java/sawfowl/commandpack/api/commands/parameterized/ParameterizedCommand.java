package sawfowl.commandpack.api.commands.parameterized;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

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
import org.spongepowered.api.world.server.ServerLocation;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.PluginCommand;
import sawfowl.commandpack.configure.locale.LocalesPaths;

/**
 * This interface is designed to simplify the creation of commands and add additional functionality to them.
 * 
 * @author SawFowl
 */
public interface ParameterizedCommand extends PluginCommand, CommandExecutor {

	/**
	 * Command code execution.
	 */
	void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException;

	/**
	 * Map of arguments.<br>
	 * The argument can be optional for the player and mandatory for the console.
	 */
	Map<String, ParameterSettings> getSettingsMap();

	/**
	 * Command building.
	 */
	Command.Parameterized build();

	/**
	 * Check the command arguments and determine who is using the command.
	 */
	@Override
	default CommandResult execute(CommandContext context) throws CommandException {
		boolean isPlayer = context.cause().first(ServerPlayer.class).isPresent();
		Locale locale = getLocale(context.cause());
		if(!isPlayer && onlyPlayer()) exception(CommandPack.getInstance().getLocales().getComponent(locale, LocalesPaths.COMMANDS_EXCEPTION_ONLY_PLAYER));
		if(getSettingsMap() != null && !getSettingsMap().isEmpty()) for(ParameterSettings settings : getSettingsMap().values()) if(!settings.containsIn(context) && (!settings.isOptional() || (!isPlayer && !settings.isOptionalForConsole()))) exception(locale, settings.getPath());
		checkCooldown(context.cause(), locale, isPlayer);
		execute(context, context.cause().first(ServerPlayer.class).map(player -> (Audience) player).orElse(context.cause().audience()), locale, isPlayer);
		return success();
	}

	default boolean onlyPlayer() {
		return false;
	}

	/**
	 * Command registration.
	 */
	default void register(RegisterCommandEvent<Parameterized> event) {
		if(build() == null) return;
		if(getCommandSettings() == null) {
			if(getCommandSettings() == null || getCommandSettings().isEnable()) CommandPack.getInstance().getAPI().registerCommand(this);
		} else {
			if(!getCommandSettings().isEnable()) return;
			if(getCommandSettings() == null || getCommandSettings().isEnable()) CommandPack.getInstance().getAPI().registerCommand(this);
		}
		CommandPack.getInstance().getPlayersData().getTempData().addTrackingCooldownCommand(this);
	}

	/**
	 * Command builder.<br>
	 * This builder adds permission.
	 */
	default Builder builder() {
		return builderNoPerm().permission(permission());
	}

	/**
	 * Command builder.<br>
	 * This builder adds arguments if they exist.
	 */
	default Builder builderNoPerm() {
		return getSettingsMap() != null && !getSettingsMap().isEmpty() ?
				Command.builder()
					.addParameters(getSettingsMap().values().stream().map(ParameterSettings::getParameterUnknownType).toArray(Value[]::new))
					.executor(this) :
				Command.builder()
					.executor(this);
	}

	/**
	 * Command building.<br>
	 * Automatically applies permission and arguments if available.
	 */
	default Command.Parameterized fastBuild() {
		return (permission() == null ?  builderNoPerm() : builder()).build();
	}

	default Optional<ServerPlayer> getPlayer(CommandContext context) {
		return getArgument(context, ServerPlayer.class, "Player");
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

	/**
	 * Getting an object from a command argument.
	 */
	default <T> Optional<T> getArgument(CommandContext context, Class<T> object, String arg) {
		return getSettingsMap() != null && getSettingsMap().containsKey(arg) ? getSettingsMap().get(arg).getParameterValue(object, context) : Optional.empty();
	}

	/**
	 * Getting an object from a command argument.
	 */
	default <T> Optional<T> getArgument(CommandContext context, Value<T> value) {
		return context.one(value);
	}

}
