package sawfowl.commandpack.api.data.command;

import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

@ConfigSerializable
public interface Settings extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	String[] getAliases();

	List<String> getAliasesList();

	long getCooldown();

	Delay getDelay();

	Price getPrice();

	boolean isEnable();

	interface Builder extends AbstractBuilder<Settings>, org.spongepowered.api.util.Builder<Settings, Builder> {

		Builder setAliases(List<String> aliases);

		Builder setAliases(String[] aliases);

		Builder setCooldown(long cooldown);

		Builder setDelay(Delay delay);

		Builder setEnable(boolean enable);

		Builder setPrice(Price price);
		
	}

}
