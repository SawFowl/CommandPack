package sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.RestrictEntitySpawn;

@ConfigSerializable
public class ImplementRestrictEntitySpawn implements RestrictEntitySpawn {

	public ImplementRestrictEntitySpawn() {}

	@Setting("Title")
	private String title = "Используйте этот раздел конфигурации, чтобы контролировать, какие сущности могут появляться на сервере.\nНастройки для миров имеют более высокий приоритет, чем глобальные.\nСущность с id \"minecraft:player\" всегда игнорирует запрет на спавн.";
	@Setting("BlackList")
	private String blackList = "Если значение равно true, то сущности, указанные в этой настройке, не смогут появляться в мире.\nЕсли значение равно false, то будут появляться только те сущности, которые указаны в этой настройке.";
	@Setting("EntitiesList")
	private String entitiesList = "Список сущностей для управления их спавном.";
	@Setting("WorldsMap")
	private String worldsMap = "Индивидуальные настройки для каждого игрового мира.";

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
