package sawfowl.commandpack.configure.locale.locales.abstractlocale.comments;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Afk;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Economy;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Punishment;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.RandomTeleport;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.RestrictEntitySpawn;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.RestrictMods;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.SpawnData;

public interface MainConfig {

	Afk getAfk();

	Economy getEconomy();

	Punishment getPunishment();

	RandomTeleport getRandomTeleport();

	RestrictEntitySpawn getRestrictEntitySpawn();

	RestrictMods getRestrictMods();

	SpawnData getSpawnData();

	String getHideTeleportCommandSource();

	String getEnableMotd();

	String getChangeConnectionMessages();

	String getPrintPlayerMods();

	String getMySQL();

	String getFixTopCommand();

	String getPreventDamage();

	String getItemSerializer();

}
