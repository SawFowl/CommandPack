package sawfowl.commandpack.configure.configs.punishment.queries;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Insert {

	public Insert(){}

	@Setting("ProfileBan")
	private String insertProfileBanSql = "REPLACE INTO bans (uuid, name, source, created, expiration, reason) VALUES(?, ?, ?, ?, ?, ?)";
	@Setting("IpBan")
	private String insertIPBanSql = "REPLACE INTO bans_ip (ip, source, created, expiration, reason) VALUES(?, ?, ?, ?, ?)";
	@Setting("CombitedBan")
	private String insertCombitedBanSql = "REPLACE INTO bans (uuid, ip, name, source, created, expiration, reason, ipban) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	@Setting("Mute")
	private String insertMuteSql = "REPLACE INTO mutes (uuid, name, source, created, expiration, reason) VALUES(?, ?, ?, ?, ?, ?)";
	@Setting("Warns")
	private String insertWarnsSql = "REPLACE INTO warns (uuid, warns_data) VALUES(?, ?)";

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

}
