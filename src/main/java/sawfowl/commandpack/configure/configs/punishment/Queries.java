package sawfowl.commandpack.configure.configs.punishment;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Queries {

	public Queries(){}

	String[] tablesArray = {"bans", "bans_ip", "mutes", "warns"};
	String[] indexesArray = {"uuid", "ip"};
	@Setting("CreateProfileBanTable")
	private String createProfileBansTableSql = "CREATE TABLE IF NOT EXISTS bans(uuid VARCHAR(128) UNIQUE, name TEXT, source TEXT, created BIGINT, expiration BIGINT, reason TEXT, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(UUID))";
	@Setting("CreateIPBansTable")
	private String createIPBansTableSql = "CREATE TABLE IF NOT EXISTS bans_ip(ip VARCHAR(128) UNIQUE, source TEXT, created BIGINT, expiration BIGINT, reason TEXT, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(ip))";
	@Setting("CreateMutesTable")
	private String createMutesTableSql = "CREATE TABLE IF NOT EXISTS mutes(uuid VARCHAR(128) UNIQUE, name TEXT, source TEXT, created BIGINT, expiration BIGINT, reason TEXT, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(uuid))";
	@Setting("CreateWarnsTable")
	private String createWarnsTableSql = "CREATE TABLE IF NOT EXISTS warns(uuid VARCHAR(128) UNIQUE, warns_data LONGTEXT, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(uuid))";
	@Setting("SelectAllProfileBans")
	private String selectAllProfileBansSql = "SELECT * FROM bans";
	@Setting("SelectAllIPBans")
	private String selectAllIPBansSql = "SELECT * FROM bans_ip";
	@Setting("SelectAllMutes")
	private String selectAllMutesSql = "SELECT * FROM mutes";
	@Setting("SelectAllWarns")
	private String selectAllWarnsSql = "SELECT * FROM warns";
	@Setting("InsertProfileBan")
	private String insertProfileBanSql = "REPLACE INTO bans (uuid, name, source, created, expiration, reason) VALUES(?, ?, ?, ?, ?, ?)";
	@Setting("InsertIpBan")
	private String insertIPBanSql = "REPLACE INTO bans_ip (ip, source, created, expiration, reason) VALUES(?, ?, ?, ?, ?)";
	@Setting("InsertMute")
	private String insertMuteSql = "REPLACE INTO mutes (uuid, name, source, created, expiration, reason) VALUES(?, ?, ?, ?, ?, ?)";
	@Setting("InsertWarns")
	private String insertWarnsSql = "REPLACE INTO warns (uuid, warns_data) VALUES(?, ?)";
	@Setting("DeleteProfileBan")
	private String deleteProfileBanSql = "DELETE FROM bans WHERE uuid = ?";
	@Setting("DeleteIPBan")
	private String deleteIPBanSql = "DELETE FROM bans_ip WHERE ip = ?";
	@Setting("DeleteMute")
	private String deleteMuteSql = "DELETE FROM mutes WHERE uuid = ?";
	@Setting("DeleteWarns")
	private String deleteWarnsSql = "DELETE FROM warns WHERE uuid = ?";
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

	public String createProfileBansTableSql() {
		return createProfileBansTableSql;
	}

	public String createIPBansTableSql() {
		return createIPBansTableSql;
	}

	public String createMutesTableSql() {
		return createMutesTableSql;
	}

	public String createWarnsTableSql() {
		return createWarnsTableSql;
	}

	public String selectAllProfileBansSql() {
		return selectAllProfileBansSql;
	}

	public String selectAllIPBansSql() {
		return selectAllIPBansSql;
	}

	public String selectAllMutesSql() {
		return selectAllMutesSql;
	}

	public String selectAllWarnsSql() {
		return selectAllWarnsSql;
	}

	public String insertProfileBanSql() {
		return insertProfileBanSql;
	}

	public String insertIPBanSql() {
		return insertIPBanSql;
	}

	public String insertMuteSql() {
		return insertMuteSql;
	}

	public String insertWarnsSql() {
		return insertWarnsSql;
	}

	public String deleteProfileBanSql() {
		return deleteProfileBanSql;
	}

	public String deleteIPBanSql() {
		return deleteIPBanSql;
	}

	public String deleteMuteSql() {
		return deleteMuteSql;
	}

	public String deleteWarnsSql() {
		return deleteWarnsSql;
	}

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

}
