package sawfowl.commandpack.configure.configs.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Delay {

	public Delay() {}
	public Delay(long seconds) {
		this.seconds = seconds;
	}
	public Delay(long seconds, CancelRules cancelRules) {
		this.seconds = seconds;
		this.cancelRules = cancelRules;
	}

	@Setting("Seconds")
	private long seconds = 0;
	@Setting("CancelRules")
	private CancelRules cancelRules = new CancelRules();

	public long getSeconds() {
		return seconds;
	}

	public CancelRules getCancelRules() {
		return cancelRules;
	}

	@Override
	public String toString() {
		return "Delay [Seconds=" + seconds + ", CancelRules=" + cancelRules + "]";
	}

}
