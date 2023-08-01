package sawfowl.commandpack.configure.configs.punishment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Queries {

	public Queries(){}

	private String unixTimeFormatPlaceholder = "BIGINT";
	private String dateTimeFormatPlaceholder = "DATETIME";
	private String[] tablesArray = {"bans", "bans_ip", "mutes", "warns"};
	private String[] indexesArray = {"uuid", "ip"};
	@Setting("CreateProfileBanTable")
	private String createProfileBansTableSql = "CREATE TABLE IF NOT EXISTS bans(uuid VARCHAR(128) UNIQUE, name TEXT, source TEXT, created %time%, expiration %time%, reason TEXT, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(UUID))";
	@Setting("CreateIPBansTable")
	private String createIPBansTableSql = "CREATE TABLE IF NOT EXISTS bans_ip(ip VARCHAR(128) UNIQUE, source TEXT, created %time%, expiration %time%, reason TEXT, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(ip))";
	@Setting("CombinedBansSql")
	private String createCombinedBansSql = "CREATE TABLE IF NOT EXISTS bans(id BIGINT UNSIGNED AUTO_INCREMENT UNIQUE, uuid VARCHAR(128) UNIQUE, ip VARCHAR(128), source TEXT, created %time%, expiration %time%, reason TEXT, ipban TINYINT(1) DEFAULT 0, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(id))";
	@Setting("CreateMutesTable")
	private String createMutesTableSql = "CREATE TABLE IF NOT EXISTS mutes(uuid VARCHAR(128) UNIQUE, name TEXT, source TEXT, created %time%, expiration %time%, reason TEXT, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(uuid))";
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
	@Setting("InsertCombitedBan")
	private String insertCombitedBanSql = "REPLACE INTO bans (uuid, ip, name, source, created, expiration, reason, ipban) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
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
	@Setting("UnixTime")
	@Comment("Using unix time in database queries.\nIf you change the time format, you may need to delete tables.\nUse only to ensure compatibility with other plugins.")
	private boolean unixTime = false;
	@Setting("CreateCombinedBansTable")
	@Comment("Creating a combined table with bans data.\nNot recommended.")
	private boolean createCombinedBansTable = false;
	@Setting("DateTimeFormat")
	@Comment("Don't change unnecessarily.")
	private String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
	@Setting("TimeZone")
	@Comment("Set your date time zone.\nAvailable options can be viewed at the link - https://gist.github.com/SawFowl/12dc8342e14bce41f95411f833d911f4")
	private String timeZone = "UTC";

	public String createProfileBansTableSql() {
		return createProfileBansTableSql.replace("%time%", unixTime ? unixTimeFormatPlaceholder : dateTimeFormatPlaceholder);
	}

	public String createIPBansTableSql() {
		return createIPBansTableSql.replace("%time%", unixTime ? unixTimeFormatPlaceholder : dateTimeFormatPlaceholder);
	}

	public String createMutesTableSql() {
		return createMutesTableSql.replace("%time%", unixTime ? unixTimeFormatPlaceholder : dateTimeFormatPlaceholder);
	}

	public String createCombinedBansSql() {
		return createCombinedBansSql.replace("%time%", unixTime ? unixTimeFormatPlaceholder : dateTimeFormatPlaceholder);
	}

	public String createWarnsTableSql() {
		return createWarnsTableSql.replace("%time%", unixTime ? unixTimeFormatPlaceholder : dateTimeFormatPlaceholder);
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

	public String getInsertCombitedBanSql() {
		return insertCombitedBanSql;
	}

	public String insertMuteSql() {
		return insertMuteSql;
	}

	public String insertWarnsSql() {
		return insertWarnsSql;
	}

	public String deleteProfileBanSql() {
		return deleteProfileBanSql.replace("%time%", unixTime ? unixTimeFormatPlaceholder : dateTimeFormatPlaceholder);
	}

	public String deleteIPBanSql() {
		return deleteIPBanSql.replace("%time%", unixTime ? unixTimeFormatPlaceholder : dateTimeFormatPlaceholder);
	}

	public String deleteMuteSql() {
		return deleteMuteSql.replace("%time%", unixTime ? unixTimeFormatPlaceholder : dateTimeFormatPlaceholder);
	}

	public String deleteWarnsSql() {
		return deleteWarnsSql.replace("%time%", unixTime ? unixTimeFormatPlaceholder : dateTimeFormatPlaceholder);
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

	public boolean isUnixTime() {
		return unixTime;
	}

	public boolean isCreateCombinedBansTable() {
		return createCombinedBansTable;
	}

	public DateFormat createDateTimeFormat() {
		return new SimpleDateFormat(dateTimeFormat);
	}

	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

	public TimeZone getTimeZone() {
		return TimeZone.getTimeZone(timeZone);
	}

}
