package sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Punishment.DBSettings;

@ConfigSerializable
public class ImplementDBSettings implements DBSettings {

	public ImplementDBSettings() {}

	@Setting("Title")
	private String title = "Configuring queries to work with the MySQL database.\nChanging the settings can help in creating compatibility with other plugins.\nDo not remove the `written` column from queries. It is used for automatic data synchronization.";
	@Setting("Columns")
	private String columns = "Names of columns for receiving data.\nIf you change the names, you must delete the tables in the existing database or manually rename the columns in the `ban` and `mute` tables.";
	@Setting("Tables")
	private String tables = "Table names for automatic data synchronization.";
	@Setting("SyncIntervals")
	private String syncIntervals = "Intervals between plugin data updates.\nTime is indicated in seconds.";
	@Setting("CreateCombinedBansTable")
	private String createCombinedBansTable = "Creating a combined table with bans data.\nNot recommended.\nUse if you need to work with an existing database of another plugin. Compatibility is not guaranteed.\nWhen using the combo table, removing the ban of a player who was banned by IP will remove the IP address ban as well. Similarly with the removal of the ban by IP.";
	@Setting("UnixTime")
	private String unixTime = "Using unix time in database queries.\nIf you change the time format, you may need to delete tables.\nUse only to ensure compatibility with other plugins.";

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getColumns() {
		return columns;
	}

	@Override
	public String getTables() {
		return tables;
	}

	@Override
	public String getSyncIntervals() {
		return syncIntervals;
	}

	@Override
	public String getCreateCombinedBansTable() {
		return createCombinedBansTable;
	}

	@Override
	public String getUnixTime() {
		return unixTime;
	}

}
