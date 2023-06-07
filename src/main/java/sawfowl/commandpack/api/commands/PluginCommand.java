package sawfowl.commandpack.api.commands;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.SystemSubject;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.util.locale.LocaleSource;
import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.command.Price;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.commands.ThrowingConsumer;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.commandpack.utils.tasks.DelayTimerTask;
import sawfowl.localeapi.api.TextUtils;

/**
 * An auxiliary interface for creating commands.
 * 
 * @author SawFowl
 */
public interface PluginCommand {

	/**
	 * Command settings.<br>
	 * This is where you specify the aliases, kooldowns, delays, and price.
	 */
	Settings getCommandSettings();

	/**
	 * Retrieving text from a specified location. You can return null, but in which case you need to create localized messages in LocaleAPI.
	 */
	Component getText(Object[] path);

	/**
	 * {@link PluginContainer} to which the command will be registered.
	 */
	PluginContainer getContainer();

	/**
	 * The permission needed to execute the command.
	 */
	String permission();

	/**
	 * The main alias of the command.
	 */
	String command();

	/**
	 * The name of the command for which the last execution time will be checked.<br>
	 * For example, commands such as `/day`, `/night` are checked as `/time`.<br>
	 * By default, it is unique for each command. Overriding it may be useful in some situations.
	 */
	default String trackingName() {
		return command();
	}

	default Map<UUID, Long> getCooldowns() {
		return CommandPack.getInstance().getPlayersData().getTempData().getTrackingMap(trackingName());
	}

	/**
	 * Getting a localized message for the player.
	 */
	default Component getText(ServerPlayer player, Object[] path) {
		return getText(player.locale(), path);
	}

	default Locale getLocale(CommandCause cause) {
		return cause.audience() instanceof SystemSubject ? CommandPack.getInstance().getLocales().getLocaleService().getSystemOrDefaultLocale() : (cause.audience() instanceof LocaleSource ? ((LocaleSource) cause.audience()).locale() : org.spongepowered.api.util.locale.Locales.DEFAULT);
	}

	/**
	 * Getting a localized message.
	 */
	default Component getText(Locale locale, Object... path) {
		Component text = getText(path);
		if(text != null) return text;
		return text(CommandPack.getInstance().getLocales().getLocaleService().getOrDefaultLocale(getContainer().metadata().id(), locale).getString(path));
	}

	default List<Component> getListTexts(Locale locale, Object... path) {
		return CommandPack.getInstance().getLocales().getLocaleService().getOrDefaultLocale(getContainer().metadata().id(), locale).getListStrings(path).stream().map(s -> text(s)).collect(Collectors.toList());
	}

	default CommandResult success() {
		return CommandResult.success();
	}

	/**
	 * Serialization of text.
	 */
	default Component text(String string) {
		if(isLegacyDecor(string)) {
			return TextUtils.deserializeLegacy(string);
		} else {
			return TextUtils.deserialize(string);
		}
	}

	default Component text(Object objectToString) {
		return text(objectToString.toString());
	}

	/**
	 * Outputs an error message when the command is executed.
	 */
	default CommandException exception(Component text) throws CommandException {
		throw new CommandException(text);
	}

	/**
	 * Outputs an error message when the command is executed.
	 */
	default CommandException exception(String text) throws CommandException {
		return exception(text(text));
	}

	/**
	 * 
	 * Outputs a localized error message during command execution.
	 */
	default CommandException exception(Locale locale, Object... path) throws CommandException {
		return exception(getText(locale, path));
	}

	default CommandException exception(Locale locale, String replaceKey, Component replaceValue, Object... path) throws CommandException {
		return exception(TextUtils.replace(getText(locale, path), replaceKey, replaceValue));
	}

	default CommandException exception(Locale locale, String key, String value, Object... path) throws CommandException {
		return exception(TextUtils.replace(getText(locale, path), key, value));
	}

	default CommandException exception(Locale locale, String[] keys, String[] values, Object... path) throws CommandException {
		return exception(TextUtils.replace(getText(locale, path), keys, values));
	}

	default CommandException exception(Locale locale, String[] keys, Component[] values, Object... path) throws CommandException {
		return exception(TextUtils.replaceToComponents(getText(locale, path), keys, values));
	}

	/**
	 * Time formatting.
	 */
	default Component timeFormat(long second, Locale locale) {
		long minute = TimeUnit.SECONDS.toMinutes(second);
		long hour = TimeUnit.SECONDS.toHours(second);
		long days = TimeUnit.SECONDS.toDays(second);
		if(days == 0) {
			if(hour == 0) {
				if(minute == 0) {
					return TextUtils.replace(Component.text(String.format((second > 9 ? "%02d" : "%01d"), second) + "%second%"), "%second%", CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.TIME_SECOND));
				} else return TextUtils.replaceToComponents(Component.text(String.format((minute > 9 ? "%02d" : "%01d"), minute) + "%minute%" + (second - (minute * 60) > 0 ? " " + String.format((second - (minute * 60) > 9 ? "%02d" : "%01d"), second - (minute * 60)) + "%second%" : "")), new String[] {"%minute%", "%second%"}, new Component[] {CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.TIME_MINUTE), CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.TIME_SECOND)});
			} else return TextUtils.replaceToComponents(Component.text(String.format((hour > 9 ? "%02d" : "%01d"), hour) + "%hour%" + (minute - (hour * 60) > 0 ? " " + String.format((minute - (hour * 60) > 9 ? "%02d" : "%01d"), minute - (hour * 60)) + "%minute%" : "")), new String[] {"%hour%", "%minute%"}, new Component[] {CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.TIME_HOUR), CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.TIME_MINUTE)});
		}
		return TextUtils.replaceToComponents(Component.text(String.format((days > 9 ? "%02d" : "%01d"), days) + "%days% " + String.format((hour - (days * 24) > 9 ? "%02d" : "%01d"), hour - (days * 24)) + "%hour%" + (minute - (hour * 60) > 0 ? " " + String.format((minute - (hour * 60) > 9 ? "%02d" : "%01d"), minute - (hour * 60)) + "%minute%" : "")), new String[] {"%days%", "%hour%", "%minute%"}, new Component[] {CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.TIME_DAYS), CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.TIME_HOUR), CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.TIME_MINUTE)});
	}

	/**
	 * Code execution delay.<br>
	 * Automatically activates the command's execution fee if required by the command's settings.
	 */
	default void delay(ServerPlayer player, Locale locale, ThrowingConsumer<PluginCommand, CommandException> consumer) throws CommandException {
		if(getCommandSettings().getDelay().getSeconds() > 0 && !player.hasPermission(Permissions.getIgnoreDelayTimer(command()))) {
			CommandPack.getInstance().getPlayersData().getTempData().addCommandTracking(command(), player);
			Sponge.asyncScheduler().submit(Task.builder().plugin(getContainer()).interval(1, TimeUnit.SECONDS).execute(new DelayTimerTask(consumer, player, 0, getContainer(), command(), getCommandSettings(), this)).build());
		} else {
			economy(player, locale);
			consumer.accept(this);
		}
	}

	/**
	 * Payment for the execution of the command.
	 */
	default void economy(ServerPlayer player, Locale locale) throws CommandException {
		if(getCommandSettings() == null || !CommandPack.getInstance().getEconomy().isPresent() || player.hasPermission(Permissions.getIgnorePrice(command()))) return;
		Price price = getCommandSettings().getPrice();
		if(price.getMoney() > 0) {
			Currency currency = CommandPack.getInstance().getEconomy().checkCurrency(price.getCurrency());
			BigDecimal money = createDecimal(price.getMoney());
			if(!CommandPack.getInstance().getEconomy().checkPlayerBalance(player.uniqueId(), currency, money)) exception(TextUtils.replaceToComponents(CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.COMMANDS_ERROR_TAKE_MONEY), new String[] {Placeholders.MONEY, Placeholders.COMMAND}, new Component[] {currency.symbol().append(text(money.toString())), text("/" + command())}));
			CommandPack.getInstance().getEconomy().removeFromPlayerBalance(player, currency, money);
		}
	}

	static BigDecimal createDecimal(double value) {
		return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
	}

	default boolean isLegacyDecor(String string) {
		return string.indexOf('&') != -1 && !string.endsWith("&") && isStyleChar(string.charAt(string.indexOf("&") + 1));
	}

	default boolean isStyleChar(char ch) {
		return "0123456789abcdefklmnor".indexOf(ch) != -1;
	}

	/**
	 * Sending a multi-page list of text.
	 */
	default void sendPaginationList(Audience target, Component title, Component padding, int linesPerPage, List<Component> components) {
		if(linesPerPage < 1) linesPerPage = 5;
		PaginationList.builder()
			.linesPerPage(linesPerPage)
			.title(title)
			.contents(components)
			.padding(padding)
			.sendTo(target);
	}

}
