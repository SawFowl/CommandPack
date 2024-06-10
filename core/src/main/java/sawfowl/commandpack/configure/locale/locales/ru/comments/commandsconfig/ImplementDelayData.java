package sawfowl.commandpack.configure.locale.locales.ru.comments.commandsconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.commandsconfig.DelayData;
import sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig.ImplementCancelRules;

@ConfigSerializable
public class ImplementDelayData implements DelayData {

	public ImplementDelayData() {}

	@Setting("CancelRules")
	private ImplementCancelRules cancelRules = new ImplementCancelRules();
	@Setting("Seconds")
	private String seconds = "Time in seconds until the command is activated, unless the player has permission to ignore this delay.";

	@Override
	public CancelRules getCancelRules() {
		return cancelRules;
	}

	@Override
	public String getSeconds() {
		return seconds;
	}

}
