package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import java.math.BigDecimal;

import org.spongepowered.api.service.economy.Currency;

import net.kyori.adventure.text.Component;

public interface Pay {

	Component getNoPermission(Currency currency);

	Component getNotEnoughMoney(Currency currency);

	Component getSuccess(Currency currency, BigDecimal value, Component target);

	Component getSuccessTarget(Component source, Currency currency, BigDecimal value);

}
