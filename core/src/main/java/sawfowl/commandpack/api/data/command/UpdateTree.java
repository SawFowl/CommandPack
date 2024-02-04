package sawfowl.commandpack.api.data.command;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

import sawfowl.commandpack.configure.configs.commands.UpdateRawTree;

public interface UpdateTree extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	static UpdateTree of(boolean enable, long interval) {
		return builder().setEnable(enable).setInterval(interval).build();
	}

	static UpdateTree defaultValues() {
		return new UpdateRawTree();
	}

	boolean isEnable();

	long getInterval();

	interface Builder extends AbstractBuilder<UpdateTree>, org.spongepowered.api.util.Builder<UpdateTree, Builder> {

		Builder setEnable(boolean value);

		Builder setInterval(long value);

	}

}
