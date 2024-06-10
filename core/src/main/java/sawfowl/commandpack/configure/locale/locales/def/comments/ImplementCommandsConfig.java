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
	@Setting("DelayData")
	private ImplementPrice price = new ImplementPrice();

	@Override
	public DelayData getDelayData() {
		return delayData;
	}

	@Override
	public Price getPrice() {
		return price;
	}

}
