package sawfowl.commandpack.commands.raw.economy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

@Register
public class Pay extends AbstractPlayerCommand {

	private List<BigDecimal> empty = new ArrayList<BigDecimal>();
	public Pay(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, String[] args, Mutable arguments) throws CommandException {
		BigDecimal amount = getBigDecimal(args, cause, 1).get().abs();
		UniqueAccount account = getArgument(UniqueAccount.class, cause, args, 0).get();
		Optional<Currency> optCurrency = getCurrency(args, cause, 2);
		if(account.uniqueId().equals(src.uniqueId())) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_TARGET_SELF);
		delay(src, locale, consumer -> {
			if(optCurrency.isPresent()) {
				Currency currency = optCurrency.get();
				if(!src.hasPermission(Permissions.getCurrencyAccess(currency))) exception(getText(locale, LocalesPaths.COMMANDS_PAY_NO_PERM).replace(new String[] {Placeholders.CURRENCY_SYMBOL, Placeholders.CURRENCY_STYLED_SYMBOL, Placeholders.CURRENCY_NAME, Placeholders.CURRENCY_PLURAL_NAME}, currency.symbol(), currency.symbol().style(currency.displayName().style()), currency.displayName(), currency.pluralDisplayName()).get());
				if(!plugin.getEconomy().checkPlayerBalance(src.uniqueId(), currency, amount)) exception(getText(locale, LocalesPaths.COMMANDS_PAY_NOT_ENOUGH_MONEY).replace(new String[] {Placeholders.CURRENCY_SYMBOL, Placeholders.CURRENCY_STYLED_SYMBOL, Placeholders.CURRENCY_NAME, Placeholders.CURRENCY_PLURAL_NAME}, currency.symbol(), currency.symbol().style(currency.displayName().style()), currency.displayName(), currency.pluralDisplayName()).get());
				plugin.getEconomy().getEconomyServiceImpl().findOrCreateAccount(src.uniqueId()).get().transfer(account, currency, amount);
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_PAY_SUCCESS).replace(new String[] {Placeholders.PLAYER, Placeholders.CURRENCY_SYMBOL, Placeholders.CURRENCY_STYLED_SYMBOL, Placeholders.CURRENCY_NAME, Placeholders.CURRENCY_PLURAL_NAME, Placeholders.MONEY}, account.displayName(), currency.symbol(), currency.symbol().style(currency.displayName().style()), currency.displayName(), currency.pluralDisplayName(), text(amount.doubleValue())).get());
				Sponge.server().player(account.uniqueId()).ifPresent(target -> {
					target.sendMessage(getText(target.locale(), LocalesPaths.COMMANDS_PAY_SUCCESS_TARGET).replace(new String[] {Placeholders.PLAYER, Placeholders.CURRENCY_SYMBOL, Placeholders.CURRENCY_STYLED_SYMBOL, Placeholders.CURRENCY_NAME, Placeholders.CURRENCY_PLURAL_NAME, Placeholders.MONEY}, src.get(Keys.CUSTOM_NAME).orElse(text(src.name())), currency.symbol(), currency.symbol().style(currency.displayName().style()), currency.displayName(), currency.pluralDisplayName(), text(amount.doubleValue())).get());
				});
			} else {
				Currency currency = plugin.getEconomy().getEconomyServiceImpl().defaultCurrency();
				if(!plugin.getEconomy().checkPlayerBalance(src.uniqueId(), currency, amount)) exception(getText(locale, LocalesPaths.COMMANDS_PAY_NOT_ENOUGH_MONEY).replace(new String[] {Placeholders.CURRENCY_SYMBOL, Placeholders.CURRENCY_STYLED_SYMBOL, Placeholders.CURRENCY_NAME, Placeholders.CURRENCY_PLURAL_NAME}, currency.symbol(), currency.symbol().style(currency.displayName().style()), currency.displayName(), currency.pluralDisplayName()).get());
				plugin.getEconomy().getEconomyServiceImpl().findOrCreateAccount(src.uniqueId()).get().transfer(account, currency, amount);
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_PAY_SUCCESS).replace(new String[] {Placeholders.PLAYER, Placeholders.CURRENCY_SYMBOL, Placeholders.CURRENCY_STYLED_SYMBOL, Placeholders.CURRENCY_NAME, Placeholders.CURRENCY_PLURAL_NAME, Placeholders.MONEY}, account.displayName(), currency.symbol(), currency.symbol().style(currency.displayName().style()), currency.displayName(), currency.pluralDisplayName(), text(amount.doubleValue())).get());
				Sponge.server().player(account.uniqueId()).ifPresent(target -> {
					target.sendMessage(getText(target.locale(), LocalesPaths.COMMANDS_PAY_SUCCESS_TARGET).replace(new String[] {Placeholders.PLAYER, Placeholders.CURRENCY_SYMBOL, Placeholders.CURRENCY_STYLED_SYMBOL, Placeholders.CURRENCY_NAME, Placeholders.CURRENCY_PLURAL_NAME, Placeholders.MONEY}, src.get(Keys.CUSTOM_NAME).orElse(text(src.name())), currency.symbol(), currency.symbol().style(currency.displayName().style()), currency.displayName(), currency.pluralDisplayName(), text(amount.doubleValue())).get());
				});
			}
		});
	}

	@Override
	public Component shortDescription(Locale locale) {
		return null;
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return null;
	}

	@Override
	public String permission() {
		return Permissions.PAY;
	}

	@Override
	public String command() {
		return "pay";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/pay <User> <Amount> <Currency>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		if(empty == null) empty = new ArrayList<BigDecimal>();
		return Arrays.asList(
			RawArguments.createUniqueAccountArgument(false, false, 0, null, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT),
			RawArguments.createBigDecimalArgument("Money", empty, false, false, 1, null, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			RawArguments.createCurrencyArgument(true, true, 2, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

	@Override
	public void register(RegisterCommandEvent<Raw> event) {
		if(!plugin.getMainConfig().getEconomy().isEnable()) return;
		if(getCommandSettings() == null) {
			event.register(getContainer(), this, command());
		} else {
			if(!getCommandSettings().isEnable()) return;
			if(getCommandSettings().getAliases() != null && getCommandSettings().getAliases().length > 0) {
				event.register(getContainer(), this, command(), getCommandSettings().getAliases());
			} else event.register(getContainer(), this, command());
		}
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
