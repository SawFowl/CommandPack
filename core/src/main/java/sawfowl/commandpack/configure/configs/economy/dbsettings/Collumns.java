package sawfowl.commandpack.configure.configs.economy.dbsettings;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Collumns {

	public Collumns(){}

	@Setting("UUID")
	private String uuid = "uuid";
	@Setting("Identifier")
	private String identifier = "name";

	public String getUuid() {
		return uuid;
	}

	public String getIdentifier() {
		return identifier;
	}

}
