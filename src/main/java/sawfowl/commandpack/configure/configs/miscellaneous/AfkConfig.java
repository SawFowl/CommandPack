package sawfowl.commandpack.configure.configs.miscellaneous;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class AfkConfig {

	public AfkConfig(){}

	@Setting("TurnOnDelay")
	@Comment("Delay before AFK status turns on.")
	private int turnOnDlay = 300;
	@Setting("KickDelay")
	@Comment("Time before kick a player with AFK status turned on if he does not have permission for unlimited AFK.")
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
