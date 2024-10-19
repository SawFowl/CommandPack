package sawfowl.commandpack.configure.locale.locales.def.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Afk.Titles;

@ConfigSerializable
public class ImplementTitles implements Titles {

	public ImplementTitles() {}

	@Setting("BeforeKick")
	private String beforeKick = "If true, then the player will be shown how much time is left before he will be disconnected from the server.";
	@Setting("Unlimit")
	private String unlimit = "If true, the player will constantly be sent a title with the message that he is AFK.\nThis option applies only to players who do not have a time limit on the AFK.";

	@Override
	public String getBeforeKick() {
		return beforeKick;
	}

	@Override
	public String getUnlimit() {
		return unlimit;
	}

}
