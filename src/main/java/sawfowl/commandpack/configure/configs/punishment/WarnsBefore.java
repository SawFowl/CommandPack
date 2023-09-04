package sawfowl.commandpack.configure.configs.punishment;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

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
	@Comment("Duration of punishments in minutes when the warning limit is reached.\nUsed only if all player warnings are permanent. Otherwise the duration will be equal to the shortest warning.\nSetting the value to 0 or below 0 will give the player a permanent punishment, provided that the player has not exceeded the warning limit and has temporary warnings.")
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
