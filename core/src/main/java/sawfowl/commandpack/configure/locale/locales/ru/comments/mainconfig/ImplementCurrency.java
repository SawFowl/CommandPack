package sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Economy.Currency;

@ConfigSerializable
public class ImplementCurrency implements Currency {

	public ImplementCurrency() {}

	@Setting("Key")
	private String key = "Ключ регистрации валюты.\nНе используйте декорирование текста или специальные символы!";
	@Setting("DBCollumn")
	private String dBCollumn = "Эта опция используется только при выборе MySql для хранения данных экономики.\nДля корректной записи данных каждая валюта должна иметь собственное имя колонки.\nМожно использовать для настройки совместимости с базами данных плагина экономики Bukkit.\nИспользуйте совместимость на свой страх и риск. Стабильная работа не гарантируется.\nНе используйте декорирование текста или специальные символы, кроме подчеркивания!";
	@Setting("ID")
	private String iD = "Эта опция используется только при выборе MySql для хранения экономических данных.\nКаждая валюта должна иметь свой собственный id.\nНе разрешается изменять его после создания таблиц.\nНе разрешается также вставлять новые валюты по id между существующими.";
	@Setting("Default")
	private String defaultCurrency = "Валюта, используемая по умолчанию.";
	@Setting("TransferPermission")
	private String transferPermission = "Если true, то игрок должен иметь разрешение на передачу валюты кому-либо.\nЭто правило применимо только к команде `/pay`.";

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getDBCollumn() {
		return dBCollumn;
	}

	@Override
	public String getID() {
		return iD;
	}

	@Override
	public String getDefault() {
		return defaultCurrency;
	}

	@Override
	public String getTransferPermission() {
		return transferPermission;
	}

}
