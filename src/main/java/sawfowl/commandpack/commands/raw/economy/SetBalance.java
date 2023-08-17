package sawfowl.commandpack.commands.raw.economy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.service.economy.Currency;
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
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class SetBalance extends AbstractRawCommand {

	private List<BigDecimal> empty;
	public SetBalance(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		Optional<String> accountName = getString(args, 0);
		BigDecimal newValue = getBigDecimal(args, 1).get();
		Currency currency = getCurrency(args, 0).get();
		if(accountName.isPresent()) {
			Optional<UniqueAccount> account = plugin.getEconomy().getEconomyService().streamUniqueAccounts().filter(a -> a.identifier().equals(accountName.get())).findFirst();
			if(account.isPresent()) {
				account.get().setBalance(currency, newValue);
				// Добавить сообщение
			} else {
				plugin.getEconomy().getEconomyService().findOrCreateAccount(accountName.get()).get().setBalance(currency, newValue);
				// Добавить сообщение
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
		return Permissions.BALANCE_STAFF;
	}

	@Override
	public String command() {
		return "setbalance";
	}

	@Override
	public Component usage(CommandCause cause) {
		return null;
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

}
