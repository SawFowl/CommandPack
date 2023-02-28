package sawfowl.commandpack.configure.configs.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Suicide {

	public Suicide(){}

	@Setting(Settings.COOLDOWN)
	private int cooldown = 0;
	@Setting(Settings.DELAY)
	private int delay = 0;
	@Setting(Settings.PRICE)
	private double price = 0;

	public int getCooldown() {
		return cooldown;
	}

	public int getDelay() {
		return delay;
	}

	public double getPrice() {
		return price;
	}

}
