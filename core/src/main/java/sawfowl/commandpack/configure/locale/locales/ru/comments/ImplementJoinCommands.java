package sawfowl.commandpack.configure.locale.locales.ru.comments;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.JoinCommands;

@ConfigSerializable
public class ImplementJoinCommands implements JoinCommands {

	public ImplementJoinCommands() {}

	@Setting("FirstJoin")
	private String firstJoin = "Эти команды выполняются только при первом входе игрока на сервер.";
	@Setting("Regularly")
	private String regularly = "Эти команды выполняются каждый раз, когда игрок заходит на сервер, за исключением первого входа.";

	@Override
	public String getFirstJoin() {
		return firstJoin;
	}

	@Override
	public String getRegularly() {
		return regularly;
	}

}
