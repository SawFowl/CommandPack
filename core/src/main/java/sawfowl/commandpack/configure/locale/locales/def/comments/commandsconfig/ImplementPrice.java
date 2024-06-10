package sawfowl.commandpack.configure.locale.locales.def.comments.commandsconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.commandsconfig.Price;

@ConfigSerializable
public class ImplementPrice implements Price {

	public ImplementPrice() {}

	@Setting("Currency")
	private String currency = "The currency used. Both the currency symbol and its name are accepted.\\nIf the specified currency will not be present on the server, the default currency will be used.";
	@Setting("Money")
	private String money = "The price a player will pay for completing a command.";

	@Override
	public String getCurrency() {
		return currency;
	}

	@Override
	public String getMoney() {
		return money;
	}

}
