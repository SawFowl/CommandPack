package sawfowl.commandpack.configure.configs.miscellaneous;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class RestrictMods {

	public RestrictMods(){}

	@Setting("Enable")
	private boolean enable = false;
	@Setting("Blacklist")
	private boolean blackList = true;
	@Setting("Mods")
	@Comment("This list contains only the id of the mods.")
	private List<String> mods = new ArrayList<>();

	public boolean isEnable() {
		return enable;
	}

	public boolean isAllowedPlayerMod(String modId) {
		return blackList ? !mods.contains(modId) : mods.contains(modId);
	}

}
