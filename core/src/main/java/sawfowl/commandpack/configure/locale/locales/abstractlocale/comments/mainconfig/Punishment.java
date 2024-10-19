package sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig;

public interface Punishment {

	interface DBSettings {

		String getTitle();

		String getColumns();

		String getTables();

		String getSyncIntervals();

		String getCreateCombinedBansTable();

		String getUnixTime();
		
	}

	interface WarnsBefore {

		String getTitle();

		String getPunishTime();

	}

	DBSettings getDBSettings();

	WarnsBefore getWarnsBefore();

	String getStorageType();

	String getAnnounce();

	String getDateTimeFormat();

	String getTimeZone();

}
