package sawfowl.commandpack.api.data.command;

import java.util.List;

import javax.annotation.Nullable;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

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
	 * Command aliases.
	 */
	String[] getAliases();

	/**
	 * Command aliases.
	 */
	List<String> getAliasesList();

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

	/**
	 * For `Raw` commands only.<br>
	 * No effect if argument tree construction is enabled. See {@link #isGenerateRawTree()}
	 */
	@Nullable Boolean isAutocomplete();

	/**
	 * For `Raw` commands only.<br>
	 */
	@Nullable Boolean isGenerateRawTree();

	interface Builder extends AbstractBuilder<Settings>, org.spongepowered.api.util.Builder<Settings, Builder> {

		Builder setAliases(List<String> aliases);

		Builder setAliases(String... aliases);

		Builder setCooldown(long cooldown);

		Builder setDelay(Delay delay);

		Builder setEnable(boolean enable);

		Builder setPrice(Price price);

		Builder setRawAutoComplete(Boolean enable);

		Builder generateRawCommandTree(Boolean enable);

	}

}
