package sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Economy;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.ImplementTables;

@ConfigSerializable
public class ImplementEconomy implements Economy {

	public ImplementEconomy() {}

	@Setting("Currency")
	private ImplementCurrency currency = new ImplementCurrency();
	@Setting("Tables")
	private ImplementTables tables = new ImplementTables();
	@Setting("StorageType")
	private String storageType = "Available values: File, H2, MySql.";
	@Setting("UpdateInterval")
	private String updateInterval = "The time between polls of the MySql database to update the data in the cache.\nThe time is specified in milliseconds.";
	@Setting("DBSettings")
	private String dBSettings = "This options is only used when selecting MySql to store the economy data.\nCan be used to customize compatibility with Bukkit economy plugin databases.\nUse compatibility at your own risk.\nStable work is not guaranteed.";
	@Setting("AdditionalChecks")
	private String additionalChecks = "Additional checks when performing transactions with player balance reduction when using MySql.";

	@Override
	public Currency getCurrency() {
		return currency;
	}

	@Override
	public Tables getTables() {
		return tables;
	}

	@Override
	public String getStorageType() {
		return storageType;
	}

	@Override
	public String getUpdateInterval() {
		return updateInterval;
	}

	@Override
	public String getDBSettings() {
		return dBSettings;
	}

	@Override
	public String getAdditionalChecks() {
		return additionalChecks;
	}

}
