package sawfowl.commandpack.configure.locale.locales.def.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.RestrictMods;

@ConfigSerializable
public class ImplementRestrictMods implements RestrictMods {

	public ImplementRestrictMods() {}

	@Setting("Blacklist")
	private String blacklist = "If true, the player will not be able to connect to the server if he has at least 1 of the mods listed in these settings installed.\nAlso this parameter affects the filtering of game clients.\nA value of false reverses the behavior of these settings.";
	@Setting("Clients")
	private String clients = "This list contains the names of the clients in lower case.";
	@Setting("ModsList")
	private String modsList = "This list contains only the id of the mods.\nThis setting will only work if the plugin can retrieve the player's mod list.";
	@Setting("Packets")
	private String packets = "An alternative way to filter the mods by the data channels they use.\nIn this setting you need to specify the data channel and the name of the mod that will be shown to the player when disconnecting.\nThis method can be used when the client does not send its mod list to the server.\nThis option always works in blacklist mode, as you need to specify the name of the mod.\nLogging of received packets can be enabled for completion. The required data channels will be displayed in the package data \"minecraft:register\".";

	@Override
	public String getBlacklist() {
		return blacklist;
	}

	@Override
	public String getClients() {
		return clients;
	}

	@Override
	public String getModsList() {
		return modsList;
	}

	@Override
	public String getPackets() {
		return packets;
	}

}
