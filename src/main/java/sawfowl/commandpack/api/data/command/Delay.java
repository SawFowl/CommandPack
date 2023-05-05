package sawfowl.commandpack.api.data.command;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

/**
 * Setting the command execution delay.
 * 
 * @author SawFowl
 */
public interface Delay extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	static Delay of(long seconds, CancelRules rules) {
		return builder().setSeconds(seconds).setCancelRules(rules).build();
	}

	long getSeconds();

	CancelRules getCancelRules();

	interface Builder extends AbstractBuilder<Delay>, org.spongepowered.api.util.Builder<Delay, Builder> {

		Builder setSeconds(long seconds);

		Builder setCancelRules(CancelRules rules);

	}

}
