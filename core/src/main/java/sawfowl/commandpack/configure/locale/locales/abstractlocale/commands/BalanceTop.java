package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import java.math.BigDecimal;

import org.spongepowered.api.service.economy.Currency;

import net.kyori.adventure.text.Component;

public interface BalanceTop {

	Component getTitle(Currency currency);

	Component getPadding();

	Component getElement(String pos, String player, BigDecimal value);

}
