package sawfowl.commandpack.commands.raw.economy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Balance;

public class BalanceAll extends AbstractRawCommand {

	private List<BigDecimal> empty;
	public BalanceAll(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		Account account = args.<Account>get(0).get();
		if(isPlayer) {
			ServerPlayer source = (ServerPlayer) audience;
			List<Component> messages = new ArrayList<Component>();
			Component title = getBalance(locale).getTitleOther(account.displayName());
			for(Currency currency : plugin.getEconomy().getEconomyServiceImpl().getCurrencies()) {
				messages.add(getBalance(locale).getElementOther(currency, account.balance(currency)));
			}
			sendPaginationList(source, title, text("=").color(title.color()), 10, messages);
		} else {
			audience.sendMessage(getBalance(locale).getTitleOther(account.displayName()).append(text(":")));
			for(Currency currency : plugin.getEconomy().getEconomyServiceImpl().getCurrencies()) {
				audience.sendMessage(getBalance(locale).getElementOther(currency, account.balance(currency)));
			}
		}
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
		return "balance";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/eco balance <Account>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		if(empty == null) empty = new ArrayList<BigDecimal>();
		return Arrays.asList(RawArguments.createAccountArgument(RawBasicArgumentData.createAccount(0, null, null), null, locale -> getExceptions(locale).getUserNotPresent()));
	}

	@Override
	public void register(RegisterCommandEvent<Raw> event) {
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

	private Balance getBalance(Locale locale) {
		return getCommands(locale).getBalance();
	}

}
