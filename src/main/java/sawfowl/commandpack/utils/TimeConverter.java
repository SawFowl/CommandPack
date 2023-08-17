package sawfowl.commandpack.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import sawfowl.commandpack.CommandPack;

public class TimeConverter {

	private static final CommandPack plugin = CommandPack.getInstance();
	private static final TimeZone timeZone = plugin.getMainConfig().getPunishment().getTimeZone();
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(plugin.getMainConfig().getPunishment().getDateTimeFormat()).withZone(timeZone.toZoneId());
	private static final DateFormat dateFormat = setDateFormat();

	public static String toString(Instant time) {
		return dateFormat.format(Timestamp.from(time));
	}

	public static Instant fromString(String time) {
		return LocalDateTime.parse(time, formatter).atZone(timeZone.toZoneId()).toInstant();
	}

	private static DateFormat setDateFormat() {
		DateFormat dateFormat = plugin.getMainConfig().getPunishment().createDateTimeFormat();
		dateFormat.setTimeZone(timeZone);
		return dateFormat;
	}

}
