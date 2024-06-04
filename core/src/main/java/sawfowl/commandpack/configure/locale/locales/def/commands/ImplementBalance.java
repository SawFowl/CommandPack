package sawfowl.commandpack.configure.locale.locales.def.commands;

import java.math.BigDecimal;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Balance;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementBalance implements Balance {

	public ImplementBalance() {}

	@Setting("Title")
	private Component title = TextUtils.deserializeLegacy("&3Your balance");
	@Setting("TitleOther")
	private Component titleOther = TextUtils.deserializeLegacy("&3Balance: " + Placeholders.PLAYER);
	@Setting("Element")
	@Comment("You can use the following placeholders to display the currency type:\n" + Placeholders.CURRENCY_SYMBOL + " - Displays the currency symbol.\n" + Placeholders.CURRENCY_STYLED_SYMBOL + " - Displays the currency symbol using the design from its name.\n" + Placeholders.CURRENCY_NAME + " - Displays the name of the currency.\n" + Placeholders.CURRENCY_PLURAL_NAME + " - Displays the currency name in the plural.")
	private Component element = TextUtils.deserializeLegacy("&a" + Placeholders.CURRENCY_NAME + "&f: &e" + Placeholders.VALUE);
	@Setting("ElementOther")
	@Comment("You can use the following placeholders to display the currency type:\n" + Placeholders.CURRENCY_SYMBOL + " - Displays the currency symbol.\n" + Placeholders.CURRENCY_STYLED_SYMBOL + " - Displays the currency symbol using the design from its name.\n" + Placeholders.CURRENCY_NAME + " - Displays the name of the currency.\n" + Placeholders.CURRENCY_PLURAL_NAME + " - Displays the currency name in the plural.")
	private Component elementOther = TextUtils.deserializeLegacy("&a" + Placeholders.CURRENCY_NAME + "&f: &e" + Placeholders.VALUE);
	@Setting("Hiden")
	private Component hiden = TextUtils.deserializeLegacy("&cThe balance of player &e" + Placeholders.PLAYER + "&c is hidden.");

	@Override
	public Component getTitle() {
		return title;
	}

	@Override
	public Component getTitleOther(String player) {
		return Text.of(titleOther).replace(Placeholders.PLAYER, player).get();
	}

	@Override
	public Component getElement(Currency currency, BigDecimal value) {
		return Text.of(element).replace(Placeholders.CURRENCY_SYMBOL, currency.symbol()).replace(Placeholders.CURRENCY_STYLED_SYMBOL, currency.symbol().color(currency.displayName().color()).style(currency.displayName().style())).replace(Placeholders.CURRENCY_NAME, currency.displayName()).replace(Placeholders.CURRENCY_PLURAL_NAME, currency.pluralDisplayName()).replace(Placeholders.VALUE, value.doubleValue()).get();
	}

	@Override
	public Component getElementOther(Currency currency, BigDecimal value) {
		return Text.of(elementOther).replace(Placeholders.CURRENCY_SYMBOL, currency.symbol()).replace(Placeholders.CURRENCY_STYLED_SYMBOL, currency.symbol().color(currency.displayName().color()).style(currency.displayName().style())).replace(Placeholders.CURRENCY_NAME, currency.displayName()).replace(Placeholders.CURRENCY_PLURAL_NAME, currency.pluralDisplayName()).replace(Placeholders.VALUE, value.doubleValue()).get();
	}

	@Override
	public Component getHiden(String player) {
		return Text.of(hiden).replace(Placeholders.PLAYER, player).get();
	}

}
