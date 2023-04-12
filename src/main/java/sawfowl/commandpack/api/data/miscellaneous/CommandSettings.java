package sawfowl.commandpack.api.data.miscellaneous;

import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.Command.Raw;
import org.spongepowered.api.data.DataHolderBuilder;
import org.spongepowered.api.data.SerializableDataHolder;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;

import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.configure.configs.commands.CommandPrice;
import sawfowl.commandpack.configure.configs.commands.Delay;

public interface CommandSettings extends SerializableDataHolder.Mutable {

	default Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	String[] getAliases();

	List<String> getAliasesList();

	long getCooldown();

	Delay getDelay();

	CommandPrice getPrice();

	boolean isEnable();

	void registerParameterized(Class<? extends AbstractParameterizedCommand> clazz, RegisterCommandEvent<Parameterized> event);

	void registerRaw(Class<? extends AbstractRawCommand> clazz, RegisterCommandEvent<Raw> event);

	interface Builder extends DataHolderBuilder.Mutable<CommandSettings, Builder> {

		Builder setAliases(List<String> aliases);

		Builder setAliases(String[] aliases);

		Builder setCooldown(long cooldown);

		Builder setDelay(Delay delay);

		Builder setEnable(boolean enable);

		Builder setPrice(CommandPrice price);
		
	}

}
