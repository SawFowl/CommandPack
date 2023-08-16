package sawfowl.commandpack.configure.configs.punishment;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.configs.punishment.queries.Columns;
import sawfowl.commandpack.configure.configs.punishment.queries.Patterns;
import sawfowl.commandpack.configure.configs.punishment.queries.SyncIntervals;
import sawfowl.commandpack.configure.configs.punishment.queries.Tables;

@ConfigSerializable
public class Queries {

	public Queries(){}

	private String[] tablesArray = {"bans", "bans_ip", "mutes", "warns"};
	private String[] indexesArray = {"uuid", "ip"};
	@Setting("Columns")
	@Comment("Names of columns for receiving data.")
	private Columns columns = new Columns();
	private Map<String, String> tables = IntStream.range(0, tablesArray.length).boxed().collect(Collectors.toMap(i -> tablesArray[i], i -> tablesArray[i]));
	@Setting("Indexes")
	@Comment("Indexed column names for automatic data synchronization.")
	private Map<String, String> indexes = IntStream.range(0, indexesArray.length).boxed().collect(Collectors.toMap(i -> indexesArray[i], i -> indexesArray[i]));
	@Setting("Patterns")
	@Comment("The order of columns for writing data to the database.\nRearrange the placeholders as you need. Their order must match the order of the columns in the database. Separation of placeholders - '><'.\nColumn names are specified in the request to add data to the database.")
	private Patterns patterns = new Patterns();
	@Setting("Tables")
	@Comment("Table names for automatic data synchronization.")
	private Tables tables2 = new Tables();
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

	public Patterns getPatterns() {
		return patterns;
	}

	public Map<String, String> getTables() {
		return tables;
	}

	public Map<String, String> getIndexes() {
		return indexes;
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

	public Tables getTables2() {
		return tables2;
	}

}
