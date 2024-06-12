package sawfowl.commandpack.configure.locale.locales.abstractlocale;

import net.kyori.adventure.text.Component;

public interface Time {

	Component getMilliseconds();

	Component getSecond();

	Component getMinute();

	Component getHour();

	Component getDay();

	String getFormat();

	String getTimeZone();

}
