package sawfowl.commandpack.configure.configs.miscellaneous;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.localeapi.api.LocalisedComment;

@ConfigSerializable
public class RestrictMods {

	public RestrictMods(){}

	@Setting("Enable")
	private boolean enable = false;
	@Setting("Blacklist")
	@LocalisedComment(path = {"Comments", "MainConfig", "RestrictMods", "Blacklist"}, plugin = "commandpack")
	private boolean blackList = true;
	@Setting("Mods")
	@LocalisedComment(path = {"Comments", "MainConfig", "RestrictMods", "ModsList"}, plugin = "commandpack")
	private List<String> mods = new ArrayList<>();

	public boolean isEnable() {
		return enable;
	}

	public boolean isAllowedPlayerMod(String modId) {
		return blackList ? !mods.contains(modId) : mods.contains(modId);
	}

}
