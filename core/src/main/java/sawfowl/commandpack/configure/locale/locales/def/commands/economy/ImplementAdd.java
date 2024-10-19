package sawfowl.commandpack.configure.locale.locales.def.commands.economy;

import java.math.BigDecimal;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Economy;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementAdd implements Economy.SubCommand {

	public ImplementAdd() {}

	@Setting("Success")
	@Comment("You can use the following placeholders to display the currency type:\n" + Placeholders.CURRENCY_SYMBOL + " - Displays the currency symbol.\n" + Placeholders.CURRENCY_STYLED_SYMBOL + " - Displays the currency symbol using the design from its name.\n" + Placeholders.CURRENCY_NAME + " - Displays the name of the currency.\n" + Placeholders.CURRENCY_PLURAL_NAME + " - Displays the currency name in the plural.")
	private Component success = TextUtils.deserializeLegacy("&aBalance of &e" + Placeholders.PLAYER + "&a has been increased in '&e" + Placeholders.CURRENCY_NAME + "&a' currency by &e" + Placeholders.VALUE + "&a and is now &e" + Placeholders.MONEY + "&a.");
	@Setting("SuccessUnknown")
	@Comment("You can use the following placeholders to display the currency type:\n" + Placeholders.CURRENCY_SYMBOL + " - Displays the currency symbol.\n" + Placeholders.CURRENCY_STYLED_SYMBOL + " - Displays the currency symbol using the design from its name.\n" + Placeholders.CURRENCY_NAME + " - Displays the name of the currency.\n" + Placeholders.CURRENCY_PLURAL_NAME + " - Displays the currency name in the plural.")
	private Component successUnknown = TextUtils.deserializeLegacy("&aUnknown player nickname&f: &e" + Placeholders.PLAYER + "&a. Created/used a temporary account with the specified name and increased the balance in the '&e" + Placeholders.CURRENCY_NAME + "&a' currency by &e" + Placeholders.VALUE + "&a and is now &e" + Placeholders.MONEY + "&a.");
	@Setting("SuccessTarget")
	@Comment("You can use the following placeholders to display the currency type:\n" + Placeholders.CURRENCY_SYMBOL + " - Displays the currency symbol.\n" + Placeholders.CURRENCY_STYLED_SYMBOL + " - Displays the currency symbol using the design from its name.\n" + Placeholders.CURRENCY_NAME + " - Displays the name of the currency.\n" + Placeholders.CURRENCY_PLURAL_NAME + " - Displays the currency name in the plural.")
	private Component successTarget = TextUtils.deserializeLegacy("&aYour balance in '&e" + Placeholders.CURRENCY_NAME + "&a' currency has been increased in &e" + Placeholders.VALUE + " &aand now equals " + Placeholders.MONEY + "&a.");

	@Override
	public Component getSuccess(Component player, Currency currency, BigDecimal value, BigDecimal money) {
		return Text.of(success).replace(Placeholders.PLAYER, player).replace(Placeholders.CURRENCY_SYMBOL, currency.symbol()).replace(Placeholders.CURRENCY_STYLED_SYMBOL, currency.symbol().color(currency.displayName().color()).style(currency.displayName().style())).replace(Placeholders.CURRENCY_NAME, currency.displayName()).replace(Placeholders.CURRENCY_PLURAL_NAME, currency.pluralDisplayName()).replace(Placeholders.VALUE, value.doubleValue()).replace(Placeholders.MONEY, money.doubleValue()).get();
	}

	@Override
	public Component getSuccessUnknown(Component player, Currency currency, BigDecimal value, BigDecimal money) {
		return Text.of(successUnknown).replace(Placeholders.PLAYER, player).replace(Placeholders.CURRENCY_SYMBOL, currency.symbol()).replace(Placeholders.CURRENCY_STYLED_SYMBOL, currency.symbol().color(currency.displayName().color()).style(currency.displayName().style())).replace(Placeholders.CURRENCY_NAME, currency.displayName()).replace(Placeholders.CURRENCY_PLURAL_NAME, currency.pluralDisplayName()).replace(Placeholders.VALUE, value.doubleValue()).replace(Placeholders.MONEY, money.doubleValue()).get();
	}

	@Override
	public Component getSuccessTarget(Currency currency, BigDecimal value, BigDecimal money) {
		return Text.of(successTarget).replace(Placeholders.CURRENCY_SYMBOL, currency.symbol()).replace(Placeholders.CURRENCY_STYLED_SYMBOL, currency.symbol().color(currency.displayName().color()).style(currency.displayName().style())).replace(Placeholders.CURRENCY_NAME, currency.displayName()).replace(Placeholders.CURRENCY_PLURAL_NAME, currency.pluralDisplayName()).replace(Placeholders.VALUE, value.doubleValue()).replace(Placeholders.MONEY, money.doubleValue()).get();
	}

}
