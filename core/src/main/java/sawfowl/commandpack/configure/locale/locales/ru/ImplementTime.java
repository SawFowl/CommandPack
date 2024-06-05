package sawfowl.commandpack.configure.locale.locales.ru;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Time;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementTime implements Time {

	public ImplementTime() {}

	@Setting("Second")
	private Component second = TextUtils.deserializeLegacy("с");
	@Setting("Minute")
	private Component minute = TextUtils.deserializeLegacy("м");
	@Setting("Hour")
	private Component hour = TextUtils.deserializeLegacy("ч");
	@Setting("Day")
	private Component day = TextUtils.deserializeLegacy("д");

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

}
