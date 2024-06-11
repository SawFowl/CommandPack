package sawfowl.commandpack.configure.locale.locales.def.comments;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.CommandsConfig;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.commandsconfig.DelayData;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.commandsconfig.Price;
import sawfowl.commandpack.configure.locale.locales.def.comments.commandsconfig.ImplementDelayData;
import sawfowl.commandpack.configure.locale.locales.def.comments.commandsconfig.ImplementPrice;

@ConfigSerializable
public class ImplementCommandsConfig implements CommandsConfig {

	public ImplementCommandsConfig() {}

	@Setting("DelayData")
	private ImplementDelayData delayData = new ImplementDelayData();
	@Setting("Price")
	private ImplementPrice price = new ImplementPrice();
	@Setting("AutoCompleteRaw")
	private String autoCompleteRaw = "If this section is present, it means that this command belongs to the raw commands.\nIf true, then autocompletion of arguments will be available for this command, which may simplify its use by the player.\nIf false, then autocompletion will be unavailable.";

	@Override
	public DelayData getDelayData() {
		return delayData;
	}

	@Override
	public Price getPrice() {
		return price;
	}

	@Override
	public String getAutoCompleteRaw() {
		return autoCompleteRaw;
	}

}
