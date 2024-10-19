package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import java.math.BigDecimal;

import org.spongepowered.api.service.economy.Currency;

import net.kyori.adventure.text.Component;

public interface Balance {

	Component getTitle();

	Component getTitleOther(Component player);

	Component getElement(Currency currency, BigDecimal value);

	Component getElementOther(Currency currency, BigDecimal value);

	Component getHiden(Component player);

}
