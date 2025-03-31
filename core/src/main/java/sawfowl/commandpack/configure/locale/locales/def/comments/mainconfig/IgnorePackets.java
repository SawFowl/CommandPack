package sawfowl.commandpack.configure.locale.locales.def.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class IgnorePackets {

	public IgnorePackets() {}

	@Setting("Title")
	private String title = "Settings to ignore listening for received packets.";
	@Setting("Enable")
	private String enable = "If false, these settings will be disabled.";
	@Setting("Debug")
	private String debug = "If true, all package ids will be output to the console.";
	@Setting("Whitelist")
	private String whitelist = "If false, the list of packet IDs will be prohibitive.";
	@Setting("Packets")
	private String packets = "A list of packet identifiers. If Blacklist = true, then data retrieval from packets will be applied only to those packets whose identifiers are specified in this list.";

}
