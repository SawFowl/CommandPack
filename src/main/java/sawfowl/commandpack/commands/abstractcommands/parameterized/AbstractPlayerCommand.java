package sawfowl.commandpack.commands.abstractcommands.parameterized;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.configure.LocalesPaths;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.Command;
import sawfowl.localeapi.api.TextUtils;

public abstract class AbstractPlayerCommand extends AbstractCommand {

	private Map<UUID, Long> cooldowns = new HashMap<>();

	public AbstractPlayerCommand(CommandPack plugin, String command) {
		super(plugin, command);
	}

	public AbstractPlayerCommand(CommandPack plugin, String command, String[] aliases) {
		super(plugin, command, aliases);
	}

	@Override
	public void execute(CommandContext context, Audience audience, Locale locale) throws CommandException {
		ServerPlayer player = (ServerPlayer) audience;
		Command command = plugin.getCommandsConfig().getCommandConfig(this.command);
		checkCooldown(player, command);
		if(command.getDelay() > 0) {
			Sponge.asyncScheduler().submit(Task.builder().plugin(plugin.getPluginContainer()).interval(1, TimeUnit.SECONDS).execute(new CancellingTimerTask(context, player, command.getDelay())).build());
		} else execute(context, player, locale);
	}

	@Override
	public boolean canExecute(CommandCause cause) {
		return cause.audience() instanceof ServerPlayer && cause.hasPermission(permission());
	}

	private void checkCooldown(ServerPlayer player, Command command) throws CommandException {
		Long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		if(cooldowns.containsKey(player.uniqueId())) {
			if(command.getCooldown() + cooldowns.get(player.uniqueId()) > currentTime) exception(TextUtils.replace(getLocales().getText(player.locale(), LocalesPaths.COMMANDS_COOLDOWN), Placeholders.DELAY, getExpireTimeFromNow(currentTime - cooldowns.get(player.uniqueId()), player.locale())));
			cooldowns.remove(player.uniqueId());
			cooldowns.put(player.uniqueId(), currentTime + command.getDelay());
		} else cooldowns.put(player.uniqueId(), currentTime + command.getDelay());
		Sponge.asyncScheduler().submit(Task.builder().plugin(plugin.getPluginContainer()).delay(command.getCooldown() + 1, TimeUnit.SECONDS).execute(() -> {
			if(cooldowns.containsKey(player.uniqueId())) cooldowns.remove(player.uniqueId());
		}).build());
	}

	public Component getExpireTimeFromNow(long second, Locale locale) {
		long minute = TimeUnit.SECONDS.toMinutes(second);
		long hour = TimeUnit.SECONDS.toHours(second);
		if(hour == 0) {
			if(minute == 0) {
				return TextUtils.replace(Component.text(String.format((second > 9 ? "%02d" : "%01d"), second) + "%second%"), "%second%", getLocales().getText(locale, LocalesPaths.TIME_SECOND));
			} else return TextUtils.replaceToComponents(Component.text(String.format((minute > 9 ? "%02d" : "%01d"), minute) + "%minute%" + (second - (minute * 60) > 0 ? " " + String.format((second - (minute * 60) > 9 ? "%02d" : "%01d"), second - (minute * 60)) + "%second%" : "")), new String[] {"%minute%", "%second%"}, new Component[] {getLocales().getText(locale, LocalesPaths.TIME_MINUTE), getLocales().getText(locale, LocalesPaths.TIME_SECOND)});
		}
		return TextUtils.replaceToComponents(Component.text(String.format((hour > 9 ? "%02d" : "%01d"), hour) + "%hour%" + (minute - (hour * 60) > 0 ? " " + String.format((minute - (hour * 60) > 9 ? "%02d" : "%01d"), minute - (hour * 60)) + "%minute%" : "")), new String[] {"%hour%", "%minute%"}, new Component[] {getLocales().getText(locale, LocalesPaths.TIME_HOUR), getLocales().getText(locale, LocalesPaths.TIME_MINUTE)});
	}

	public long getExpireHourFromNow(long second) {
		return TimeUnit.SECONDS.toHours(second);
	}

	public long getExpireMinuteFromNow(long second) {
		return TimeUnit.SECONDS.toMinutes(second);
	}

	class CancellingTimerTask implements Consumer<ScheduledTask> {
		CancellingTimerTask(CommandContext context, ServerPlayer player, long seconds) {
			this.uuid = player.uniqueId();
			this.seconds = seconds;
			this.context = context;
		}
		private final UUID uuid;
		private long seconds;
		private final CommandContext context;
		private long hour;
		private long minute;
		@Override
		public void accept(ScheduledTask task) {
			if(seconds <= 0 || !getPlayer(uuid).isPresent()) {
				Sponge.server().scheduler().executor(plugin.getPluginContainer()).execute(() -> {
					getPlayer(uuid).ifPresent(player -> {
						try {
							execute(context, player, player.locale());
						} catch (CommandException e) {
							plugin.getLogger().error(e.getLocalizedMessage());
						}
					});
				});
				task.cancel();
				return;
			} else {
				if(getExpireHourFromNow(seconds) > 0) {
					if(hour != getExpireHourFromNow(seconds)) {
						hour = getExpireHourFromNow(seconds);
						getPlayer(uuid).ifPresent(player -> {
							if(player.isOnline()) player.sendMessage(TextUtils.replace(getLocales().getText(player.locale(), LocalesPaths.COMMANDS_WAIT), Placeholders.DELAY, getExpireTimeFromNow(seconds, player.locale())).hoverEvent(HoverEvent.showText(Component.text("/" + command).color(NamedTextColor.LIGHT_PURPLE))));
						});
					}
				} else if(seconds > 60) {
					if(minute != getExpireMinuteFromNow(seconds)) {
						minute = getExpireMinuteFromNow(seconds);
						getPlayer(uuid).ifPresent(player -> {
							if(player.isOnline()) player.sendMessage(TextUtils.replace(getLocales().getText(player.locale(), LocalesPaths.COMMANDS_WAIT), Placeholders.DELAY, getExpireTimeFromNow(seconds, player.locale())).hoverEvent(HoverEvent.showText(Component.text("/" + command).color(NamedTextColor.LIGHT_PURPLE))));
						});
					}
				} else if(seconds == 60 || seconds == 30 || seconds == 10 || seconds <= 5) getPlayer(uuid).ifPresent(player -> {
						if(player.isOnline()) player.sendMessage(TextUtils.replace(getLocales().getText(player.locale(), LocalesPaths.COMMANDS_WAIT), Placeholders.DELAY, getExpireTimeFromNow(seconds, player.locale())).hoverEvent(HoverEvent.showText(Component.text("/" + command).color(NamedTextColor.LIGHT_PURPLE))));
					});
				seconds--;
			}
		}
	}

	private Optional<ServerPlayer> getPlayer(UUID uuid) {
		return Sponge.server().player(uuid);
	}

	public abstract void execute(CommandContext context, ServerPlayer player, Locale locale) throws CommandException;

}
