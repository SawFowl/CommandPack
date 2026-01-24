package sawfowl.commandpack.configure.locales.ru.comments.commandsconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locales.abstractlocale.comments.commandsconfig.DelayData.CancelRules;

@ConfigSerializable
public class ImplementCancelRules implements CancelRules {

	public ImplementCancelRules() {}

	@Setting("AllowMoving")
	private String allowMoving = "Отмена выполнения команды при перемещении игрока.";
	@Setting("AllowOtherCommand")
	private String allowOtherCommand = "Отмена выполнения команды, когда игрок использует другую команду.";

	@Override
	public String getAllowMoving() {
		return allowMoving;
	}

	@Override
	public String getAllowOtherCommand() {
		return allowOtherCommand;
	}

}
