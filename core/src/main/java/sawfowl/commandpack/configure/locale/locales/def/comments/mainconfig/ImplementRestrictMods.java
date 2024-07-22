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
	private String blacklist = "If true, the player will not be able to connect to the server if they have at least 1 of the mods/packages or client listed in these settings.";
	@Setting("Clients")
	private String clients = "This list contains the names of the clients.";
	@Setting("ModsList")
	private String modsList = "This list contains only the id of the mods.";
	@Setting("Packets")
	private String packets = "An alternative way to filter the mods by the data channels they use.\nIn this setting you need to specify the data channel and the name of the mod that will be shown to the player when disconnecting.\nThis method can be used when the client does not send its mod list to the server.";

	@Override
	public String getTitle() {
		return title;
	}

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
