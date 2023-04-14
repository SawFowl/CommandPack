package sawfowl.commandpack.api.data.commands.settings;

import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.builder.AbstractBuilder;
import sawfowl.commandpack.configure.configs.commands.CommandPrice;
import sawfowl.commandpack.configure.configs.commands.Delay;

@ConfigSerializable
public interface CommandSettings extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	String[] getAliases();

	List<String> getAliasesList();

	long getCooldown();

	Delay getDelay();

	CommandPrice getPrice();

	boolean isEnable();

	interface Builder extends AbstractBuilder<CommandSettings>, org.spongepowered.api.util.Builder<CommandSettings, Builder> {

		Builder setAliases(List<String> aliases);

		Builder setAliases(String[] aliases);

		Builder setCooldown(long cooldown);

		Builder setDelay(DelaySettings delay);

		Builder setEnable(boolean enable);

		Builder setPrice(PriceSettings price);
		
	}

}
