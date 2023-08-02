package sawfowl.commandpack.configure.configs.punishment.queries;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Delete {

	public Delete(){}

	@Setting("ProfileBan")
	private String deleteProfileBanSql = "DELETE FROM bans WHERE uuid = ?";
	@Setting("IPBan")
	@Comment("When using a combined bans table without modifying the table creation query, specify the `bans` table in that query.\nOtherwise, removing bans by IP will not work.")
	private String deleteIPBanSql = "DELETE FROM bans_ip WHERE ip = ?";
	@Setting("Mute")
	private String deleteMuteSql = "DELETE FROM mutes WHERE uuid = ?";
	@Setting("Warns")
	private String deleteWarnsSql = "DELETE FROM warns WHERE uuid = ?";

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

}
