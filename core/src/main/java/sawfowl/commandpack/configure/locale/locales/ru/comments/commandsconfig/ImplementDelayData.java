package sawfowl.commandpack.configure.locale.locales.ru.comments.commandsconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.commandsconfig.DelayData;

@ConfigSerializable
public class ImplementDelayData implements DelayData {

	public ImplementDelayData() {}

	@Setting("CancelRules")
	private ImplementCancelRules cancelRules = new ImplementCancelRules();
	@Setting("Seconds")
	private String seconds = "Время в секундах до активации команды, если только у игрока нет разрешения игнорировать эту задержку.";

	@Override
	public CancelRules getCancelRules() {
		return cancelRules;
	}

	@Override
	public String getSeconds() {
		return seconds;
	}

}
