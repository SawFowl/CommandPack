package sawfowl.commandpack.configure.configs.punishment;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.configs.punishment.queries.Columns;
import sawfowl.commandpack.configure.configs.punishment.queries.SyncIntervals;
import sawfowl.commandpack.configure.configs.punishment.queries.Tables;

@ConfigSerializable
public class Queries {

	public Queries(){}

	@Setting("Columns")
	@Comment("Names of columns for receiving data.")
	private Columns columns = new Columns();
	@Setting("Tables")
	@Comment("Table names for automatic data synchronization.")
	private Tables tables = new Tables();
	@Setting("SyncIntervals")
	@Comment("Intervals between plugin data updates.\nTime is indicated in seconds.")
	private SyncIntervals syncIntervals = new SyncIntervals();
	@Setting("CreateCombinedBansTable")
	@Comment("Creating a combined table with bans data.\nNot recommended.")
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
