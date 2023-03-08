package sawfowl.commandpack.commands.abstractcommands;

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
import org.spongepowered.api.SystemSubject;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.util.Nameable;
import org.spongepowered.api.util.locale.LocaleSource;
import org.spongepowered.api.world.LocatableBlock;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.commands.ThrowingConsumer;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.commands.CommandPrice;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.locale.Locales;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.commandpack.utils.Logger;
import sawfowl.localeapi.api.TextUtils;

public abstract class PluginCommand {

	protected final CommandPack plugin;
	protected final String command;
	protected final String[] aliases;
	protected Map<UUID, Long> cooldowns = new HashMap<>();
	protected final CommandSettings commandSettings;
	public PluginCommand(CommandPack plugin, String command, CommandSettings commandSettings) {
		this.plugin = plugin;
		this.command = command;
		this.commandSettings = commandSettings;
		this.aliases = commandSettings.getAliases();
	}

	protected abstract String permission();

	protected CommandResult success() {
		return CommandResult.success();
	}

	protected Locale getLocale(CommandCause cause) {
		return cause.audience() instanceof SystemSubject ? getLocales().getLocaleService().getSystemOrDefaultLocale() : (cause.audience() instanceof LocaleSource ? ((LocaleSource) cause.audience()).locale() : org.spongepowered.api.util.locale.Locales.DEFAULT);
	}

	protected CommandException exception(Component text) throws CommandException {
		throw new CommandException(text);
	}

	protected CommandException exception(String text) throws CommandException {
		return exception(text(text));
	}

	protected CommandException exception(Locale locale, Object... path) throws CommandException {
		return exception(getText(locale, path));
	}

	protected CommandException exception(Locale locale, String key, String value, Object... path) throws CommandException {
		return exception(TextUtils.replace(getText(locale, path), key, value));
	}

	protected CommandException exception(Locale locale, String key, Component value, Object... path) throws CommandException {
		return exception(TextUtils.replace(getText(locale, path), key, value));
	}

	protected CommandException exception(Locale locale, String[] keys, String[] values, Object... path) throws CommandException {
		return exception(TextUtils.replace(getText(locale, path), keys, values));
	}

	protected CommandException exception(Locale locale, String[] keys, Component[] values, Object... path) throws CommandException {
		return exception(TextUtils.replaceToComponents(getText(locale, path), keys, values));
	}

	protected Component text(String string) {
		return TextUtils.deserializeLegacy(string);
	}

	protected BigDecimal createDecimal(double value) {
		return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
	}

	protected Component getText(Locale locale, Object... path) {
		return getLocales().getText(locale, path);
	}

	protected Component getText(ServerPlayer player, Object... path) {
		return getText(player.locale(), path);
	}

	protected Locales getLocales() {
		return plugin.getLocales();
	}

	protected Logger getLogger() {
		return plugin.getLogger();
	}

	protected Component getExpireTimeFromNow(long second, Locale locale) {
		long minute = TimeUnit.SECONDS.toMinutes(second);
		long hour = TimeUnit.SECONDS.toHours(second);
		if(hour == 0) {
			if(minute == 0) {
				return TextUtils.replace(Component.text(String.format((second > 9 ? "%02d" : "%01d"), second) + "%second%"), "%second%", getLocales().getText(locale, LocalesPaths.TIME_SECOND));
			} else return TextUtils.replaceToComponents(Component.text(String.format((minute > 9 ? "%02d" : "%01d"), minute) + "%minute%" + (second - (minute * 60) > 0 ? " " + String.format((second - (minute * 60) > 9 ? "%02d" : "%01d"), second - (minute * 60)) + "%second%" : "")), new String[] {"%minute%", "%second%"}, new Component[] {getLocales().getText(locale, LocalesPaths.TIME_MINUTE), getLocales().getText(locale, LocalesPaths.TIME_SECOND)});
		}
		return TextUtils.replaceToComponents(Component.text(String.format((hour > 9 ? "%02d" : "%01d"), hour) + "%hour%" + (minute - (hour * 60) > 0 ? " " + String.format((minute - (hour * 60) > 9 ? "%02d" : "%01d"), minute - (hour * 60)) + "%minute%" : "")), new String[] {"%hour%", "%minute%"}, new Component[] {getLocales().getText(locale, LocalesPaths.TIME_HOUR), getLocales().getText(locale, LocalesPaths.TIME_MINUTE)});
	}

	protected long getExpireHourFromNow(long second) {
		return TimeUnit.SECONDS.toHours(second);
	}

	protected long getExpireMinuteFromNow(long second) {
		return TimeUnit.SECONDS.toMinutes(second);
	}

	protected Optional<ServerPlayer> getPlayer(UUID uuid) {
		return Sponge.server().player(uuid);
	}

	public Optional<ServerPlayer> getPlayer(String name) {
		return Sponge.server().player(name);
	}

	protected void saveUser(User user) {
		Sponge.server().userManager().forceSave(user.uniqueId());
	}

	protected class CooldownTimerTask implements Consumer<ScheduledTask> {
		public CooldownTimerTask(ServerPlayer player){
			uuid = player.uniqueId();
		}
		private final UUID uuid;
		private final long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		long cooldown = plugin.getCommandsConfig().getCommandConfig(command).getCooldown();
		@Override
		public void accept(ScheduledTask task) {
			if(cooldowns.containsKey(uuid)) {
				if((cooldowns.get(uuid) + cooldown) - currentTime <= 0) cooldowns.remove(uuid);
			} else {
				task.cancel();
			}
			
		}
		
	}

	protected String getSourceName(CommandCause cause, Audience audience, Locale locale, ServerPlayer player) {
		return plugin.getMainConfig().isHideTeleportCommandSource() && !player.hasPermission(Permissions.IGNORE_HIDE_COMMAND_SOURCE) ? player.name() :
			audience instanceof SystemSubject ? getString(locale, LocalesPaths.NAME_SYSTEM) :
				isCommandBlock(cause) ? getString(locale, LocalesPaths.NAME_COMMANDBLOCK) + blockCords(cause) :
					isCommandBlockMinecart(cause) ? getString(locale, LocalesPaths.NAME_COMMANDBLOCK_MINECART) + entityCords(cause) :
						audience instanceof Nameable ? ((Nameable) audience).name() :
							getString(locale, LocalesPaths.NAME_UNKNOWN);
	}

	protected boolean isCommandBlock(CommandCause cause) {
		return getLocatableBlock(cause).filter(block -> (block.blockState().type().equals(BlockTypes.COMMAND_BLOCK.get()) || block.blockState().type().equals(BlockTypes.CHAIN_COMMAND_BLOCK.get()) || block.blockState().type().equals(BlockTypes.REPEATING_COMMAND_BLOCK.get()))).isPresent();
	}

	protected Optional<LocatableBlock> getLocatableBlock(CommandCause cause) {
		return cause.first(LocatableBlock.class);
	}

	protected String blockCords(CommandCause cause) {
		return getLocatableBlock(cause).map(LocatableBlock::serverLocation).map(location -> ("<" + location.worldKey().asString() + ">" + location.blockPosition())).orElse("");
	}

	protected boolean isCommandBlockMinecart(CommandCause cause) {
		return getEntity(cause).isPresent();
	}

	protected Optional<Entity> getEntity(CommandCause cause) {
		return cause.first(Entity.class).filter(entity -> (entity.type().equals(EntityTypes.COMMAND_BLOCK_MINECART.get())));
	}

	protected String entityCords(CommandCause cause) {
		return getEntity(cause).map(Entity::serverLocation).map(location -> ("<" + location.worldKey().asString() + ">" + location.blockPosition())).orElse("");
	}

	protected String getString(Locale locale, Object[] path) {
		return plugin.getLocales().getString(locale, path);
	}

	protected void economy(ServerPlayer player, Locale locale) throws CommandException {
		if(plugin.getEconomy().isPresent() && !player.hasPermission(Permissions.getIgnorePrice(this.command))) {
			CommandPrice price = commandSettings.getPrice();
			if(price.getMoney() > 0) {
				Currency currency = plugin.getEconomy().checkCurrency(price.getCurrency());
				BigDecimal money = createDecimal(price.getMoney());
				if(!plugin.getEconomy().checkPlayerBalance(player.uniqueId(), currency, money)) exception(locale, new String[] {Placeholders.MONEY, Placeholders.COMMAND}, new Component[] {currency.symbol().append(text(money.toString())), text("/" + this.command)}, LocalesPaths.COMMANDS_ERROR_TAKE_MONEY);
			}
		}
	}

	public void delay(ServerPlayer player, Locale locale, ThrowingConsumer<PluginCommand, CommandException> consumer) throws CommandException {
		if(commandSettings.getDelay().getSeconds() > 0 && !player.hasPermission(Permissions.getIgnoreDelayTimer(this.command))) {
			plugin.getTempPlayerData().addCommandTracking(this.command, player);
			Sponge.asyncScheduler().submit(Task.builder().plugin(plugin.getPluginContainer()).interval(1, TimeUnit.SECONDS).execute(new DelayTimerTask(consumer, player, commandSettings.getDelay().getSeconds())).build());
		} else {
			economy(player, locale);
			consumer.accept(this);
		}
	}

	protected class DelayTimerTask implements Consumer<ScheduledTask> {
		public DelayTimerTask(ThrowingConsumer<PluginCommand, CommandException> consumer, ServerPlayer player, long seconds) {
			this.uuid = player.uniqueId();
			this.seconds = seconds;
			this.consumer = consumer;
		}
		private final UUID uuid;
		private long seconds;
		private final ThrowingConsumer<PluginCommand, CommandException> consumer;
		private long hour;
		private long minute;
		boolean first = true;
		@Override
		public void accept(ScheduledTask task) {
			if(seconds <= 0 || !getPlayer(uuid).isPresent() || !getPlayer(uuid).get().isOnline()) {
				Sponge.server().scheduler().executor(plugin.getPluginContainer()).execute(() -> {
					getPlayer(uuid).ifPresent(player -> {
						try {
							if(plugin.getTempPlayerData().getTrackingPlayerCommands(player).isPresent() && plugin.getTempPlayerData().getTrackingPlayerCommands(uuid).get().containsKey(command)) {
								economy(player, player.locale());
								consumer.accept(PluginCommand.this);
							}
						} catch (CommandException e) {
							player.sendMessage(e.componentMessage());
						}
					});
				});
				if(plugin.getTempPlayerData().getTrackingPlayerCommands(uuid).isPresent() && plugin.getTempPlayerData().getTrackingPlayerCommands(uuid).get().containsKey(command)) plugin.getTempPlayerData().removeCommandTracking(command, uuid);;
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

	/**
	 * Added to speed up the creation of commands.
	 * Must be deleted after the job is done.
	 */
	@Deprecated
	public static Permissions perm() {
		return Permissions.instance;
	}

}
