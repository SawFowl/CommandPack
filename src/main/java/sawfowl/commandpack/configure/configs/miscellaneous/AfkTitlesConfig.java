package sawfowl.commandpack.configure.configs.miscellaneous;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class AfkTitlesConfig {

	public AfkTitlesConfig(){}

	@Setting
	@Comment("If true, then the player will be shown how much time is left before he will be disconnected from the server.")
	private boolean beforeKick = true;
	@Setting
	@Comment("If true, the player will constantly be sent a title with the message that he is AFK.\nThis option applies only to players who do not have a time limit on the AFK.")
	private boolean unlimit = false;

	public boolean isBeforeKick() {
		return beforeKick;
	}

	public boolean isUnlimit() {
		return unlimit;
	}

}
