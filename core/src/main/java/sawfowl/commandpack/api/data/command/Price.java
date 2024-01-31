package sawfowl.commandpack.api.data.command;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.service.economy.Currency;

import net.kyori.adventure.builder.AbstractBuilder;

/**
 * Setting the command execution price.
 * 
 * @author SawFowl
 */
public interface Price extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	static Price of(String currency, double money) {
		return builder().currency(currency).money(money).build();
	}

	String getCurrency();

	double getMoney();
	
	interface Builder extends AbstractBuilder<Price>, org.spongepowered.api.util.Builder<Price, Builder> {

		Builder currency(Currency currency);

		Builder currency(String currency);

		Builder money(double money);

	}

}
