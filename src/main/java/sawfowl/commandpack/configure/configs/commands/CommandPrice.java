package sawfowl.commandpack.configure.configs.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class CommandPrice {

	public CommandPrice() {}

	public CommandPrice(String currency, double money) {
		this.currency = currency;
		this.money = money;
	}

	@Setting("Currency")
	@Comment("The currency used. Both the currency symbol and its name are accepted.\nIf the specified currency will not be present on the server, the default currency will be used.")
	private String currency = "$";
	@Setting("Money")
	@Comment("The price a player will pay for completing a command.")
	private double money = 0;

	public String getCurrency() {
		return currency;
	}

	public double getMoney() {
		return money;
	}

}
