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

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.PluginCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.commandpack.utils.tasks.CooldownTimerTask;

public interface RawCommand extends PluginCommand, Raw {

	void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException;

	List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException;

	Component shortDescription(Locale locale);

	Component extendedDescription(Locale locale);

	List<CommandCompletion> getEmptyCompletion();

	Map<String, RawCommand> getChildExecutors();

	@Override
	default CommandResult process(CommandCause cause, Mutable arguments) throws CommandException {
		boolean isPlayer = cause.audience() instanceof ServerPlayer;
		Locale locale = getLocale(cause);
		String[] args = Stream.of(arguments.input().split(" ")).map(String::toString).filter(string -> (!string.equals(""))).toArray(String[]::new);
		if(args.length != 0 && getChildExecutors() != null && !getChildExecutors().isEmpty() && getChildExecutors().containsKey(args[0]) && getChildExecutors().get(args[0]).canExecute(cause)) {
			List<String> list = new ArrayList<>(Arrays.asList(args));
			list.remove(0);
			getChildExecutors().get(args[0]).process(cause, cause.audience(), getLocale(cause), isPlayer, list.toArray(new String[] {}), arguments);
			return success();
		}
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
		process(cause, cause.audience(), locale, isPlayer, args, arguments);
		return success();
	}

	@Override
	default List<CommandCompletion> complete(CommandCause cause, Mutable arguments) throws CommandException {
		List<String> args = Stream.of(arguments.input().split(" ")).filter(string -> (!string.equals(""))).collect(Collectors.toList());
		if(getChildExecutors() != null && !getChildExecutors().isEmpty()) {
			if(args.size() == 0 || (args.size() == 1 && !arguments.input().contains(args.get(0) + " "))) {
				return getChildExecutors().keySet().stream().filter(command -> (args.isEmpty() || ((command.equals(args.get(0)) || command.startsWith(args.get(0))) && getChildExecutors().get(command).canExecute(cause)))).map(CommandCompletion::of).collect(Collectors.toList());
			}
			if(getChildExecutors().containsKey(args.get(0))) {
				return getChildExecutors().get(args.get(0)).complete(cause, args.subList(1, args.size()), arguments, arguments.input().contains(args.get(0) + " ") ? arguments.input().replace(args.get(0) + " ", "") : arguments.input().replace(args.get(0), ""));
			}
		}
		List<CommandCompletion> complete = complete(cause, args, arguments, arguments.input());
		return complete == null || complete.size() == 0 ? getEmptyCompletion() : complete.stream().collect(Collectors.toList());
	}

	@Override
	default Optional<Component> shortDescription(CommandCause cause) {
		return Optional.ofNullable(shortDescription(getLocale(cause)));
	}

	@Override
	default Optional<Component> extendedDescription(CommandCause cause) {
		return Optional.ofNullable(extendedDescription(getLocale(cause)));
	}

	@Override
	default boolean canExecute(CommandCause cause) {
		return cause.hasPermission(permission());
	}

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
			throw exception(CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.COMMANDS_KITS_COOLDOWN_INCORRECT_TIME));
		}
	}

	default Optional<Integer> parseInt(String arg) {
		return NumberUtils.isParsable(arg) ? Optional.ofNullable(NumberUtils.createInteger(arg)) : Optional.empty();
	}

	default Optional<Double> parseDouble(String arg) {
		return NumberUtils.isParsable(arg) ? Optional.ofNullable(NumberUtils.createDouble(arg)) : Optional.empty();
	}

}
