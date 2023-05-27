package sawfowl.commandpack.api.commands.raw;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.math.NumberUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.Command.Raw;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.PluginCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.commandpack.utils.tasks.CooldownTimerTask;

/**
 * This interface is designed to simplify the creation of Raw commands.
 *
 * @author SawFowl
 */
public interface RawCommand extends PluginCommand, Raw {

	/**
	 * Command code execution.
	 */
	void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException;

	Component shortDescription(Locale locale);

	Component extendedDescription(Locale locale);

	/**
	 * The method is designed to define an empty autofill list.<br>
	 * It is recommended to create a class variable to save resources.
	 */
	List<CommandCompletion> getEmptyCompletion();

	/**
	 * Subcommand map.<br>
	 * The subcommands automatically get to the first place when the arguments are autocomplete.
	 */
	Map<String, RawCommand> getChildExecutors();

	Map<Integer, RawArgument<?>> getArguments();

	/**
	 * Checks for child commands and who activated the command.
	 */
	@Override
	default CommandResult process(CommandCause cause, Mutable arguments) throws CommandException {
		boolean isPlayer = cause.audience() instanceof ServerPlayer;
		Locale locale = getLocale(cause);
		String[] args = Stream.of(arguments.input().split(" ")).map(String::toString).filter(string -> (!string.equals(""))).toArray(String[]::new);
		checkArguments(cause, args, isPlayer, locale);
		checkCooldown(cause, locale, isPlayer);
		if(args.length != 0 && getChildExecutors() != null && !getChildExecutors().isEmpty() && getChildExecutors().containsKey(args[0]) && getChildExecutors().get(args[0]).canExecute(cause)) {
			String[] childArgs = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[] {};
			getChildExecutors().get(args[0]).process(cause, cause.audience(), locale, isPlayer, childArgs, arguments);
			return success();
		}
		process(cause, cause.audience(), locale, isPlayer, args, arguments);
		return success();
	}

	/**
	 * Autocomplete arguments.
	 */
	@Override
	default List<CommandCompletion> complete(CommandCause cause, Mutable arguments) throws CommandException {
		List<String> args = Stream.of(arguments.input().split(" ")).filter(string -> (!string.equals(""))).collect(Collectors.toList());
		String currentInput = arguments.input();
		List<CommandCompletion> complete = completeChild(cause, args, arguments, currentInput);
		return complete == null || complete.size() == 0 ? (getEmptyCompletion() == null ? new ArrayList<>() : getEmptyCompletion()) : complete;
	}

	default void checkArguments(CommandCause cause, String[] args, boolean isPlayer, Locale locale) throws CommandException {
		if(args.length != 0 && getChildExecutors() != null && !getChildExecutors().isEmpty() && getChildExecutors().containsKey(args[0]) && getChildExecutors().get(args[0]).canExecute(cause)) {
			getChildExecutors().get(args[0]).checkArguments(cause, (args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[] {}), isPlayer, locale);
		}
		if(getArguments() != null && !getArguments().isEmpty()) for(RawArgument<?> arg : getArguments().values()) if((args.length <= arg.getCursor() && (!arg.isOptional() || (!isPlayer && !arg.isOptionalForConsole())))) exceptionAppendUsage(cause, getText(locale, arg.getLocalesPath()));
	}

	default void checkCooldown(CommandCause cause, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) cause.audience();
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
	}

	default List<CommandCompletion> completeChild(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		if(getChildExecutors() != null && !getChildExecutors().isEmpty()) {
			List<CommandCompletion> completions;
			if(args.size() == 0 || (args.size() == 1 && !currentInput.endsWith(" "))) {
				completions = getChildExecutors().keySet().stream().filter(command -> (args.isEmpty() || ((command.equals(args.get(0)) || command.startsWith(args.get(0))) && getChildExecutors().get(command).canExecute(cause)))).map(CommandCompletion::of).collect(Collectors.toList());
				completions.addAll(complete(cause, args, arguments, currentInput));
				return completions;
			} else if(getChildExecutors().containsKey(args.get(0))) {
				completions = getChildExecutors().get(args.get(0)).completeChild(cause, args.subList(1, args.size()), arguments, currentInput.contains(args.get(0) + " ") ? currentInput.replace(args.get(0) + " ", "") : currentInput.replace(args.get(0), ""));
				completions.addAll(complete(cause, args, arguments, currentInput));
				return completions;
			}
		}
		return complete(cause, args, arguments, currentInput);
	}

	/**
	 * Auto-complete command arguments.
	 */
	default List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		if(getArguments() == null || !getArguments().containsKey(args.size())) return getEmptyCompletion();
		if(currentInput.endsWith(" ") || args.size() == 0) {
			RawArgument<?> rawArg = getArguments().get(args.size());
			return rawArg.getVariants().map(CommandCompletion::of).collect(Collectors.toList());
		} else {
			RawArgument<?> rawArg = getArguments().get(args.size() - 1);
			return rawArg.getVariants().filter(v -> ((v.contains(args.get(args.size() - 1)) && !currentInput.endsWith(" ")) || (args.get(args.size() - 1).contains(v) && !args.get(args.size() - 1).contains(v + " ")) || (v.contains(":") && (v.split(":")[0].contains(args.get(args.size() - 1)) || v.split(":")[1].contains(args.get(args.size() - 1)))))).map(CommandCompletion::of).collect(Collectors.toList());
		}
	}

	@Override
	default Optional<Component> shortDescription(CommandCause cause) {
		return Optional.ofNullable(shortDescription(getLocale(cause)));
	}

	@Override
	default Optional<Component> extendedDescription(CommandCause cause) {
		return Optional.ofNullable(extendedDescription(getLocale(cause)));
	}

	/**
	 * Determining who can use the command.
	 */
	@Override
	default boolean canExecute(CommandCause cause) {
		return cause.hasPermission(permission());
	}

	/**
	 * Command registration.
	 */
	default void register(RegisterCommandEvent<Raw> event) {
		if(getCommandSettings() == null) {
			event.register(getContainer(), this, command());
		} else {
			if(!getCommandSettings().isEnable()) return;
			if(getCommandSettings().getAliases() != null && getCommandSettings().getAliases().length > 0) {
				event.register(getContainer(), this, command(), getCommandSettings().getAliases());
			} else event.register(getContainer(), this, command());
		}
	}

	/**
	 * Convert string to object {@link Duration}
	 */
	default Duration getDuration(String s, Locale locale) throws CommandException {
		s = s.toUpperCase();
		if (!s.contains("T")) {
			if (s.contains("D")) {
				if (s.contains("H") || s.contains("M") || s.contains("S")) {
					s = s.replace("D", "DT");
				}
			} else {
				if (s.startsWith("P")) {
					s = "PT" + s.substring(1);
				} else {
					s = "T" + s;
				}
			}
		}
		if (!s.startsWith("P")) {
			s = "P" + s;
		}
		try {
			return Duration.parse(s);
		} catch (final DateTimeParseException ex) {
			throw exception(CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.COMMANDS_EXCEPTION_COOLDOWN_INCORRECT_TIME));
		}
	}

	/**
	 * An attempt to convert a string to an integer.
	 */
	default Optional<Integer> parseInt(String arg) {
		return NumberUtils.isParsable(arg) ? Optional.ofNullable(NumberUtils.createInteger(arg)) : Optional.empty();
	}

	/**
	 * An attempt to convert a string to a fractional number.
	 */
	default Optional<Double> parseDouble(String arg) {
		return NumberUtils.isParsable(arg) ? Optional.ofNullable(NumberUtils.createDouble(arg)) : Optional.empty();
	}

	default CommandException exceptionAppendUsage(CommandCause cause, Component text) throws CommandException {
		throw new CommandException(usage(cause) == null ? text : text.append(Component.newline()).append(usage(cause)));
	}

	default CommandException exceptionAppendUsage(CommandCause cause, Locale locale, Object[] localePath) throws CommandException {
		throw new CommandException(getText(locale, localePath).append(Component.newline()).append(usage(cause)));
	}

	default Optional<ServerWorld> getWorld(String[] args, int cursor) {
		return getArgument(ServerWorld.class, args, cursor);
	}

	default Optional<ServerPlayer> getPlayer(String[] args, int cursor) {
		return getArgument(ServerPlayer.class, args, cursor);
	}

	default Optional<String> getString(String[] args, int cursor) {
		return getArgument(String.class, args, cursor);
	}

	default Optional<Boolean> getBoolean(String[] args, int cursor) {
		return getArgument(Boolean.class, args, cursor);
	}

	default Optional<Integer> getInteger(String[] args, int cursor) {
		return getArgument(Integer.class, args, cursor);
	}

	default Optional<Long> getLong(String[] args, int cursor) {
		return getArgument(Long.class, args, cursor);
	}

	default Optional<Double> getDouble(String[] args, int cursor) {
		return getArgument(Double.class, args, cursor);
	}

	default Optional<Duration> getDurationArg(String[] args, int cursor, Locale locale) throws CommandException {
		Optional<String> arg = getArgument(String.class, args, cursor);
		return arg.isPresent() ? Optional.ofNullable(getDuration(arg.get(), locale)) : Optional.empty();
	}

	@SuppressWarnings("unchecked")
	default <T> Optional<T> getArgument(Class<T> clazz, String[] args, int cursor) {
		return getArguments() == null || !getArguments().containsKey(cursor) || getArguments().get(cursor).getClazz() != clazz || !getArguments().get(cursor).getClazz().getName().equals(clazz.getName()) ? Optional.empty() : ((RawArgument<T>) getArguments().get(cursor)).getResult(clazz, args);
	}

}
