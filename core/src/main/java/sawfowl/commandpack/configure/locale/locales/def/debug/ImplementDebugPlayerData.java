package sawfowl.commandpack.configure.locale.locales.def.debug;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Debug.DebugPlayerData;

@ConfigSerializable
public class ImplementDebugPlayerData implements DebugPlayerData {

	public ImplementDebugPlayerData() {}

	@Setting("Mods")
	@Comment("This feature only works if you have Forge on the server side.")
	private String modsList = "The player " + Placeholders.PLAYER + " login to the server with the mods: " + Placeholders.VALUE + ".";
	@Setting("Packets")
	private String packets = "Received \"" + Placeholders.PACKET + "\" packet from player" + Placeholders.PLAYER + ". Package data: '" + Placeholders.VALUE + "'.";


	@Override
	public String getMods(String player, String mods) {
		return modsList.replace(Placeholders.PLAYER, player).replace(Placeholders.VALUE, mods);
	}

	@Override
	public String getPackets(String player, String packet, String data) {
		return packets.replace(Placeholders.PACKET , packet).replace(Placeholders.PLAYER , player).replace(Placeholders.VALUE , data);
	}

}
