package sawfowl.commandpack.configure.configs.punishment;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class PunishTime {

	public PunishTime(){}

	@Setting("Ban")
	private int banTime = 4320;
	@Setting("Mute")
	private int muteTime = 180;

	public int getBanTime() {
		return banTime * 60;
	}

	public int getMuteTime() {
		return muteTime * 60;
	}

}
