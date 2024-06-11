package sawfowl.commandpack.configure.locale.locales.ru.debug;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Debug.Economy;

@ConfigSerializable
public class ImplementEconomy implements Economy {

	public ImplementEconomy() {}

	@Setting("ErrorGiveMoney")
	private String errorGiveMoney = "Не удалось добавить игровую валюту на аккаунт " + Placeholders.PLAYER + ".";
	@Setting("ErrorTakeMoney")
	private String errorTakeMoney = "Не удалось списать игровую валюту с аккаунта " + Placeholders.PLAYER + ".";
	@Setting("NotFound")
	private String notFound = "На сервере отсутствует плагин экономики. Некоторые функции будут недоступны. Вы можете включить функционал плагина экономики в основной конфигурации плагина CommandPack.";

	@Override
	public String getErrorGiveMoney(String player) {
		return errorGiveMoney.replace(Placeholders.PLAYER, player);
	}

	@Override
	public String getErrorTakeMoney(String player) {
		return errorTakeMoney.replace(Placeholders.PLAYER, player);
	}

	@Override
	public String getNotFound() {
		return notFound;
	}

}
