package sawfowl.commandpack.configure.configs.miscellaneous;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.localeapi.api.LocalisedComment;

@ConfigSerializable
public class AfkTitlesConfig {

	public AfkTitlesConfig(){}

	@Setting("BeforeKick")
	@LocalisedComment(path = {"Comments", "MainConfig", "Afk", "Titles", "BeforeKick"}, plugin = "commandpack")
	private boolean beforeKick = true;
	@Setting("Unlimit")
	@LocalisedComment(path = {"Comments", "MainConfig", "Afk", "Titles", "Unlimit"}, plugin = "commandpack")
	private boolean unlimit = false;

	public boolean isBeforeKick() {
		return beforeKick;
	}

	public boolean isUnlimit() {
		return unlimit;
	}

}
