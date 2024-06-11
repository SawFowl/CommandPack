package sawfowl.commandpack.configure.configs.punishment;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.localeapi.api.LocalisedComment;

@ConfigSerializable
public class WarnsBefore {

	public WarnsBefore(){}

	@Setting("Ban")
	private int ban = 10;
	@Setting("Kick")
	private int kick = 7;
	@Setting("Mute")
	private int mute = 3;
	@Setting("PunishTime")
	@LocalisedComment(path = {"Comments", "MainConfig", "Punishment", "WarnsBefore", "PunishTime"}, plugin = "commandpack")
	private PunishTime punishTime = new PunishTime();

	public int getBan() {
		return ban;
	}

	public int getKick() {
		return kick;
	}

	public int getMute() {
		return mute;
	}

	public PunishTime getPunishTime() {
		return punishTime;
	}

}
