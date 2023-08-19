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
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawCompleterSupplier;
import sawfowl.commandpack.api.commands.raw.arguments.RawResultSupplier;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class RemoveFromBalance extends AbstractRawCommand {

	private List<BigDecimal> empty;
	public RemoveFromBalance(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		Optional<String> accountName = getString(args, 0);
		BigDecimal remove = getBigDecimal(args, 1).get();
		Currency currency = getCurrency(args, 2).orElse(plugin.getEconomy().getEconomyService().defaultCurrency());
		if(accountName.isPresent()) {
			Optional<UniqueAccount> account = plugin.getEconomy().getEconomyService().streamUniqueAccounts().filter(a -> a.identifier().equals(accountName.get())).findFirst();
			if(account.isPresent()) {
				account.get().withdraw(currency, remove);
				audience.sendMessage(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_REMOVE_BALANCE_SUCCESS_USER), new String[] {Placeholders.PLAYER, Placeholders.CURRENCY_NAME, Placeholders.VALUE, Placeholders.MONEY}, new Component[] {account.get().displayName(), currency.displayName(), text(remove.doubleValue()), text(account.get().balance(currency).doubleValue())}));
				Sponge.server().player(account.get().uniqueId()).ifPresent(player -> {
					player.sendMessage(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_REMOVE_BALANCE_SUCCESS_TARGET), new String[] {Placeholders.CURRENCY_NAME, Placeholders.VALUE, Placeholders.MONEY}, new Component[] {currency.displayName(), text(remove.doubleValue()), text(account.get().balance(currency).doubleValue())}));
				});
			} else {
				Sponge.server().userManager().load(accountName.get()).thenAccept(optUser -> {
					if(optUser.isPresent()) {
						plugin.getEconomy().getEconomyService().findOrCreateAccount(optUser.get().uniqueId()).get().withdraw(currency, remove);
						audience.sendMessage(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_REMOVE_BALANCE_SUCCESS_USER), new String[] {Placeholders.PLAYER, Placeholders.CURRENCY_NAME, Placeholders.VALUE, Placeholders.MONEY}, new Component[] {text(optUser.get().name()), currency.displayName(), text(remove.doubleValue()), text(account.get().balance(currency).doubleValue())}));
						optUser.get().player().ifPresent(player -> {
							player.sendMessage(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_REMOVE_BALANCE_SUCCESS_TARGET), new String[] {Placeholders.CURRENCY_NAME, Placeholders.VALUE, Placeholders.MONEY}, new Component[] {currency.displayName(), text(remove.doubleValue()), text(account.get().balance(currency).doubleValue())}));
						});
					} else {
						Account temp = plugin.getEconomy().getEconomyService().findOrCreateAccount(accountName.get()).get();
						temp.withdraw(currency, remove);
						audience.sendMessage(TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_REMOVE_BALANCE_SUCCESS_CREATE), new String[] {Placeholders.PLAYER, Placeholders.CURRENCY_NAME, Placeholders.VALUE, Placeholders.MONEY}, new Component[] {text(accountName.get()), currency.displayName(), text(remove.doubleValue()), text(temp.balance(currency).doubleValue())}));
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
		return "remove";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/eco remove <User> <Amount> [Currency]");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		if(empty == null) empty = new ArrayList<BigDecimal>();
		return Arrays.asList(RawArgument.of(String.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return plugin.getEconomy().getEconomyService().streamUniqueAccounts().map(UniqueAccount::identifier);
			}
		}, new RawResultSupplier<String>() {
			@Override
			public Optional<String> get(String[] args) {
				return args.length == 0 ? Optional.empty() : Optional.ofNullable(plugin.getEconomy().getEconomyService().streamUniqueAccounts().filter(account -> account.identifier().equals(args[0])).findFirst().map(UniqueAccount::identifier).orElse(args[0]));
			}
		}, true, true, 0, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT),
		RawArguments.createBigDecimalArgument(empty, false, false, 1, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
		RawArguments.createCurrencyArgument(true, true, 2, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT));
	}

	@Override
	public void register(RegisterCommandEvent<Raw> event) {
	}

}
