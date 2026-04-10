package sawfowl.commandpack.configure.locales.ru.comments.mainconfig;

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
	@Setting("Whitelist")
	private String whitelist = "Если false, то список идентификаторов пакетов будет запрещающим.";
	@Setting("Packets")
	private String packets = "Список идентификаторов пакетов. Если Whitelist = true, то получение данных будет применяться только к тем пакетам, чьи идентификаторы указаны в этом списке.";

}
