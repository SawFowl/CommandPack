package sawfowl.commandpack.configure.configs;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Command {

	public static final Command EMPTY = new Command();

	public Command() {}

	public Command(long cooldown, long delay, double price) {
		this.cooldown = cooldown;
		this.delay = delay;
		this.price = price;
	}

	@Setting("Cooldown")
	private long cooldown = 0;
	@Setting("Delay")
	private long delay = 0;
	@Setting("Price")
	private double price = 0;
	@Setting("Enable")
	private boolean enable = true;

	public long getCooldown() {
		return cooldown;
	}

	public long getDelay() {
		return delay;
	}

	public double getPrice() {
		return price;
	}

	public boolean isEnable() {
		return enable;
	}

	@Override
	public String toString() {
		return "Command [cooldown=" + cooldown + ", delay=" + delay + ", price=" + price + ", enable=" + enable + "]";
	}

}
