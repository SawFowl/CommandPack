package sawfowl.commandpack.commands.abstractcommands.parameterized;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.Command.Builder;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.Currency;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.configure.Locales;
import sawfowl.commandpack.configure.LocalesPaths;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.CommandPrice;
import sawfowl.commandpack.utils.Logger;
import sawfowl.localeapi.api.TextUtils;

public abstract class AbstractCommand implements CommandExecutor {

	protected final CommandPack plugin;
	protected final String command;
	final String[] aliases;
	private Map<UUID, Long> cooldowns = new HashMap<>();
	public AbstractCommand(CommandPack plugin, String command) {
		this.plugin = plugin;
		this.command = command;
		aliases = null;
	}

	public AbstractCommand(CommandPack plugin, String command, String[] aliases) {
		this.plugin = plugin;
		this.command = command;
		this.aliases = aliases;
	}

	public abstract void execute(CommandContext context, Audience audience, Locale locale) throws CommandException;

	public abstract Command.Parameterized build();

	public abstract String permission();

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		if(context.cause().audience() instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) context.cause().audience();
			sawfowl.commandpack.configure.configs.Command command = plugin.getCommandsConfig().getCommandConfig(this.command);
			if(plugin.getEconomy().isPresent() && !player.hasPermission(Permissions.getIgnorePrice(this.command))) {
				CommandPrice price = command.getPrice();
				if(price.getMoney() > 0) {
					Currency currency = plugin.getEconomy().checkCurrency(price.getCurrency());
					BigDecimal money = createDecimal(price.getMoney());
					if(!plugin.getEconomy().checkPlayerBalance(player.uniqueId(), currency, money)) exception(TextUtils.replaceToComponents(getText(player, LocalesPaths.COMMANDS_ERROR_TAKE_MONEY), new String[] {Placeholders.MONEY, Placeholders.COMMAND}, new Component[] {currency.symbol().append(text(money.toString())), text("/" + this.command)}));
				}
			}
			checkCooldown(player, command);
			if(command.getDelay().getSeconds() > 0 && !player.hasPermission(Permissions.getIgnoreDelayTimer(this.command))) {
				plugin.getTempPlayerData().addCommandTracking(this.command, player);
				Sponge.asyncScheduler().submit(Task.builder().plugin(plugin.getPluginContainer()).interval(1, TimeUnit.SECONDS).execute(new CancellingTimerTask(context, player, command.getDelay().getSeconds())).build());
			} else execute(context, player, player.locale());
		} else execute(context, context.cause().audience(), plugin.getLocales().getLocaleService().getSystemOrDefaultLocale());
		return success();
	}

	public CommandResult success() {
		return CommandResult.success();
	}

	public Builder builder() {
		return Command.builder()
				.permission(permission())
				.executor(this);
	}

	public CommandException exception(Component text) throws CommandException {
		throw new CommandException(text);
	}

	public CommandException exception(String text) throws CommandException {
		return exception(text(text));
	}

	public Component text(String string) {
		return TextUtils.deserializeLegacy(string);
	}

	public void register(RegisterCommandEvent<Parameterized> event) {
		if(aliases != null && aliases.length > 0) {
			event.register(plugin.getPluginContainer(), build(), command, aliases);
		} else event.register(plugin.getPluginContainer(), build(), command);
	}

	public Locales getLocales() {
		return plugin.getLocales();
	}

	public Logger getLogger() {
		return plugin.getLogger();
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

	void checkCooldown(ServerPlayer player, sawfowl.commandpack.configure.configs.Command command) throws CommandException {
		Long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		if(cooldowns.containsKey(player.uniqueId())) {
			if(command.getCooldown() + cooldowns.get(player.uniqueId()) > currentTime) exception(TextUtils.replace(getLocales().getText(player.locale(), LocalesPaths.COMMANDS_COOLDOWN), Placeholders.DELAY, getExpireTimeFromNow(currentTime - cooldowns.get(player.uniqueId()), player.locale())));
			cooldowns.remove(player.uniqueId());
			cooldowns.put(player.uniqueId(), currentTime + command.getDelay().getSeconds());
		} else cooldowns.put(player.uniqueId(), currentTime + command.getDelay().getSeconds());
		Sponge.asyncScheduler().submit(Task.builder().plugin(plugin.getPluginContainer()).delay(command.getCooldown() + 1, TimeUnit.SECONDS).execute(() -> {
			if(cooldowns.containsKey(player.uniqueId())) cooldowns.remove(player.uniqueId());
		}).build());
	}

	private Optional<ServerPlayer> getPlayer(UUID uuid) {
		return Sponge.server().player(uuid);
	}

	public BigDecimal createDecimal(double value) {
		return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
	}

	public Component getText(ServerPlayer player, Object... path) {
		return getLocales().getText(player.locale(), path);
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
		boolean first = true;
		@Override
		public void accept(ScheduledTask task) {
			if(seconds <= 0 || !getPlayer(uuid).isPresent() || !getPlayer(uuid).get().isOnline()) {
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
				ServerPlayer player = getPlayer(uuid).get();
				if(!plugin.getTempPlayerData().getTrackingPlayerCommands(player).isPresent() || !plugin.getTempPlayerData().getTrackingPlayerCommands(player).get().containsKey(command)) {
					task.cancel();
					return;
				}
				if(getExpireHourFromNow(seconds) > 0) {
					if(hour != getExpireHourFromNow(seconds)) {
						hour = getExpireHourFromNow(seconds);
						if(player.isOnline()) player.sendMessage(TextUtils.replace(getLocales().getText(player.locale(), LocalesPaths.COMMANDS_WAIT), Placeholders.DELAY, getExpireTimeFromNow(seconds, player.locale())).hoverEvent(HoverEvent.showText(Component.text("/" + command).color(NamedTextColor.LIGHT_PURPLE))));
					}
				} else if(seconds > 60) {
					if(minute != getExpireMinuteFromNow(seconds)) {
						minute = getExpireMinuteFromNow(seconds);
						if(player.isOnline()) player.sendMessage(TextUtils.replace(getLocales().getText(player.locale(), LocalesPaths.COMMANDS_WAIT), Placeholders.DELAY, getExpireTimeFromNow(seconds, player.locale())).hoverEvent(HoverEvent.showText(Component.text("/" + command).color(NamedTextColor.LIGHT_PURPLE))));
					}
				} else if(seconds == 60 || seconds == 30 || seconds == 10 || seconds <= 5 || first) {
					first = false;
					if(player.isOnline()) player.sendMessage(TextUtils.replace(getLocales().getText(player.locale(), LocalesPaths.COMMANDS_WAIT), Placeholders.DELAY, getExpireTimeFromNow(seconds, player.locale())).hoverEvent(HoverEvent.showText(Component.text("/" + command).color(NamedTextColor.LIGHT_PURPLE))));
				}
				seconds--;
			}
		}
	}

}
