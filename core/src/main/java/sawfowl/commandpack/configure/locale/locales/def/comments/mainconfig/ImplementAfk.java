package sawfowl.commandpack.configure.locale.locales.def.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Afk;

@ConfigSerializable
public class ImplementAfk implements Afk {

	public ImplementAfk() {}
	@Setting("Titles")
	private ImplementTitles titles = new ImplementTitles();
	@Setting("TurnOnDelay")
	private String turnOnDelay = "Delay before AFK status turns on.";
	@Setting("KickDelay")
	private String kickDelay = "Time before kick a player with AFK status turned on if he does not have permission for unlimited AFK.";

	@Override
	public Titles getTitles() {
		return titles;
	}

	@Override
	public String getTurnOnDelay() {
		return turnOnDelay;
	}

	@Override
	public String getKickDelay() {
		return kickDelay;
	}

}
