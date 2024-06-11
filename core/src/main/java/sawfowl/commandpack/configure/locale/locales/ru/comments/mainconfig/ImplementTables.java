package sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Economy.Tables;

@ConfigSerializable
public class ImplementTables implements Tables {

	public ImplementTables() {}

	@Setting("UniqueAccounts")
	private String uniqueAccounts = "Эта таблица используется для записи баланса игроков.";
	@Setting("Accounts")
	private String accounts = "Эта таблица предназначена для записи данных учетной записи без UUID.\nОна подходит для тех случаев, когда есть попытка изменить баланс игрока, который еще не вошел на сервер.\nПосле того как игрок войдет на сервер, данные будут сохранены заново с добавленным UUID.";

	@Override
	public String getUniqueAccounts() {
		return uniqueAccounts;
	}

	@Override
	public String getAccounts() {
		return accounts;
	}

}
