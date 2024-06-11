package sawfowl.commandpack.configure.locale.locales.ru.comments;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.CommandsConfig;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.commandsconfig.DelayData;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.commandsconfig.Price;
import sawfowl.commandpack.configure.locale.locales.ru.comments.commandsconfig.ImplementDelayData;
import sawfowl.commandpack.configure.locale.locales.ru.comments.commandsconfig.ImplementPrice;

@ConfigSerializable
public class ImplementCommandsConfig implements CommandsConfig {

	public ImplementCommandsConfig() {}

	@Setting("DelayData")
	private ImplementDelayData delayData = new ImplementDelayData();
	@Setting("Price")
	private ImplementPrice price = new ImplementPrice();
	@Setting("AutoCompleteRaw")
	private String autoCompleteRaw = "Наличие этой секции означает, что данная команда относится к сырым командам.\nЕсли true, то для данной команды будет доступно автодополнение аргументов, что может упростить ее использование игроку.\nЕсли false, то автодополнение будет недоступно.";

	@Override
	public DelayData getDelayData() {
		return delayData;
	}

	@Override
	public Price getPrice() {
		return price;
	}

	@Override
	public String getAutoCompleteRaw() {
		return null;
	}

}
