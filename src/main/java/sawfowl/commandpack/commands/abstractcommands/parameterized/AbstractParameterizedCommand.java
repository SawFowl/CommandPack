package sawfowl.commandpack.commands.abstractcommands.parameterized;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.Command.Builder;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.server.ServerLocation;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.commands.abstractcommands.PluginCommand;
import sawfowl.commandpack.commands.settings.ParameterSettings;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public abstract class AbstractParameterizedCommand extends PluginCommand implements CommandExecutor {

	private Map<UUID, Long> cooldowns = new HashMap<>();
	protected final Map<String, ParameterSettings> parameterSettings = new HashMap<>();
	public AbstractParameterizedCommand(CommandPack plugin) {
		super(plugin);
		List<ParameterSettings> parameterSettings = getParameterSettings();
		if(parameterSettings != null && !parameterSettings.isEmpty()) {
			parameterSettings.forEach(setting -> {
				setting.getParameterUnknownType().key().key();
				this.parameterSettings.put(setting.getParameterUnknownType().key().key(), setting);
			});
		}
	}

	/*public AbstractParameterizedCommand() {
		List<ParameterSettings> parameterSettings = getParameterSettings();
		if(parameterSettings != null && !parameterSettings.isEmpty()) {
			parameterSettings.forEach(setting -> {
				setting.getParameterUnknownType().key().key();
				this.parameterSettings.put(setting.getParameterUnknownType().key().key(), setting);
			});
		}
	}*/

	public abstract void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException;

	public abstract Command.Parameterized build();

	public abstract List<ParameterSettings> getParameterSettings();

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		boolean isPlayer = context.cause().audience() instanceof ServerPlayer;
		Locale locale = getLocale(context.cause());
		if(!parameterSettings.isEmpty()) for(ParameterSettings settings : parameterSettings.values()) if(!context.one(settings.getParameterUnknownType()).isPresent() && (!settings.isOptional() || (!isPlayer && !settings.isOptionalForConsole()))) exception(locale, settings.getPath());
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) context.cause().audience();
			Long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
			if(!cooldowns.containsKey(player.uniqueId())) {
				cooldowns.put(player.uniqueId(), currentTime + commandSettings.getCooldown());
				Sponge.asyncScheduler().submit(Task.builder().plugin(plugin.getPluginContainer()).interval(1, TimeUnit.SECONDS).execute(new CooldownTimerTask(player)).build());
			} else {
				if((cooldowns.get(player.uniqueId())) - currentTime > 0) exception(locale, Placeholders.DELAY, getExpireTimeFromNow((cooldowns.get(player.uniqueId())) - currentTime, locale), LocalesPaths.COMMANDS_COOLDOWN);
				cooldowns.remove(player.uniqueId());
				cooldowns.put(player.uniqueId(), currentTime + commandSettings.getCooldown());
			}
			execute(context, player, locale, isPlayer);
		} else execute(context, context.cause().audience(), locale, isPlayer);
		return success();
	}

	public Builder builder() {
		return parameterSettings.isEmpty() ?
				Command.builder()
					.permission(permission())
					.executor(this) :
				Command.builder()
					.permission(permission())
					.addParameters(parameterSettings.values().stream().map(ParameterSettings::getParameterUnknownType).toArray(Value[]::new))
					.executor(this);
	}

	public Command.Parameterized fastBuild() {
		return builder().build();
	}

	public void register(RegisterCommandEvent<Parameterized> event) {
		if(aliases != null && aliases.length > 0) {
			event.register(plugin.getPluginContainer(), build(), command(), aliases);
		} else event.register(plugin.getPluginContainer(), build(), command());
	}

	public Optional<ServerPlayer> getPlayer(CommandContext context) {
		return getArgument(context, ServerPlayer.class, "Player");
	}

	public Optional<ServerPlayer> getPlayer(CommandContext context, Audience src, boolean isPlayer) {
		return Optional.ofNullable(getPlayer(context).orElseGet(() -> (isPlayer ? (ServerPlayer) src : null)));
	}

	public Optional<String> getUser(CommandContext context) {
		return getArgument(context, String.class, "User");
	}

	public Optional<Boolean> getBoolean(CommandContext context, String arg) {
		return getArgument(context, Boolean.class, arg);
	}

	public boolean getBoolean(CommandContext context, String arg, boolean def) {
		return getBoolean(context, arg).orElse(def);
	}

	public Optional<String> getString(CommandContext context, String arg) {
		return getArgument(context, String.class, arg);
	}

	public String getString(CommandContext context, String arg, String def) {
		return getString(context, arg).orElse(def);
	}

	public Optional<ServerLocation> getLocation(CommandContext context) {
		return getArgument(context, ServerLocation.class, "Location");
	}

	public <T> Optional<T> getArgument(CommandContext context, Class<T> object, String arg) {
		return parameterSettings.containsKey(arg) ? parameterSettings.get(arg).getParameterValue(object, context) : Optional.empty();
	}

}
