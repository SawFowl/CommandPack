package sawfowl.commandpack.configure.configs.punishment;

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
	@Setting("CreationDate")
	private String creationDate = "creation_date";
	@Setting("ExpirationDate")
	private String expirationDate = "expiration_date";
	@Setting("Reason")
	private String reason = "reason";

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

	public String getCreationDate() {
		return creationDate;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public String getReason() {
		return reason;
	}

}
