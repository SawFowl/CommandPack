package sawfowl.commandpack.api.data.commands.raw;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import sawfowl.commandpack.api.data.commands.CooldownTimerTask;
import sawfowl.commandpack.api.data.commands.PluginCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public interface RawCommand extends PluginCommand, Raw {

	void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args) throws CommandException;

	List<String> complete(CommandCause cause, List<String> args, String plainArg) throws CommandException;

	Component shortDescription(Locale locale);

	Component extendedDescription(Locale locale);

	List<CommandCompletion> getEmptyCompletion();

	@Override
	default List<CommandCompletion> complete(CommandCause cause, Mutable arguments) throws CommandException {
		List<String> complete = complete(cause, Stream.of(arguments.input().split(" ")).map(String::toString).filter(string -> (!string.equals(""))).collect(Collectors.toList()), arguments.input());
		return complete == null || complete.size() == 0 ? getEmptyCompletion() : complete.stream().map(string -> (CommandCompletion.of(string))).collect(Collectors.toList());
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

	@Override
	default CommandResult process(CommandCause cause, Mutable arguments) throws CommandException {
		boolean isPlayer = cause.audience() instanceof ServerPlayer;
		Locale locale = getLocale(cause);
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) cause.audience();
			Long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
			if(!getCooldowns().containsKey(player.uniqueId())) {
				getCooldowns().put(player.uniqueId(), currentTime + getCommandSettings().getCooldown());
				Sponge.asyncScheduler().submit(Task.builder().plugin(getContainer()).interval(1, TimeUnit.SECONDS).execute(new CooldownTimerTask(player, getCommandSettings(), getCooldowns())).build());
			} else {
				if((getCooldowns().get(player.uniqueId())) - currentTime > 0) exception(locale, Placeholders.DELAY, getExpireTimeFromNow((getCooldowns().get(player.uniqueId())) - currentTime, locale), LocalesPaths.COMMANDS_COOLDOWN);
				getCooldowns().remove(player.uniqueId());
				getCooldowns().put(player.uniqueId(), currentTime + getCommandSettings().getCooldown());
			}
			process(cause, player, locale, isPlayer, Stream.of(arguments.input().split(" ")).map(String::toString).filter(string -> (!string.equals(""))).toArray(String[]::new));
		} else process(cause, cause.audience(), locale, isPlayer, Stream.of(arguments.input().split(" ")).map(String::toString).filter(string -> (!string.equals(""))).toArray(String[]::new));
		return success();
	}

	default void register(RegisterCommandEvent<Raw> event) {
		if(getCommandSettings() != null && getCommandSettings().getAliases() != null && getCommandSettings().getAliases().length > 0) {
			event.register(getContainer(), this, command(), getCommandSettings().getAliases());
		} else event.register(getContainer(), this, command());
	}

}
