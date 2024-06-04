package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.service.economy.Currency;

import com.ibm.icu.math.BigDecimal;

import net.kyori.adventure.text.Component;

public interface Pay {

	Component getNoPermission(Currency currency);

	Component getNotEnoughMoney(Currency currency);

	Component getSuccess(Currency currency, BigDecimal value, String target);

	Component getSuccessTarget(String source, Currency currency, BigDecimal value);

}
