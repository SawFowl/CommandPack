package sawfowl.commandpack.configure.configs.miscellaneous;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.localeapi.api.LocalisedComment;

@ConfigSerializable
public class DebugPlayerData {

	public DebugPlayerData() {}

	@Setting("Mods")
	@LocalisedComment(path = {"Comments", "MainConfig", "DebugMods"}, plugin = "commandpack")
	private boolean mods = true;
	@Setting("Packets")
	@LocalisedComment(path = {"Comments", "MainConfig", "DebugPackets"}, plugin = "commandpack")
	private boolean packets = false;

	public boolean mods() {
		return mods;
	}

	public boolean packets() {
		return packets;
	}

}
