package sawfowl.commandpack.api.data.command;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.configure.configs.commands.RawSettingsImpl;

public interface RawSettings extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	static Builder unregisteredBuilder() {
		return new RawSettingsImpl().builder();
	}

	static RawSettings defaultValues() {
		return new RawSettingsImpl();
	}

	static RawSettings of(UpdateTree updateTree, boolean autoComplete, boolean generateRawTree) {
		return new RawSettingsImpl().builder().setUpdateTree(updateTree).setAutoComplete(autoComplete).setGenerateRawTree(generateRawTree).build();
	}

	UpdateTree getUpdateTree();

	/**
	 * No effect if argument tree construction is enabled. See {@link #isGenerateRawTree()}
	 */
	boolean isAutoComplete();

	/**
	 * If true, the argument tree construction will be performed.<br>
	 * The {@link RawCommand} interface will not use methods for argument autocomplete.
	 */
	boolean isGenerateRawTree();

	interface Builder extends AbstractBuilder<RawSettings>, org.spongepowered.api.util.Builder<RawSettings, Builder> {

		Builder setUpdateTree(UpdateTree updateTree);

		Builder setAutoComplete(boolean value);

		Builder setGenerateRawTree(boolean value);

	}
}
