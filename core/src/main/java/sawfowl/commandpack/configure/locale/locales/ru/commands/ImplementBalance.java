package sawfowl.commandpack.configure.locale.locales.ru.commands;

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
	private Component title = TextUtils.deserializeLegacy("&3Ваш баланс");
	@Setting("TitleOther")
	private Component titleOther = TextUtils.deserializeLegacy("&3Баланс: " + Placeholders.PLAYER);
	@Setting("Element")
	@Comment("Вы можете использовать следующие плейсхолдеры для отображения валюты:\n" + Placeholders.CURRENCY_SYMBOL + " - Отображение символа валюты.\n" + Placeholders.CURRENCY_STYLED_SYMBOL + " - Отображение символа валюты с применением стиля из ее имени.\n" + Placeholders.CURRENCY_NAME + " - Отображение имени валюты.\n" + Placeholders.CURRENCY_PLURAL_NAME + " - Отображение имени валюты в множественном числе.")
	private Component element = TextUtils.deserializeLegacy("&a" + Placeholders.CURRENCY_NAME + "&f: &e" + Placeholders.VALUE);
	@Setting("ElementOther")
	@Comment("Вы можете использовать следующие плейсхолдеры для отображения валюты:\n" + Placeholders.CURRENCY_SYMBOL + " - Отображение символа валюты.\n" + Placeholders.CURRENCY_STYLED_SYMBOL + " - Отображение символа валюты с применением стиля из ее имени.\n" + Placeholders.CURRENCY_NAME + " - Отображение имени валюты.\n" + Placeholders.CURRENCY_PLURAL_NAME + " - Отображение имени валюты в множественном числе.")
	private Component elementOther = TextUtils.deserializeLegacy("&a" + Placeholders.CURRENCY_NAME + "&f: &e" + Placeholders.VALUE);
	@Setting("Hiden")
	private Component hiden = TextUtils.deserializeLegacy("&cБаланс игрока &e" + Placeholders.PLAYER + "&c скрыт.");

	@Override
	public Component getTitle() {
		return title;
	}

	@Override
	public Component getTitleOther(Component player) {
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
	public Component getHiden(Component player) {
		return Text.of(hiden).replace(Placeholders.PLAYER, player).get();
	}

}
