package sawfowl.commandpack.commands.abstractcommands.raw;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.math.NumberUtils;
import org.spongepowered.api.command.Command.Raw;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.scheduler.Task;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.commands.abstractcommands.PluginCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public abstract class AbstractRawCommand extends PluginCommand implements Raw {

	private List<CommandCompletion> empty = new ArrayList<>();
	public AbstractRawCommand(CommandPack plugin, String command, CommandSettings commandSettings) {
		super(plugin, command, commandSettings);
	}

	protected abstract void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args) throws CommandException;

	protected abstract List<String>  complete(CommandCause cause, List<String> args, String plainArg) throws CommandException;

	protected abstract Predicate<AbstractRawCommand> canExecuteRaw(CommandCause cause);

	protected abstract Component shortDescription(Locale locale);

	protected abstract Component extendedDescription(Locale locale);

	@Override
	public CommandResult process(CommandCause cause, Mutable arguments) throws CommandException {
		boolean isPlayer = cause.audience() instanceof ServerPlayer;
		Locale locale = getLocale(cause);
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) cause.audience();
			Long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
			if(!cooldowns.containsKey(player.uniqueId())) {
				cooldowns.put(player.uniqueId(), currentTime + commandSettings.getCooldown());
				Sponge.asyncScheduler().submit(Task.builder().plugin(plugin.getPluginContainer()).interval(1, TimeUnit.SECONDS).execute(new CooldownTimerTask(player)).build());
			} else {
				if((cooldowns.get(player.uniqueId())) - currentTime > 0) exception(locale, Placeholders.DELAY, getExpireTimeFromNow((cooldowns.get(player.uniqueId())) - currentTime, locale), LocalesPaths.COMMANDS_COOLDOWN);
				cooldowns.remove(player.uniqueId());
				cooldowns.put(player.uniqueId(), currentTime + commandSettings.getCooldown());
			}
			process(cause, player, locale, isPlayer, Stream.of(arguments.input().split(" ")).map(String::toString).filter(string -> (!string.equals(""))).toArray(String[]::new));
		} else process(cause, cause.audience(), locale, isPlayer, Stream.of(arguments.input().split(" ")).map(String::toString).filter(string -> (!string.equals(""))).toArray(String[]::new));
		return success();
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, Mutable arguments) throws CommandException {
		List<String> complete = complete(cause, Stream.of(arguments.input().split(" ")).map(String::toString).filter(string -> (!string.equals(""))).collect(Collectors.toList()), arguments.input());
		return complete == null || complete.size() == 0 ? empty : complete.stream().map(string -> (CommandCompletion.of(string))).collect(Collectors.toList());
	}

	@Override
	public Optional<Component> shortDescription(CommandCause cause) {
		return Optional.ofNullable(shortDescription(getLocale(cause)));
	}

	@Override
	public Optional<Component> extendedDescription(CommandCause cause) {
		return Optional.ofNullable(extendedDescription(getLocale(cause)));
	}

	protected boolean isNumber(String string) {
		return string != null && NumberUtils.isParsable(string);
	}

	protected int toInt(String string) {
		return isNumber(string) ? NumberUtils.createInteger(string) : 0;
	}

	@Override
	public boolean canExecute(CommandCause cause) {
		return cause.hasPermission(permission()) && canExecuteRaw(cause).test(this);
	}

	public void register(RegisterCommandEvent<Raw> event) {
		if(aliases != null && aliases.length > 0) {
			event.register(plugin.getPluginContainer(), this, command, aliases);
		} else event.register(plugin.getPluginContainer(), this, command);
	}

}
