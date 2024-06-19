package sawfowl.commandpack.configure.locale.locales.ru.debug;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Debug.DebugPlayerData;

@ConfigSerializable
public class ImplementDebugPlayerData implements DebugPlayerData {

	public ImplementDebugPlayerData() {}

	@Setting("Mods")
	@Comment("Эта функция работает только при наличии Forge на стороне сервера.")
	private String modsList = "Игрок " + Placeholders.PLAYER + " входит на сервер с модами: " + Placeholders.VALUE + ".";
	@Setting("Packets")
	private String packets = "Получен пакет \"" + Placeholders.PACKET + "\" от игрока " + Placeholders.PLAYER + ". Данные пакета '" + Placeholders.VALUE + "'.";


	@Override
	public String getMods(String player, String mods) {
		return modsList.replace(Placeholders.PLAYER, player).replace(Placeholders.VALUE, mods);
	}

	@Override
	public String getPackets(String player, String packet, String data) {
		return packets.replace(Placeholders.PACKET , packet).replace(Placeholders.PLAYER , player).replace(Placeholders.VALUE , data);
	}

}
