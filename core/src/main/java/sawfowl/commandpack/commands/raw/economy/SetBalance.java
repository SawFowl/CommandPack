package sawfowl.commandpack.commands.raw.economy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.command.registrar.tree.CommandTreeNodeTypes;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class SetBalance extends AbstractRawCommand {

	private List<BigDecimal> empty = new ArrayList<BigDecimal>();
	public SetBalance(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		Optional<String> accountName = getString(args, 0);
		BigDecimal newValue = getBigDecimal(args, 1).get();
		Currency currency = getCurrency(args, 2).orElse(plugin.getEconomy().getEconomyService().defaultCurrency());
		if(accountName.isPresent()) {
			Optional<UniqueAccount> account = plugin.getEconomy().getEconomyService().streamUniqueAccounts().filter(a -> a.identifier().equals(accountName.get())).findFirst();
			if(account.isPresent()) {
				account.get().setBalance(currency, newValue);
				audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_SET_BALANCE_SUCCESS_USER).replace(new String[] {Placeholders.PLAYER, Placeholders.CURRENCY_NAME, Placeholders.CURRENCY_PLURAL_NAME, Placeholders.CURRENCY_SYMBOL, Placeholders.CURRENCY_STYLED_SYMBOL, Placeholders.VALUE}, account.get().displayName(), currency.displayName(), currency.pluralDisplayName(), currency.symbol(), currency.symbol().style(currency.pluralDisplayName().style()), text(newValue.doubleValue())).get());
				Sponge.server().player(account.get().uniqueId()).ifPresent(player -> {
					player.sendMessage(getText(locale, LocalesPaths.COMMANDS_SET_BALANCE_SUCCESS_TARGET).replace(new String[] {Placeholders.CURRENCY_NAME, Placeholders.CURRENCY_PLURAL_NAME, Placeholders.CURRENCY_SYMBOL, Placeholders.CURRENCY_STYLED_SYMBOL, Placeholders.MONEY}, currency.displayName(), currency.pluralDisplayName(), currency.symbol(), currency.symbol().style(currency.pluralDisplayName().style()), text(newValue.doubleValue())).get());
				});
			} else {
				Sponge.server().userManager().load(accountName.get()).thenAccept(optUser -> {
					if(optUser.isPresent()) {
						plugin.getEconomy().getEconomyService().findOrCreateAccount(optUser.get().uniqueId()).get().setBalance(currency, newValue);
						audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_SET_BALANCE_SUCCESS_USER).replace(new String[] {Placeholders.PLAYER, Placeholders.CURRENCY_NAME, Placeholders.CURRENCY_PLURAL_NAME, Placeholders.CURRENCY_SYMBOL, Placeholders.CURRENCY_STYLED_SYMBOL, Placeholders.VALUE}, text(optUser.get().name()), currency.displayName(), currency.pluralDisplayName(), currency.symbol(), currency.symbol().style(currency.pluralDisplayName().style()), text(newValue.doubleValue())).get());
						optUser.get().player().ifPresent(player -> {
							player.sendMessage(getText(locale, LocalesPaths.COMMANDS_SET_BALANCE_SUCCESS_TARGET).replace(new String[] {Placeholders.CURRENCY_NAME, Placeholders.CURRENCY_PLURAL_NAME, Placeholders.CURRENCY_SYMBOL, Placeholders.CURRENCY_STYLED_SYMBOL, Placeholders.MONEY}, currency.displayName(), currency.pluralDisplayName(), currency.symbol(), currency.symbol().style(currency.pluralDisplayName().style()), text(newValue.doubleValue())).get());
						});
					} else {
						plugin.getEconomy().getEconomyService().findOrCreateAccount(accountName.get()).get().setBalance(currency, newValue);
						audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_SET_BALANCE_SUCCESS_CREATE).replace(new String[] {Placeholders.PLAYER, Placeholders.CURRENCY_NAME, Placeholders.CURRENCY_PLURAL_NAME, Placeholders.CURRENCY_SYMBOL, Placeholders.CURRENCY_STYLED_SYMBOL, Placeholders.VALUE}, text(accountName.get()), currency.displayName(), currency.pluralDisplayName(), currency.symbol(), currency.symbol().style(currency.pluralDisplayName().style()), text(newValue.doubleValue())).get());
					}
				});
			}
		} else exception(locale, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT);
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
		return Permissions.ECONOMY_STAFF;
	}

	@Override
	public String command() {
		return "set";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/eco set <User> <Amount> [Currency]");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		if(empty == null) empty = new ArrayList<BigDecimal>();
		return Arrays.asList(
				RawArgument.of(
					String.class,
					CommandTreeNodeTypes.GAME_PROFILE.get().createNode(),
					(cause, args) -> plugin.getEconomy().getEconomyService() == null ? Stream.empty() : plugin.getEconomy().getEconomyService().streamUniqueAccounts().map(UniqueAccount::identifier),
					(cause, args) -> args.length >= 1 ? Optional.ofNullable(plugin.getEconomy().getEconomyService().streamUniqueAccounts().map(UniqueAccount::identifier).filter(var -> var.equals(args[0])).findFirst().orElse(args[0])) : Optional.empty(),
					"Player",
					true,
					true,
					0,
					null,
				LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT
			),
			RawArguments.createBigDecimalArgument("Money", empty, false, false, 1, null, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			RawArguments.createCurrencyArgument(true, true, 2, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
