package sawfowl.commandpack.configure.configs.punishment.queries;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Columns {

	public Columns(){}

	@Setting("UniqueId")
	private String uuid = "uuid";
	@Setting("IP")
	private String ip = "ip";
	@Setting("PlayerName")
	private String name = "name";
	@Setting("Source")
	private String source = "source";
	@Setting("Created")
	private String created = "created";
	@Setting("Expiration")
	private String expiration = "expiration";
	@Setting("Reason")
	private String reason = "reason";
	@Setting("IPBan")
	private String ipBan = "ipban";

	public String getUniqueId() {
		return uuid;
	}

	public String getIp() {
		return ip;
	}

	public String getName() {
		return name;
	}

	public String getSource() {
		return source;
	}

	public String getCreated() {
		return created;
	}

	public String getExpiration() {
		return expiration;
	}

	public String getReason() {
		return reason;
	}

	public String getIpBan() {
		return ipBan;
	}

}
