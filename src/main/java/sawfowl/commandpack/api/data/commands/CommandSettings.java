package sawfowl.commandpack.api.data.commands;

import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataHolderBuilder;
import org.spongepowered.api.data.SerializableDataHolder;

import sawfowl.commandpack.configure.configs.commands.CommandPrice;
import sawfowl.commandpack.configure.configs.commands.Delay;

public interface CommandSettings extends SerializableDataHolder.Mutable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	String[] getAliases();

	List<String> getAliasesList();

	long getCooldown();

	Delay getDelay();

	CommandPrice getPrice();

	boolean isEnable();

	interface Builder extends DataHolderBuilder.Mutable<CommandSettings, Builder> {

		Builder setAliases(List<String> aliases);

		Builder setAliases(String[] aliases);

		Builder setCooldown(long cooldown);

		Builder setDelay(Delay delay);

		Builder setEnable(boolean enable);

		Builder setPrice(CommandPrice price);
		
	}

}
