package sawfowl.commandpack.commands.raw.economy;

import java.math.BigDecimal;
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

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawOptional;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractPlayerCommand;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.utils.CommandsUtil;

@Register
public class Pay extends AbstractPlayerCommand {

	public Pay(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, Mutable arguments, RawArgumentsMap args) throws CommandException {
		BigDecimal amount = args.getBigDecimal(1).get().abs();
		UniqueAccount account = args.<UniqueAccount>get(0).get();
		Optional<Currency> optCurrency = args.getCurrency(2);
		if(account.uniqueId().equals(src.uniqueId())) exception(getExceptions(locale).getTargetSelf());
		delay(src, locale, consumer -> {
			if(optCurrency.isPresent()) {
				Currency currency = optCurrency.get();
				if(!src.hasPermission(Permissions.getCurrencyAccess(currency))) exception(getPay(locale).getNoPermission(currency));
				if(!plugin.getEconomy().checkPlayerBalance(src.uniqueId(), currency, amount)) exception(getPay(locale).getNotEnoughMoney(currency));
				plugin.getEconomy().getEconomyServiceImpl().findOrCreateAccount(src.uniqueId()).get().transfer(account, currency, amount);
				src.sendMessage(getPay(locale).getSuccess(currency, amount, account.displayName()));
				Sponge.server().player(account.uniqueId()).ifPresent(target -> {
					target.sendMessage(getPay(target).getSuccessTarget(src.get(Keys.DISPLAY_NAME).orElse(text(src.name())), currency, amount));
				});
			} else {
				Currency currency = plugin.getEconomy().getEconomyServiceImpl().defaultCurrency();
				if(!plugin.getEconomy().checkPlayerBalance(src.uniqueId(), currency, amount)) exception(getPay(locale).getNotEnoughMoney(currency));
				plugin.getEconomy().getEconomyServiceImpl().findOrCreateAccount(src.uniqueId()).get().transfer(account, currency, amount);
				src.sendMessage(getPay(locale).getSuccess(currency, amount, account.displayName()));
				Sponge.server().player(account.uniqueId()).ifPresent(target -> {
					target.sendMessage(getPay(target).getSuccessTarget(src.get(Keys.DISPLAY_NAME).orElse(text(src.name())), currency, amount));
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
		return Arrays.asList(
			RawArguments.createUniqueAccountArgument(RawBasicArgumentData.createUniqueAccount(0, null, null), null, locale -> getExceptions(locale).getUserNotPresent()),
			RawArguments.createBigDecimalArgument(CommandsUtil.getEmptyList(), new RawBasicArgumentData<>(null, "Money", 1, null, null), null, locale -> getExceptions(locale).getValueNotPresent()),
			RawArguments.createCurrencyArgument(RawBasicArgumentData.createCurrency(2, null, null), RawOptional.optional(), locale -> getExceptions(locale).getValueNotPresent())
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

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Pay getPay(Locale locale) {
		return getCommands(locale).getPay();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Pay getPay(ServerPlayer player) {
		return getPay(player.locale());
	}

}
