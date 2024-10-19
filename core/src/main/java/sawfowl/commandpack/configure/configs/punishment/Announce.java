package sawfowl.commandpack.configure.configs.punishment;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Announce {

	public Announce(){}

	@Setting("Ban")
	private boolean ban = false;
	@Setting("Unban")
	private boolean unban = false;
	@Setting("Mute")
	private boolean mute = false;
	@Setting("Unmute")
	private boolean unmute = false;
	@Setting("Kick")
	private boolean kick = false;
	@Setting("Warn")
	private boolean warn = false;
	@Setting("Unwarn")
	private boolean unwarn = false;

	public boolean isBan() {
		return ban;
	}

	public boolean isUnban() {
		return unban;
	}

	public boolean isMute() {
		return mute;
	}

	public boolean isUnmute() {
		return unmute;
	}

	public boolean isKick() {
		return kick;
	}

	public boolean isWarn() {
		return warn;
	}

	public boolean isUnwarn() {
		return unwarn;
	}

}
