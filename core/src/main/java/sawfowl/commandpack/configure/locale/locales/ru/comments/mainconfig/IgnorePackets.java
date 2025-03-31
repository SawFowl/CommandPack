package sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class IgnorePackets {

	public IgnorePackets() {}

	@Setting("Title")
	private String title = "Настройки игнорирования прослушки получаемых пакетов.";
	@Setting("Enable")
	private String enable = "Если false, то эти настройки будут отключены.";
	@Setting("Debug")
	private String debug = "Если true, то в консоль будут выводиться все id пакетов.";
	@Setting("Blacklist")
	private String blacklist = "Если true, то список идентификаторов пакетов будет запрещающим.";
	@Setting("Packets")
	private String packets = "Список идентификаторов пакетов. Если Blacklist = true, то получение данных будет применяться только к тем пакетам, чьи идентификаторы указаны в этом списке.";

}
