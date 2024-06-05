package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import com.ibm.icu.math.BigDecimal;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Pay;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementPay implements Pay {

	public ImplementPay() {}

	@Setting("NoPermission")
	@Comment("You can use the following placeholders to display the currency type:\n" + Placeholders.CURRENCY_SYMBOL + " - Displays the currency symbol.\n" + Placeholders.CURRENCY_STYLED_SYMBOL + " - Displays the currency symbol using the design from its name.\n" + Placeholders.CURRENCY_NAME + " - Displays the name of the currency.\n" + Placeholders.CURRENCY_PLURAL_NAME + " - Displays the currency name in the plural.")
	private Component noPerm = TextUtils.deserializeLegacy("&cYou do not have permission to transfer " + Placeholders.CURRENCY_NAME + " currency.");
	@Setting("NotEnoughMoney")
	@Comment("You can use the following placeholders to display the currency type:\n" + Placeholders.CURRENCY_SYMBOL + " - Displays the currency symbol.\n" + Placeholders.CURRENCY_STYLED_SYMBOL + " - Displays the currency symbol using the design from its name.\n" + Placeholders.CURRENCY_NAME + " - Displays the name of the currency.\n" + Placeholders.CURRENCY_PLURAL_NAME + " - Displays the currency name in the plural.")
	private Component notEnoughMoney = TextUtils.deserializeLegacy("&cYou don't have enough money in the " + Placeholders.CURRENCY_NAME + " currency.");
	@Setting("Success")
	@Comment("You can use the following placeholders to display the currency type:\n" + Placeholders.CURRENCY_SYMBOL + " - Displays the currency symbol.\n" + Placeholders.CURRENCY_STYLED_SYMBOL + " - Displays the currency symbol using the design from its name.\n" + Placeholders.CURRENCY_NAME + " - Displays the name of the currency.\n" + Placeholders.CURRENCY_PLURAL_NAME + " - Displays the currency name in the plural.")
	private Component success = TextUtils.deserializeLegacy("&aYou have transferred &e" + Placeholders.CURRENCY_STYLED_SYMBOL + "&e" + Placeholders.MONEY + " &ato the player's account &e" + Placeholders.PLAYER + "&a.");
	@Setting("SuccessTarget")
	@Comment("You can use the following placeholders to display the currency type:\n" + Placeholders.CURRENCY_SYMBOL + " - Displays the currency symbol.\n" + Placeholders.CURRENCY_STYLED_SYMBOL + " - Displays the currency symbol using the design from its name.\n" + Placeholders.CURRENCY_NAME + " - Displays the name of the currency.\n" + Placeholders.CURRENCY_PLURAL_NAME + " - Displays the currency name in the plural.")
	private Component successTarget = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + "&a transfers &e" + Placeholders.CURRENCY_STYLED_SYMBOL + "&e" + Placeholders.MONEY + "&a to your account.");

	@Override
	public Component getNoPermission(Currency currency) {
		return Text.of(noPerm).replace(Placeholders.CURRENCY_SYMBOL, currency.symbol()).replace(Placeholders.CURRENCY_STYLED_SYMBOL, currency.symbol().color(currency.displayName().color()).style(currency.displayName().style())).replace(Placeholders.CURRENCY_NAME, currency.displayName()).replace(Placeholders.CURRENCY_PLURAL_NAME, currency.pluralDisplayName()).get();
	}

	@Override
	public Component getNotEnoughMoney(Currency currency) {
		return Text.of(notEnoughMoney).replace(Placeholders.CURRENCY_SYMBOL, currency.symbol()).replace(Placeholders.CURRENCY_STYLED_SYMBOL, currency.symbol().color(currency.displayName().color()).style(currency.displayName().style())).replace(Placeholders.CURRENCY_NAME, currency.displayName()).replace(Placeholders.CURRENCY_PLURAL_NAME, currency.pluralDisplayName()).get();
	}

	@Override
	public Component getSuccess(Currency currency, BigDecimal value, String target) {
		return Text.of(success).replace(Placeholders.CURRENCY_SYMBOL, currency.symbol()).replace(Placeholders.CURRENCY_STYLED_SYMBOL, currency.symbol().color(currency.displayName().color()).style(currency.displayName().style())).replace(Placeholders.CURRENCY_NAME, currency.displayName()).replace(Placeholders.CURRENCY_PLURAL_NAME, currency.pluralDisplayName()).replace(Placeholders.VALUE, value.doubleValue()).replace(Placeholders.PLAYER, target).get();
	}

	@Override
	public Component getSuccessTarget(String source, Currency currency, BigDecimal value) {
		return Text.of(successTarget).replace(Placeholders.PLAYER, source).replace(Placeholders.CURRENCY_SYMBOL, currency.symbol()).replace(Placeholders.CURRENCY_STYLED_SYMBOL, currency.symbol().color(currency.displayName().color()).style(currency.displayName().style())).replace(Placeholders.CURRENCY_NAME, currency.displayName()).replace(Placeholders.CURRENCY_PLURAL_NAME, currency.pluralDisplayName()).replace(Placeholders.VALUE, value.doubleValue()).get();
	}

}
