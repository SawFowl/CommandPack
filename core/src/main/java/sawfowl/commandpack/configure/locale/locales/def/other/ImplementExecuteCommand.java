package sawfowl.commandpack.configure.locale.locales.def.other;

import java.math.BigDecimal;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Other.ExecuteCommand;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementExecuteCommand implements ExecuteCommand {

	public ImplementExecuteCommand() {}

	@Setting("OtherCommand")
	private Component otherCommand = TextUtils.deserializeLegacy("&cExecution of command &e\"&5" + Placeholders.COMMAND + "&e\"&c has stopped because you entered another command.");
	@Setting("Moving")
	private Component moving = TextUtils.deserializeLegacy("&cExecution of command &e\"&5" + Placeholders.COMMAND + "&e\"&c is stopped because you have moved.");
	@Setting("NoMoney")
	private Component noMoney = TextUtils.deserializeLegacy("&cYou need to have a balance of &2" + Placeholders.CURRENCY_STYLED_SYMBOL + Placeholders.MONEY + "&c to execute command &e\"&5" + Placeholders.COMMAND + "&e\"&c.");
	@Setting("TakeMoney")
	private Component takeMoney = TextUtils.deserializeLegacy("&eWithdrawn money from your account &2" + Placeholders.CURRENCY_STYLED_SYMBOL + Placeholders.MONEY + "&e for executing the &e\"&5" + Placeholders.COMMAND + "&e\" command.");

	@Override
	public Component getOtherCommand(String command) {
		return Text.of(otherCommand).replace(Placeholders.COMMAND, command).get();
	}

	@Override
	public Component getMoving(String command) {
		return Text.of(moving).replace(Placeholders.COMMAND, command).get();
	}

	@Override
	public Component getNoMoney(Currency currency, BigDecimal money, String command) {
		return Text.of(otherCommand).replace(Placeholders.CURRENCY_SYMBOL, currency.symbol()).replace(Placeholders.CURRENCY_STYLED_SYMBOL, currency.symbol().color(currency.displayName().color()).style(currency.displayName().style())).replace(Placeholders.CURRENCY_NAME, currency.displayName()).replace(Placeholders.CURRENCY_PLURAL_NAME, currency.pluralDisplayName()).replace(Placeholders.MONEY, money.doubleValue()).replace(Placeholders.COMMAND, command).get();
	}

	@Override
	public Component getTakeMoney(Currency currency, BigDecimal money, String command) {
		return Text.of(takeMoney).replace(Placeholders.CURRENCY_SYMBOL, currency.symbol()).replace(Placeholders.CURRENCY_STYLED_SYMBOL, currency.symbol().color(currency.displayName().color()).style(currency.displayName().style())).replace(Placeholders.CURRENCY_NAME, currency.displayName()).replace(Placeholders.CURRENCY_PLURAL_NAME, currency.pluralDisplayName()).replace(Placeholders.MONEY, money.doubleValue()).replace(Placeholders.COMMAND, command).get();
	}

}
