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

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.command.Price;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.commands.ThrowingConsumer;
import sawfowl.commandpack.configure.locale.locales.AbstractLocale;
import sawfowl.commandpack.utils.tasks.CooldownTimerTask;
import sawfowl.commandpack.utils.tasks.DelayTimerTask;
import sawfowl.localeapi.api.LocaleReference;
import sawfowl.localeapi.api.Text;
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
	Component getComponent(Object[] path);

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

	default void checkCooldown(CommandCause cause, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) cause.audience();
			if(getCommandSettings() != null && getCooldowns() != null && !player.hasPermission(Permissions.getIgnoreCooldown(trackingName()))) {
				Long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
				if(!getCooldowns().containsKey(player.uniqueId())) {
					getCooldowns().put(player.uniqueId(), currentTime + getCommandSettings().getCooldown());
					Sponge.asyncScheduler().submit(Task.builder().plugin(getContainer()).interval(1, TimeUnit.SECONDS).execute(new CooldownTimerTask(player, getCommandSettings(), getCooldowns())).build());
				} else {
					if((getCooldowns().get(player.uniqueId())) - currentTime > 0) exception(getLocale(locale).getCommandExceptions().getCooldown(timeFormat((getCooldowns().get(player.uniqueId())) - currentTime, locale)));
					getCooldowns().remove(player.uniqueId());
					getCooldowns().put(player.uniqueId(), currentTime + getCommandSettings().getCooldown());
				}
			}
		}
	}

	default Map<UUID, Long> getCooldowns() {
		return CommandPackInstance.getInstance().getPlayersData().getTempData().getTrackingMap(trackingName());
	}

	/**
	 * Getting a localized message for the player.
	 */
	default Component getComponent(ServerPlayer player, Object[] path) {
		return getText(player.locale(), path).get();
	}

	/**
	 * Getting a localized message for the player.
	 */
	default Text getText(ServerPlayer player, Object[] path) {
		return getText(player.locale(), path);
	}

	default Locale getLocale(CommandCause cause) {
		return cause.audience() instanceof SystemSubject ? CommandPackInstance.getInstance().getLocales().getLocaleService().getSystemOrDefaultLocale() : (cause.audience() instanceof LocaleSource ? ((LocaleSource) cause.audience()).locale() : org.spongepowered.api.util.locale.Locales.DEFAULT);
	}

	/**
	 * Getting a localized message.
	 */
	default Component getComponent(Locale locale, Object... path) {
		Component text = getComponent(path);
		if(text != null) return text;
		return CommandPackInstance.getInstance().getLocales().getLocaleService().getOrDefaultLocale(getContainer().metadata().id(), locale).getComponent(path);
	}

	/**
	 * Getting a localized message.
	 */
	default Text getText(Locale locale, Object... path) {
		return Text.of(getComponent(locale, path));
	}

	default List<Component> getListTexts(Locale locale, Object... path) {
		return CommandPackInstance.getInstance().getLocales().getLocaleService().getOrDefaultLocale(getContainer().metadata().id(), locale).getListStrings(path).stream().map(s -> text(s)).collect(Collectors.toList());
	}

	default CommandResult success() {
		return CommandResult.success();
	}

	/**
	 * Serialization of text.
	 */
	default Component text(String string) {
		return TextUtils.deserialize(string);
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
	default CommandException exception(Text text) throws CommandException {
		throw new CommandException(text.get());
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

	default CommandException exception(Locale locale, Object[] path, String replaceKey, Component replaceValue) throws CommandException {
		return exception(getText(locale, path).replace(replaceKey, replaceValue));
	}

	default CommandException exception(Locale locale, Object[] path, String key, String value) throws CommandException {
		return exception(getText(locale, path).replace(key, value));
	}

	default CommandException exception(Locale locale, Object[] path, String[] keys, String... values) throws CommandException {
		return exception(getText(locale, path).replace(keys, values));
	}

	default CommandException exception(Locale locale, Object[] path, String[] keys, Component... values) throws CommandException {
		return exception(getText(locale, path).replace(keys, values));
	}

	/**
	 * Time formatting.
	 */
	default Component timeFormat(long second, Locale locale) {
		return TextUtils.timeFormat(second, locale, getLocale(locale).getTime().getDay(), getLocale(locale).getTime().getHour(), getLocale(locale).getTime().getMinute(), getLocale(locale).getTime().getSecond());
	}

	/**
	 * Code execution delay.<br>
	 * Automatically activates the command's execution fee if required by the command's settings.
	 */
	default void delay(ServerPlayer player, Locale locale, ThrowingConsumer<PluginCommand, CommandException> consumer) throws CommandException {
		if(getCommandSettings() != null && getCommandSettings().getDelay().getSeconds() > 0 && !player.hasPermission(Permissions.getIgnoreDelayTimer(command()))) {
			CommandPackInstance.getInstance().getPlayersData().getTempData().addCommandTracking(trackingName(), player);
			Sponge.server().scheduler().submit(Task.builder().plugin(getContainer()).interval(1, TimeUnit.SECONDS).execute(new DelayTimerTask(consumer, player, getContainer(), command(), this)).build());
		} else {
			economy(player, locale);
			consumer.accept(this);
		}
	}

	/**
	 * Payment for the execution of the command.
	 */
	default void economy(ServerPlayer player, Locale locale) throws CommandException {
		if(getCommandSettings() == null || !CommandPackInstance.getInstance().getEconomy().isPresent() || player.hasPermission(Permissions.getIgnorePrice(command()))) return;
		Price price = getCommandSettings().getPrice();
		if(price.getMoney() > 0) {
			Currency currency = CommandPackInstance.getInstance().getEconomy().checkCurrency(price.getCurrency());
			BigDecimal money = createDecimal(price.getMoney());
			if(!CommandPackInstance.getInstance().getEconomy().checkPlayerBalance(player.uniqueId(), currency, money) || !CommandPackInstance.getInstance().getEconomy().removeFromPlayerBalance(player, currency, money)) exception(getLocale(player.locale()).getOther().getExecuteCommand().getNoMoney(currency, money, "/" + command()));
		}
	}

	static BigDecimal createDecimal(double value) {
		return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
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

	default <T extends LocaleReference> T getPluginLocale(Class<T> localeClass, Locale locale) {
		return CommandPackInstance.getInstance().getLocales().getLocaleService().getOrDefaultLocale(getContainer(), locale).asReference(localeClass);
	}

	default boolean isEnable() {
		return true;
	}

	private AbstractLocale getLocale(Locale locale) {
		return CommandPackInstance.getInstance().getLocales().getLocale(locale);
	}

}
