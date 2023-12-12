package sawfowl.commandpack.api.data.kits;

import java.math.BigDecimal;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.service.economy.Currency;

import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;

/**
 * @author SawFowl
 */
public interface KitPrice extends DataSerializable {

	static KitPrice of(Currency currency, BigDecimal money) {
		return Sponge.game().builderProvider().provide(Builder.class).of(currency, money);
	}

	Currency getCurrency();

	BigDecimal getMoney();

	Component asComponent();

	interface Builder extends AbstractBuilder<KitPrice>, org.spongepowered.api.util.Builder<KitPrice, Builder> {

		KitPrice of(Currency currency, BigDecimal money);

	}
}
