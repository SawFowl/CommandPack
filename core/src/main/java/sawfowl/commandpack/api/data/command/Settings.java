package sawfowl.commandpack.api.data.command;

import java.util.List;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

import sawfowl.commandpack.configure.configs.commands.CommandSettings;

/**
 * Additional command settings.
 * 
 * @author SawFowl
 */
public interface Settings extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	/**
	 * Builders register later than the commands.<br>
	 * Use this method as needed.
	 */
	static Builder unregisteredBuilder() {
		return new CommandSettings().builder();
	}

	/**
	 * Command aliases.
	 */
	String[] getAliases();

	/**
	 * Command aliases.
	 */
	List<String> getAliasesList();

	/**
	 * Whether the command contains the specified alias.
	 */
	public boolean containsAlias(String value);

	/**
	 * The time between uses of the command.
	 */
	long getCooldown();

	/**
	 * Delayed command execution.
	 */
	Delay getDelay();

	/**
	 * Command price.
	 */
	Price getPrice();

	/**
	 * Whether the registration of the command is enabled.
	 */
	boolean isEnable();

	public Optional<Boolean> isAutoComplete();

	interface Builder extends AbstractBuilder<Settings>, org.spongepowered.api.util.Builder<Settings, Builder> {

		Builder setAliases(List<String> aliases);

		Builder setAliases(String... aliases);

		Builder setCooldown(long cooldown);

		Builder setDelay(Delay delay);

		Builder setEnable(boolean enable);

		Builder setPrice(Price price);

		Builder setAutoComplete(Boolean value);

	}

}
