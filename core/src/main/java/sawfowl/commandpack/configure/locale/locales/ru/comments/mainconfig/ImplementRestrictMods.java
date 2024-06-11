package sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.RestrictMods;

@ConfigSerializable
public class ImplementRestrictMods implements RestrictMods {

	public ImplementRestrictMods() {}

	@Setting("Title")
	private String title = "Эти настройки будут работать только в том случае, если плагин запущен на сервере с Forge.";
	@Setting("Blacklist")
	private String blacklist = "Если значение равно true, игрок не сможет подключиться к серверу, если у него установлен хотя бы 1 из модов, перечисленных в этой настройке.";
	@Setting("ModsList")
	private String modsList = "Этот список содержит только идентификаторы модов.";

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
