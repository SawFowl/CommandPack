package sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Punishment.WarnsBefore;

@ConfigSerializable
public class ImplementWarnsBefore implements WarnsBefore {

	public ImplementWarnsBefore() {}

	@Setting("Title")
	private String title = "Здесь вы можете указать количество предупреждений, за которые игрок будет автоматически наказываться при достижении лимита.\nА также продолжительность наказания.";
	@Setting("PunishTime")
	private String punishTime = "Продолжительность наказаний в минутах при достижении лимита предупреждений.\nИспользуется только в том случае, если все предупреждения игрока являются постоянными. В противном случае продолжительность будет равна самому короткому предупреждению.\nУстановка значение на 0 или ниже 0, приведет к тому, что игрок получит постоянное наказание, при условии, что он не превысил лимит предупреждений и имеет временные предупреждения.";

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getPunishTime() {
		return punishTime;
	}

}
