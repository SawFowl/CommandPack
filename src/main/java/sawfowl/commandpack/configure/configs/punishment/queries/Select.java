package sawfowl.commandpack.configure.configs.punishment.queries;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Select {

	public Select(){}

	@Setting("ProfileBans")
	@Comment("This query will also apply when the `CreateCombinedBansTable` parameter is enabled.")
	private String selectAllProfileBansSql = "SELECT * FROM bans";
	@Setting("IPBans")
	private String selectAllIPBansSql = "SELECT * FROM bans_ip";
	@Setting("Mutes")
	private String selectAllMutesSql = "SELECT * FROM mutes";
	@Setting("Warns")
	private String selectAllWarnsSql = "SELECT * FROM warns";

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

}
