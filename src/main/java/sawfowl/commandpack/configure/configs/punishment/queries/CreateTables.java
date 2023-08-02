package sawfowl.commandpack.configure.configs.punishment.queries;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class CreateTables {

	public CreateTables(){}

	private String unixTimeFormatPlaceholder = "BIGINT";
	private String dateTimeFormatPlaceholder = "DATETIME";
	@Setting("ProfileBan")
	private String createProfileBansTableSql = "CREATE TABLE IF NOT EXISTS bans(uuid VARCHAR(128) UNIQUE, name TEXT, source TEXT, created %time%, expiration %time%, reason TEXT, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(UUID))";
	@Setting("IPBans")
	private String createIPBansTableSql = "CREATE TABLE IF NOT EXISTS bans_ip(ip VARCHAR(128) UNIQUE, source TEXT, created %time%, expiration %time%, reason TEXT, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(ip))";
	@Setting("CombinedBans")
	private String createCombinedBansSql = "CREATE TABLE IF NOT EXISTS bans(id BIGINT UNSIGNED AUTO_INCREMENT UNIQUE, uuid VARCHAR(128) UNIQUE, name TEXT, ip VARCHAR(128), source TEXT, created %time%, expiration %time%, reason TEXT, ipban TINYINT(1) DEFAULT 0, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(id))";
	@Setting("Mutes")
	private String createMutesTableSql = "CREATE TABLE IF NOT EXISTS mutes(uuid VARCHAR(128) UNIQUE, name TEXT, source TEXT, created %time%, expiration %time%, reason TEXT, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(uuid))";
	@Setting("Warns")
	private String createWarnsTableSql = "CREATE TABLE IF NOT EXISTS warns(uuid VARCHAR(128) UNIQUE, warns_data LONGTEXT, written DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(uuid))";
	@Setting("UnixTime")
	@Comment("Using unix time in database queries.\nIf you change the time format, you may need to delete tables.\nUse only to ensure compatibility with other plugins.")
	private boolean unixTime = false;

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

	public boolean isUnixTime() {
		return unixTime;
	}
	
}
