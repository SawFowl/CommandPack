package sawfowl.commandpack.configure.configs.miscellaneous;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.localeapi.api.LocalisedComment;

@ConfigSerializable
public class AfkConfig {

	public AfkConfig(){}

	@Setting("TurnOnDelay")
	@LocalisedComment(path = {"Comments", "MainConfig", "Afk", "TurnOnDelay"}, plugin = "commandpack")
	private int turnOnDlay = 300;
	@Setting("KickDelay")
	@LocalisedComment(path = {"Comments", "MainConfig", "Afk", "KickDelay"}, plugin = "commandpack")
	private int kickDelay = 30;
	@Setting("AfkTitles")
	private AfkTitlesConfig afkTitlesConfig = new AfkTitlesConfig();
	@Setting("Enable")
	private boolean enable = true;

	public int getTurnOnDlay() {
		return turnOnDlay;
	}

	public int getKickDelay() {
		return kickDelay;
	}

	public AfkTitlesConfig getAfkTitlesConfig() {
		return afkTitlesConfig;
	}

	public boolean isEnable() {
		return enable;
	}

}
