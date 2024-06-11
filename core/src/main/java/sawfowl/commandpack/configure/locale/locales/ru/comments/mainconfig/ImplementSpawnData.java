package sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.SpawnData;

@ConfigSerializable
public class ImplementSpawnData implements SpawnData {

	public ImplementSpawnData() {}

	@Setting("MoveAfterRespawn")
	private String moveAfterRespawn = "Телепорт при возрождении игрока.";
	@Setting("MoveAfterJoin")
	private String moveAfterJoin = "Телепортация игрока при подключении к серверу.";

	@Override
	public String getMoveAfterRespawn() {
		return moveAfterRespawn;
	}

	@Override
	public String getMoveAfterJoin() {
		return moveAfterJoin;
	}

}
