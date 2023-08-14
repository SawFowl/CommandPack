package sawfowl.commandpack.configure.configs.economy.dbsettings;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class DBSettings {

	public DBSettings(){}

	@Setting("Tables")
	private Tables tables = new Tables();
	@Setting("Collumns")
	private Collumns collumns = new Collumns();

	public Tables getTables() {
		return tables;
	}

	public Collumns getCollumns() {
		return collumns;
	}

}
