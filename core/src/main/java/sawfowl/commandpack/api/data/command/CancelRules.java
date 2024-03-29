package sawfowl.commandpack.api.data.command;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

import sawfowl.commandpack.configure.configs.commands.CancelRulesData;

/**
 * Rules for cancelling command code execution when using delayed execution.
 * 
 * @author SawFowl
 */
public interface CancelRules extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	static Builder unregisteredBuilder() {
		return new CancelRulesData().builder();
	}

	static CancelRules of(boolean allowMoving, boolean allowOtherCommand) {
		return builder().values(allowMoving, allowOtherCommand).build();
	}

	/**
	 * Whether the player is allowed to move while waiting for a command to be executed.
	 */
	boolean isAllowMoving();

	/**
	 * Whether the player is allowed to use other commands while waiting for the current command to execute.
	 */
	boolean isAllowOtherCommand();

	interface Builder extends AbstractBuilder<CancelRules>, org.spongepowered.api.util.Builder<CancelRules, Builder> {

		Builder allowMoving(boolean value);

		Builder allowOtherCommand(boolean value);

		Builder values(boolean allowMoving, boolean allowOtherCommand);

	}

}
