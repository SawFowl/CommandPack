package sawfowl.commandpack.configure.locale.locales.ru.commands.economy;

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
	@Comment("Вы можете использовать следующие плейсхолдеры для отображения валюты:\n" + Placeholders.CURRENCY_SYMBOL + " - Отображение символа валюты.\n" + Placeholders.CURRENCY_STYLED_SYMBOL + " - Отображение символа валюты с применением стиля из ее имени.\n" + Placeholders.CURRENCY_NAME + " - Отображение имени валюты.\n" + Placeholders.CURRENCY_PLURAL_NAME + " - Отображение имени валюты в множественном числе.")
	private Component success = TextUtils.deserializeLegacy("&aБаланс &e" + Placeholders.PLAYER + "&a увеличен в валюте '&e" + Placeholders.CURRENCY_NAME + "&a' на &e" + Placeholders.VALUE + "&a и теперь составляет &e" + Placeholders.MONEY + "&a.");
	@Setting("SuccessUnknown")
	@Comment("Вы можете использовать следующие плейсхолдеры для отображения валюты:\n" + Placeholders.CURRENCY_SYMBOL + " - Отображение символа валюты.\n" + Placeholders.CURRENCY_STYLED_SYMBOL + " - Отображение символа валюты с применением стиля из ее имени.\n" + Placeholders.CURRENCY_NAME + " - Отображение имени валюты.\n" + Placeholders.CURRENCY_PLURAL_NAME + " - Отображение имени валюты в множественном числе.")
	private Component successUnknown = TextUtils.deserializeLegacy("&aНеизвестный ник игрока&f: &e" + Placeholders.PLAYER + "&a. Создание/использование временного аккаунта с указанным именем и увеличение баланса в валюте '&e" + Placeholders.CURRENCY_NAME + "&a' на &e" + Placeholders.VALUE + "&a. Теперь баланс равен &e" + Placeholders.MONEY + "&a.");
	@Setting("SuccessTarget")
	@Comment("Вы можете использовать следующие плейсхолдеры для отображения валюты:\n" + Placeholders.CURRENCY_SYMBOL + " - Отображение символа валюты.\n" + Placeholders.CURRENCY_STYLED_SYMBOL + " - Отображение символа валюты с применением стиля из ее имени.\n" + Placeholders.CURRENCY_NAME + " - Отображение имени валюты.\n" + Placeholders.CURRENCY_PLURAL_NAME + " - Отображение имени валюты в множественном числе.")
	private Component successTarget = TextUtils.deserializeLegacy("&aВаш баланс в валюте '&e" + Placeholders.CURRENCY_NAME + "&a' увеличен на &e" + Placeholders.VALUE + " &aи теперь составляет " + Placeholders.MONEY + "&a.");

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
