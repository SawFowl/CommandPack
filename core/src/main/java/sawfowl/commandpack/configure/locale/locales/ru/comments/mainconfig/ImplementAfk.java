package sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Afk;

@ConfigSerializable
public class ImplementAfk implements Afk {

	public ImplementAfk() {}
	@Setting("Titles")
	private ImplementTitles titles = new ImplementTitles();
	@Setting("TurnOnDelay")
	private String turnOnDelay = "Задержка перед включением статуса AFK.";
	@Setting("KickDelay")
	private String kickDelay = "Время до кика игрока с включенным статусом AFK, если у него нет разрешения на неограниченный AFK.";

	@Override
	public Titles getTitles() {
		return titles;
	}

	@Override
	public String getTurnOnDelay() {
		return turnOnDelay;
	}

	@Override
	public String getKickDelay() {
		return kickDelay;
	}

}
