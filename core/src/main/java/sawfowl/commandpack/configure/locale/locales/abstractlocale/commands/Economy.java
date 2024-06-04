package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import java.math.BigDecimal;

import org.spongepowered.api.service.economy.Currency;

import net.kyori.adventure.text.Component;

public interface Economy {

	interface SubCommand {

		Component getSuccess(String player, Currency currency, BigDecimal value);

		Component getSuccessUnknown(String player, Currency currency, BigDecimal value);

		Component getSuccessTarget(Currency currency, BigDecimal value);

	}

	SubCommand getAdd();

	SubCommand getRemove();

	SubCommand getSet();

}
