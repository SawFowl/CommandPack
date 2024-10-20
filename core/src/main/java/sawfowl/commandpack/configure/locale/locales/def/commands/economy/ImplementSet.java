package sawfowl.commandpack.configure.locale.locales.def.commands.economy;

import java.math.BigDecimal;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Economy;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementSet implements Economy.SubCommand {

	public ImplementSet() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aYou have set player balance &e" + Placeholders.PLAYER + "&a in currency '&e" + Placeholders.CURRENCY_NAME + "&a' = &e" + Placeholders.MONEY + "&a.");
	@Setting("SuccessUnknown")
	private Component successUnknown = TextUtils.deserializeLegacy("&aUnknown player nickname&f: &e" + Placeholders.PLAYER + "&a. Created/used a temporary account with the specified name and changed the balance in currency '&e" + Placeholders.CURRENCY_NAME + "&a' = &e" + Placeholders.MONEY + "&a.");
	@Setting("SuccessTarget")
	private Component successTarget = TextUtils.deserializeLegacy("&aYour balance in '&e" + Placeholders.CURRENCY_NAME + "&a' currency has been changed and is now &e" + Placeholders.MONEY + "&a.");

	@Override
	public Component getSuccess(Component player, Currency currency, BigDecimal value, BigDecimal money) {
		return Text.of(success).replace(Placeholders.PLAYER, player).replace(Placeholders.CURRENCY_SYMBOL, currency.symbol()).replace(Placeholders.CURRENCY_STYLED_SYMBOL, currency.symbol().color(currency.displayName().color()).style(currency.displayName().style())).replace(Placeholders.CURRENCY_NAME, currency.displayName()).replace(Placeholders.CURRENCY_PLURAL_NAME, currency.pluralDisplayName()).replace(Placeholders.MONEY, money.doubleValue()).get();
	}

	@Override
	public Component getSuccessUnknown(Component player, Currency currency, BigDecimal value, BigDecimal money) {
		return Text.of(successUnknown).replace(Placeholders.PLAYER, player).replace(Placeholders.CURRENCY_SYMBOL, currency.symbol()).replace(Placeholders.CURRENCY_STYLED_SYMBOL, currency.symbol().color(currency.displayName().color()).style(currency.displayName().style())).replace(Placeholders.CURRENCY_NAME, currency.displayName()).replace(Placeholders.CURRENCY_PLURAL_NAME, currency.pluralDisplayName()).replace(Placeholders.MONEY, money.doubleValue()).get();
	}

	@Override
	public Component getSuccessTarget(Currency currency, BigDecimal value, BigDecimal money) {
		return Text.of(successTarget).replace(Placeholders.CURRENCY_SYMBOL, currency.symbol()).replace(Placeholders.CURRENCY_STYLED_SYMBOL, currency.symbol().color(currency.displayName().color()).style(currency.displayName().style())).replace(Placeholders.CURRENCY_NAME, currency.displayName()).replace(Placeholders.CURRENCY_PLURAL_NAME, currency.pluralDisplayName()).replace(Placeholders.VALUE, value.doubleValue()).replace(Placeholders.MONEY, money.doubleValue()).get();
	}

}
