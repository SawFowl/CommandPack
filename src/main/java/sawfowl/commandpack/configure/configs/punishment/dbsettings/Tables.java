package sawfowl.commandpack.configure.configs.punishment.dbsettings;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Tables {

	public Tables(){}

	@Setting("Bans")
	private String bans = "cp_bans";
	@Setting("IpBans")
	private String ipbans = "cp_ipbans";
	@Setting("Mutes")
	private String mutes = "cp_mutes";
	@Setting("Warns")
	private String warns = "cp_warns";

	public String getBans() {
		return bans;
	}

	public String getIpbans() {
		return ipbans;
	}

	public String getMutes() {
		return mutes;
	}

	public String getWarns() {
		return warns;
	}

	public String[] values() {
		return new String[] {bans, ipbans, mutes, warns};
	}

}
