package sawfowl.commandpack.api.data.commands.settings;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.service.economy.Currency;

import net.kyori.adventure.builder.AbstractBuilder;

public interface PriceSettings extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}
	
	interface Builder extends AbstractBuilder<PriceSettings>, org.spongepowered.api.util.Builder<PriceSettings, Builder> {

		Builder currency(Currency currency);

		Builder currency(String currency);

		Builder money(double money);

	}

	String getCurrency();

	double getMoney();

}
