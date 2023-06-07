package sawfowl.commandpack.configure.configs.miscellaneous;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class AfkConfig {

	public AfkConfig(){}

	@Setting("TurnOnDlay")
	private int turnOnDlay = 300;
	@Setting("KickDelay")
	private int kickDelay = 30;
	@Setting("AfkTitles")
	private AfkTitlesConfig afkTitlesConfig = new AfkTitlesConfig();

	public int getTurnOnDlay() {
		return turnOnDlay;
	}

	public int getKickDelay() {
		return kickDelay;
	}

	public AfkTitlesConfig getAfkTitlesConfig() {
		return afkTitlesConfig;
	}

}
