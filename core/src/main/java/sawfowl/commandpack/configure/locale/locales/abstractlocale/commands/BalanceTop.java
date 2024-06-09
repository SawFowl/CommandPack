package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.service.economy.Currency;

import net.kyori.adventure.text.Component;

public interface BalanceTop {

	Component getTitle(Currency currency);

	Component getPadding();

	Component getElement(Component pos, Component player, Component value);

}
