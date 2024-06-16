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
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawOptional;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Economy;

public class AddToBalance extends AbstractRawCommand {

	private List<BigDecimal> empty;
	public AddToBalance(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		Optional<String> accountName = args.getString(0);
		BigDecimal add = args.getBigDecimal(1).get();
		Currency currency = args.getCurrency(2).orElse(plugin.getEconomy().getEconomyService().defaultCurrency());
		if(accountName.isPresent()) {
			Optional<UniqueAccount> account = plugin.getEconomy().getEconomyService().streamUniqueAccounts().filter(a -> a.identifier().equals(accountName.get())).findFirst();
			if(account.isPresent()) {
				account.get().deposit(currency, add);
				audience.sendMessage(getEconomy(locale).getAdd().getSuccess(account.get().displayName(), currency, add, account.get().balance(currency)));
				Sponge.server().player(account.get().uniqueId()).ifPresent(player -> {
					player.sendMessage(getEconomy(player).getAdd().getSuccessTarget(currency, add, account.get().balance(currency)));
				});
			} else {
				Sponge.server().userManager().load(accountName.get()).thenAccept(optUser -> {
					if(optUser.isPresent()) {
						plugin.getEconomy().getEconomyService().findOrCreateAccount(optUser.get().uniqueId()).get().deposit(currency, add);
						audience.sendMessage(getEconomy(locale).getAdd().getSuccess(account.get().displayName(), currency, add, account.get().balance(currency)));
						optUser.get().player().ifPresent(player -> {
							player.sendMessage(getEconomy(player).getAdd().getSuccessTarget(currency, add, account.get().balance(currency)));
						});
					} else {
						Account temp = plugin.getEconomy().getEconomyService().findOrCreateAccount(accountName.get()).get();
						temp.deposit(currency, add);
						audience.sendMessage(getEconomy(locale).getAdd().getSuccessUnknown(temp.displayName(), currency, add, account.get().balance(currency)));
					}
				});
			}
		} else exception(getExceptions(locale).getUserNotPresent());
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
		return "add";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/eco add <User> <Amount> [Currency]");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		if(empty == null) empty = new ArrayList<BigDecimal>();
		return Arrays.asList(
			RawArgument.of(
				String.class,
				(cause, args) -> plugin.getEconomy().getEconomyService() == null ? Stream.empty() : plugin.getEconomy().getEconomyService().streamUniqueAccounts().map(UniqueAccount::identifier),
				(cause, args) -> args.length >= 1 ? Optional.ofNullable(plugin.getEconomy().getEconomyService().streamUniqueAccounts().map(UniqueAccount::identifier).filter(var -> var.equals(args[0])).findFirst().orElse(args[0])) : Optional.empty(),
				new RawArgumentData<>("Player", CommandTreeNodeTypes.GAME_PROFILE.get().createNode(), 0, null, null),
				new RawOptional(true, false),
				locale -> getExceptions(locale).getUserNotPresent()
			),
			RawArguments.createBigDecimalArgument(empty, new RawBasicArgumentData<BigDecimal>(null, "Money", 1, null, null), null, locale -> getExceptions(locale).getValueNotPresent()),
			RawArguments.createCurrencyArgument(RawBasicArgumentData.createCurrency(2, null, null), RawOptional.optional(), locale -> getExceptions(locale).getValueNotPresent())
		);
	}

	@Override
	public void register(RegisterCommandEvent<Raw> event) {
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

	private Economy getEconomy(Locale locale) {
		return getCommands(locale).getEconomy();
	}

	private Economy getEconomy(ServerPlayer player) {
		return getEconomy(player.locale());
	}

}