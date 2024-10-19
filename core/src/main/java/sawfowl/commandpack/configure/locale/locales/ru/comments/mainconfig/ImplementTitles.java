package sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Afk.Titles;

@ConfigSerializable
public class ImplementTitles implements Titles {

	public ImplementTitles() {}

	@Setting("BeforeKick")
	private String beforeKick = "Если true, то игроку будет показано, сколько времени осталось до отключения от сервера.";
	@Setting("Unlimit")
	private String unlimit = "Если значение равно true, игроку постоянно будет приходить сообщение о том, что он AFK.\nЭта опция применима только к игрокам, у которых нет ограничения по времени AFK.";

	@Override
	public String getBeforeKick() {
		return beforeKick;
	}

	@Override
	public String getUnlimit() {
		return unlimit;
	}

}
