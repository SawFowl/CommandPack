package sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Economy;

@ConfigSerializable
public class ImplementEconomy implements Economy {

	public ImplementEconomy() {}

	@Setting("Currency")
	private ImplementCurrency currency = new ImplementCurrency();
	@Setting("Tables")
	private ImplementTables tables = new ImplementTables();
	@Setting("StorageType")
	private String storageType = "Допустимые значения: File, H2, MySql.";
	@Setting("UpdateInterval")
	private String updateInterval = "Время между опросами базы данных MySql для обновления данных в кэше.\nВремя указывается в миллисекундах.";
	@Setting("DBSettings")
	private String dBSettings = "Эти настройки используются только при выборе MySql для хранения данных экономики.\nМожет быть использовано для настройки совместимости с базами данных плагина экономики Bukkit.\nИспользуйте совместимость на свой страх и риск. Стабильная работа не гарантируется.";
	@Setting("AdditionalChecks")
	private String additionalChecks = "Дополнительные проверки при выполнении транзакций с уменьшением баланса игрока при использовании MySql.";

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
