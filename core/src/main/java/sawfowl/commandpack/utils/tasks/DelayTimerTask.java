package sawfowl.commandpack.utils.tasks;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.PluginCommand;
import sawfowl.commandpack.api.data.command.Price;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.commands.ThrowingConsumer;
import sawfowl.localeapi.api.Text;

public class DelayTimerTask implements Consumer<ScheduledTask> {

	public DelayTimerTask(ThrowingConsumer<PluginCommand, CommandException> consumer, ServerPlayer player, PluginContainer container, String command, PluginCommand commandClass) {
		this.uuid = player.uniqueId();
		this.seconds = commandClass.getCommandSettings().getDelay().getSeconds();
		this.consumer = consumer;
		this.container = container;
		this.command = command;
		this.commandClass = commandClass;
	}

	private final UUID uuid;
	private long seconds;
	private final ThrowingConsumer<PluginCommand, CommandException> consumer;
	private long hour;
	private long minute;
	boolean first = true;
	private final PluginContainer container;
	final String command;
	private PluginCommand commandClass;
	@Override
	public void accept(ScheduledTask task) {
		Optional<Map<String, Settings>> trackingPlayerCommands = CommandPack.getInstance().getPlayersData().getTempData().getTrackingPlayerCommands(uuid);
		if(seconds <= 0 || !getPlayer(uuid).isPresent() || !getPlayer(uuid).get().isOnline()) {
			Sponge.server().scheduler().executor(container).execute(() -> {
				getPlayer(uuid).ifPresent(player -> {
					try {
						if(trackingPlayerCommands.isPresent() && trackingPlayerCommands.get().containsKey(command)) {
							economy(player, player.locale());
							consumer.accept(commandClass);
						}
					} catch (CommandException e) {
						player.sendMessage(e.componentMessage());
					}
				});
			});
			if(trackingPlayerCommands.isPresent() && trackingPlayerCommands.get().containsKey(command)) CommandPack.getInstance().getPlayersData().getTempData().removeCommandTracking(command, uuid);
			task.cancel();
			return;
		} else {
			if(!getPlayer(uuid).isPresent() || !getPlayer(uuid).get().isOnline()) {
				task.cancel();
				return;
			}
			ServerPlayer player = getPlayer(uuid).get();
			if(!trackingPlayerCommands.isPresent() || !trackingPlayerCommands.get().containsKey(command)) {
				task.cancel();
				return;
			}
			if(getExpireHourFromNow(seconds) > 0) {
				if(hour != getExpireHourFromNow(seconds)) {
					hour = getExpireHourFromNow(seconds);
					player.sendMessage(CommandPack.getInstance().getLocales().getLocale(player).getCommandExceptions().getWait(getExpireTimeFromNow(seconds, player.locale())).hoverEvent(HoverEvent.showText(Component.text("/" + command).color(NamedTextColor.LIGHT_PURPLE))));
				}
			} else if(seconds > 60) {
				if(minute != getExpireMinuteFromNow(seconds)) {
					minute = getExpireMinuteFromNow(seconds);
					player.sendMessage(CommandPack.getInstance().getLocales().getLocale(player).getCommandExceptions().getCooldown(getExpireTimeFromNow(seconds, player.locale())).hoverEvent(HoverEvent.showText(Component.text("/" + command).color(NamedTextColor.LIGHT_PURPLE))));
				}
			} else if(seconds == 60 || seconds == 30 || seconds == 10 || seconds <= 5 || first) {
				first = false;
				player.sendMessage(CommandPack.getInstance().getLocales().getLocale(player).getCommandExceptions().getCooldown(getExpireTimeFromNow(seconds, player.locale())).hoverEvent(HoverEvent.showText(Component.text("/" + command).color(NamedTextColor.LIGHT_PURPLE))));
			}
			seconds--;
		}
	}

	void economy(ServerPlayer player, Locale locale) throws CommandException {
		if(commandClass.getCommandSettings() == null || CommandPack.getInstance().getEconomy().isPresent() || player.hasPermission(Permissions.getIgnorePrice(command))) return;
		Price price = commandClass.getCommandSettings().getPrice();
		if(price.getMoney() > 0) {
			Currency currency = CommandPack.getInstance().getEconomy().checkCurrency(price.getCurrency());
			BigDecimal money = createDecimal(price.getMoney());
			if(!CommandPack.getInstance().getEconomy().checkPlayerBalance(player.uniqueId(), currency, money)) exceptionMoney(locale, currency, money);
		}
	}

	private CommandException exceptionMoney(Locale locale, Currency currency, BigDecimal money) throws CommandException {
		return new CommandException(CommandPack.getInstance().getLocales().getLocale(locale).getOther().getExecuteCommand().getNoMoney(currency, money, "/" + command));
	}

	private long getExpireHourFromNow(long second) {
		return TimeUnit.SECONDS.toHours(second);
	}

	private long getExpireMinuteFromNow(long second) {
		return TimeUnit.SECONDS.toMinutes(second);
	}

	private Optional<ServerPlayer> getPlayer(UUID uuid) {
		return Sponge.server().player(uuid);
	}

	private BigDecimal createDecimal(double value) {
		return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
	}

	private Component getExpireTimeFromNow(long second, Locale locale) {
		long minute = TimeUnit.SECONDS.toMinutes(second);
		long hour = TimeUnit.SECONDS.toHours(second);
		if(hour == 0) {
			if(minute == 0) {
				return Text.of(String.format((second > 9 ? "%02d" : "%01d"), second) + "%second%").replace("%second%", CommandPack.getInstance().getLocales().getLocale(locale).getTime().getSecond()).get();
			} else return Text.of(String.format((minute > 9 ? "%02d" : "%01d"), minute) + "%minute%" + (second - (minute * 60) > 0 ? " " + String.format((second - (minute * 60) > 9 ? "%02d" : "%01d"), second - (minute * 60)) + "%second%" : "")).replace(new String[] {"%minute%", "%second%"}, CommandPack.getInstance().getLocales().getLocale(locale).getTime().getMinute(), CommandPack.getInstance().getLocales().getLocale(locale).getTime().getSecond()).get();
		}
		return Text.of(String.format((hour > 9 ? "%02d" : "%01d"), hour) + "%hour%" + (minute - (hour * 60) > 0 ? " " + String.format((minute - (hour * 60) > 9 ? "%02d" : "%01d"), minute - (hour * 60)) + "%minute%" : "")).replace(new String[] {"%hour%", "%minute%"}, CommandPack.getInstance().getLocales().getLocale(locale).getTime().getHour(), CommandPack.getInstance().getLocales().getLocale(locale).getTime().getMinute()).get();
	}


}
