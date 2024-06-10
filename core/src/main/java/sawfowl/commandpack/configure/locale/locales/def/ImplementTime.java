package sawfowl.commandpack.configure.locale.locales.def;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Time;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementTime implements Time {

	public ImplementTime() {}

	@Setting("Second")
	private Component second = TextUtils.deserializeLegacy("s");
	@Setting("Minute")
	private Component minute = TextUtils.deserializeLegacy("m");
	@Setting("Hour")
	private Component hour = TextUtils.deserializeLegacy("h");
	@Setting("Day")
	private Component day = TextUtils.deserializeLegacy("d");
	@Setting("Format")
	private String format = "MM.dd.yyyy HH:mm:ss";
	@Setting("TimeZone")
	private String timeZone = "UTC";

	@Override
	public Component getSecond() {
		return second;
	}

	@Override
	public Component getMinute() {
		return minute;
	}

	@Override
	public Component getHour() {
		return hour;
	}

	@Override
	public Component getDay() {
		return day;
	}

	@Override
	public String getFormat() {
		return format;
	}

	@Override
	public String getTimeZone() {
		return timeZone;
	}

}
