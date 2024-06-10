package sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.RestrictEntitySpawn;

@ConfigSerializable
public class ImplementRestrictEntitySpawn implements RestrictEntitySpawn {

	public ImplementRestrictEntitySpawn() {}

	@Setting("BlackList")
	private String blackList = "If true, entities specified in this setting will not be able to spawn.\nIf false, only those entities listed in this setting will be spawned.";
	@Setting("EntitiesList")
	private String entitiesList = "A list of entities to control their spawning.";
	@Setting("WorldsMap")
	private String worldsMap = "Personalised settings for each game world.";

	@Override
	public String getBlackList() {
		return blackList;
	}

	@Override
	public String getEntitiesList() {
		return entitiesList;
	}

	@Override
	public String getWorldsMap() {
		return worldsMap;
	}

}
