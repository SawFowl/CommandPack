package sawfowl.commandpack.configure.locale.locales.ru.comments.commandsconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.commandsconfig.Price;

@ConfigSerializable
public class ImplementPrice implements Price {

	public ImplementPrice() {}

	@Setting("Currency")
	private String currency = "Используемая валюта. Принимается как символ валюты, так и ее название.\nЕсли указанная валюта не будет присутствовать на сервере, будет использована валюта по умолчанию.";
	@Setting("Money")
	private String money = "Сумма, которую игрок заплатит за выполнение команды.";

	@Override
	public String getCurrency() {
		return currency;
	}

	@Override
	public String getMoney() {
		return money;
	}

}
