package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import java.math.BigDecimal;

import org.spongepowered.api.service.economy.Currency;

import net.kyori.adventure.text.Component;

public interface Economy {

	interface SubCommand {

		Component getSuccess(Component player, Currency currency, BigDecimal value, BigDecimal money);

		Component getSuccessUnknown(Component player, Currency currency, BigDecimal value, BigDecimal money);

		Component getSuccessTarget(Currency currency, BigDecimal value, BigDecimal money);

	}

	SubCommand getAdd();

	SubCommand getRemove();

	SubCommand getSet();

}
