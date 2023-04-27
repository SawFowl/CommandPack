package sawfowl.commandpack.api.data.command;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

public interface CancelRules extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	static CancelRules of(boolean allowMoving, boolean allowOtherCommand) {
		return builder().values(allowMoving, allowOtherCommand).build();
	}

	boolean isAllowMoving();

	boolean isAllowOtherCommand();

	interface Builder extends AbstractBuilder<CancelRules>, org.spongepowered.api.util.Builder<CancelRules, Builder> {

		Builder allowMoving(boolean value);

		Builder allowOtherCommand(boolean value);

		Builder values(boolean allowMoving, boolean allowOtherCommand);

	}

}
