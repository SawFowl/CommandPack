package sawfowl.commandpack.api.data.commands.settings;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

public interface DelaySettings extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	static DelaySettings of(long seconds, CancelRulesSettings rules) {
		return builder().setSeconds(seconds).setCancelRules(rules).build();
	}

	long getSeconds();

	CancelRulesSettings getCancelRules();

	interface Builder extends AbstractBuilder<DelaySettings>, org.spongepowered.api.util.Builder<DelaySettings, Builder> {

		Builder setSeconds(long seconds);

		Builder setCancelRules(CancelRulesSettings rules);

	}

}
