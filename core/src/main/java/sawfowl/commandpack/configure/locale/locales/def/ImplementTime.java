package sawfowl.commandpack.configure.locale.locales.def;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.Time;

@ConfigSerializable
public class ImplementTime implements Time {

	public ImplementTime() {}

	@Setting("Second")
	private String second = "s";
	@Setting("Minute")
	private String minute = "m";
	@Setting("Hour")
	private String hour = "h";
	@Setting("Day")
	private String day = "d";

	@Override
	public String getSecond() {
		return second;
	}

	@Override
	public String getMinute() {
		return minute;
	}

	@Override
	public String getHour() {
		return hour;
	}

	@Override
	public String getDay() {
		return day;
	}

}
