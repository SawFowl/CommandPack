package sawfowl.commandpack.configure.configs.punishment;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.configs.punishment.dbsettings.Columns;
import sawfowl.commandpack.configure.configs.punishment.dbsettings.SyncIntervals;
import sawfowl.commandpack.configure.configs.punishment.dbsettings.Tables;

@ConfigSerializable
public class DBSettings {

	public DBSettings(){}

	@Setting("Columns")
	@Comment("Names of columns for receiving data.\nIf you change the names, you must delete the tables in the existing database or manually rename the columns in the `ban` and `mute` tables.")
	private Columns columns = new Columns();
	@Setting("Tables")
	@Comment("Table names for automatic data synchronization.")
	private Tables tables = new Tables();
	@Setting("SyncIntervals")
	@Comment("Intervals between plugin data updates.\nTime is indicated in seconds.")
	private SyncIntervals syncIntervals = new SyncIntervals();
	@Setting("CreateCombinedBansTable")
	@Comment("Creating a combined table with bans data.\nNot recommended.\nUse if you need to work with an existing database of another plugin. Compatibility is not guaranteed.\nWhen using the combo table, removing the ban of a player who was banned by IP will remove the IP address ban as well. Similarly with the removal of the ban by IP.")
	private boolean createCombinedBansTable = false;
	@Setting("UnixTime")
	@Comment("Using unix time in database queries.\nIf you change the time format, you may need to delete tables.\nUse only to ensure compatibility with other plugins.")
	private boolean unixTime = false;

	public Columns getColumns() {
		return columns;
	}

	public Tables getTables() {
		return tables;
	}

	public SyncIntervals getSyncIntervals() {
		return syncIntervals;
	}

	public boolean isCreateCombinedBansTable() {
		return createCombinedBansTable;
	}

	public boolean isUnixTime() {
		return unixTime;
	}

}
