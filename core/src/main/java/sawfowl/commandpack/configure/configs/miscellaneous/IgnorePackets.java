package sawfowl.commandpack.configure.configs.miscellaneous;

import java.util.Arrays;
import java.util.List;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.localeapi.api.LocalisedComment;

@ConfigSerializable
public class IgnorePackets {

	public IgnorePackets() {}

	@Setting("Enable")
	@LocalisedComment(path = {"Comments", "MainConfig", "IgnorePackets", "Enable"}, plugin = "commandpack")
	private boolean enable = true;
	@Setting("Debug")
	@LocalisedComment(path = {"Comments", "MainConfig", "IgnorePackets", "Debug"}, plugin = "commandpack")
	private boolean debug = false;
	@Setting("Blacklist")
	@LocalisedComment(path = {"Comments", "MainConfig", "IgnorePackets", "Blacklist"}, plugin = "commandpack")
	private boolean blacklist = true;
	@Setting("Packets")
	@LocalisedComment(path = {"Comments", "MainConfig", "IgnorePackets", "Packets"}, plugin = "commandpack")
	private List<String> packets = Arrays.asList("worldedit:cui");

	public boolean isEnable() {
		return enable;
	}

	public boolean isDebug() {
		return debug;
	}

	public boolean canEncode(String packet) {
		return blacklist ? !packets.contains(packet) : packets.contains(packet);
	}

}
