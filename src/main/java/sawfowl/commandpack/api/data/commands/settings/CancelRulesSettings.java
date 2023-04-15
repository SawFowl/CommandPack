package sawfowl.commandpack.api.data.commands.settings;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

public interface CancelRulesSettings extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	static CancelRulesSettings of(boolean allowMoving, boolean allowOtherCommand) {
		return builder().values(allowMoving, allowOtherCommand).build();
	}

	boolean isAllowMoving();

	boolean isAllowOtherCommand();

	interface Builder extends AbstractBuilder<CancelRulesSettings>, org.spongepowered.api.util.Builder<CancelRulesSettings, Builder> {

		Builder allowMoving(boolean value);

		Builder allowOtherCommand(boolean value);

		Builder values(boolean allowMoving, boolean allowOtherCommand);

	}

}
