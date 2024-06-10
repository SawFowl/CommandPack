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

	DBSettings getDBSettings();

	String getStorageType();

	String getAnnounce();

	String getWarnsBefore();

	String getPunishTime();

	String getDateTimeFormat();

	String getTimeZone();

}
