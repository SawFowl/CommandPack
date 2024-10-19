package sawfowl.commandpack.configure.locale.locales.def.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Punishment.WarnsBefore;

@ConfigSerializable
public class ImplementWarnsBefore implements WarnsBefore {

	public ImplementWarnsBefore() {}

	@Setting("Title")
	private String title = "Here you can specify the number of warnings the player will be automatically penalised on reaching.\nAs well as the duration of the punishment.";
	@Setting("PunishTime")
	private String punishTime = "Duration of punishments in minutes when the warning limit is reached.\nUsed only if all player warnings are permanent. Otherwise the duration will be equal to the shortest warning.\nSetting the value to 0 or below 0 will give the player a permanent punishment, provided that the player has not exceeded the warning limit and has temporary warnings.";

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getPunishTime() {
		return punishTime;
	}

}
