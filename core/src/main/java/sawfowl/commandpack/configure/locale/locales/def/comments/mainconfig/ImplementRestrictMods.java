package sawfowl.commandpack.configure.locale.locales.def.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.RestrictMods;

@ConfigSerializable
public class ImplementRestrictMods implements RestrictMods {

	public ImplementRestrictMods() {}

	@Setting("Title")
	private String title = "These settings will only work if the plugin is running on a server with Forge.";
	@Setting("Blacklist")
	private String blacklist = "If true, the player will not be able to connect to the server if they have at least 1 of the mods listed in this setting.";
	@Setting("ModsList")
	private String modsList = "This list contains only the id of the mods.";

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getBlacklist() {
		return blacklist;
	}

	@Override
	public String getModsList() {
		return modsList;
	}

}
