package sawfowl.commandpack.configure.configs.punishment;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.configs.punishment.queries.Columns;
import sawfowl.commandpack.configure.configs.punishment.queries.CreateTables;
import sawfowl.commandpack.configure.configs.punishment.queries.Delete;
import sawfowl.commandpack.configure.configs.punishment.queries.Insert;
import sawfowl.commandpack.configure.configs.punishment.queries.Patterns;
import sawfowl.commandpack.configure.configs.punishment.queries.Select;
import sawfowl.commandpack.configure.configs.punishment.queries.SyncIntervals;

@ConfigSerializable
public class Queries {

	public Queries(){}

	private String[] tablesArray = {"bans", "bans_ip", "mutes", "warns"};
	private String[] indexesArray = {"uuid", "ip"};
	@Setting("CreateTables")
	private CreateTables createTables = new CreateTables();
	@Setting("Select")
	private Select select = new Select();
	@Setting("Insert")
	private Insert insert = new Insert();
	@Setting("Delete")
	private Delete delete = new Delete();
	@Setting("Columns")
	@Comment("Names of columns for receiving data.")
	private Columns columns = new Columns();
	@Setting("Tables")
	@Comment("Table names for automatic data synchronization.")
	private Map<String, String> tables = IntStream.range(0, tablesArray.length).boxed().collect(Collectors.toMap(i -> tablesArray[i], i -> tablesArray[i]));
	@Setting("Indexes")
	@Comment("Indexed column names for automatic data synchronization.")
	private Map<String, String> indexes = IntStream.range(0, indexesArray.length).boxed().collect(Collectors.toMap(i -> indexesArray[i], i -> indexesArray[i]));
	@Setting("Patterns")
	@Comment("The order of columns for writing data to the database.\nRearrange the placeholders as you need. Their order must match the order of the columns in the database. Separation of placeholders - '><'.\nColumn names are specified in the request to add data to the database.")
	private Patterns patterns = new Patterns();
	@Setting("SyncIntervals")
	@Comment("Intervals between plugin data updates.\nTime is indicated in seconds.")
	private SyncIntervals syncIntervals = new SyncIntervals();
	@Setting("CreateCombinedBansTable")
	@Comment("Creating a combined table with bans data.\nNot recommended.")
	private boolean createCombinedBansTable = false;

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

	public CreateTables getCreateTables() {
		return createTables;
	}

	public Select getSelect() {
		return select;
	}

	public Insert getInsert() {
		return insert;
	}

	public Delete getDelete() {
		return delete;
	}

}
