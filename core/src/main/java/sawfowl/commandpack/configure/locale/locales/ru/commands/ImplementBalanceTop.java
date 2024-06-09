package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.BalanceTop;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementBalanceTop implements BalanceTop {

	public ImplementBalanceTop() {}

	@Setting("Title")
	@Comment("You can use the following placeholders to display the currency type:\n" + Placeholders.CURRENCY_SYMBOL + " - Displays the currency symbol.\n" + Placeholders.CURRENCY_STYLED_SYMBOL + " - Displays the currency symbol using the design from its name.\n" + Placeholders.CURRENCY_NAME + " - Displays the name of the currency.\n" + Placeholders.CURRENCY_PLURAL_NAME + " - Displays the currency name in the plural.")
	private Component title = TextUtils.deserializeLegacy("&3<Top balances &e\"" + Placeholders.CURRENCY_STYLED_SYMBOL + "&e\"&3>");
	@Setting("Padding")
	private Component padding = TextUtils.deserializeLegacy("&3=");
	@Setting("Element")
	private Component element = TextUtils.deserializeLegacy("&2" + Placeholders.RANK + ". &b" + Placeholders.PLAYER + " &6" + Placeholders.VALUE);

	@Override
	public Component getTitle(Currency currency) {
		return Text.of(title).replace(Placeholders.CURRENCY_SYMBOL, currency.symbol()).replace(Placeholders.CURRENCY_STYLED_SYMBOL, currency.symbol().color(currency.displayName().color()).style(currency.displayName().style())).replace(Placeholders.CURRENCY_NAME, currency.displayName()).replace(Placeholders.CURRENCY_PLURAL_NAME, currency.pluralDisplayName()).get();
	}

	@Override
	public Component getPadding() {
		return padding;
	}

	@Override
	public Component getElement(Component pos, Component player, Component value) {
		return Text.of(element).replace(Placeholders.RANK, pos).replace(Placeholders.PLAYER, player).replace(Placeholders.VALUE, value).get();
	}

}
