package sawfowl.commandpack.configure.locale.locales.ru.comments;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.MainConfig;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Afk;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Economy;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Punishment;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.RandomTeleport;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.RestrictEntitySpawn;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.RestrictMods;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.SpawnData;
import sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig.ImplementAfk;
import sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig.ImplementEconomy;
import sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig.ImplementPunishment;
import sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig.ImplementRandomTeleport;
import sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig.ImplementRestrictEntitySpawn;
import sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig.ImplementRestrictMods;
import sawfowl.commandpack.configure.locale.locales.ru.comments.mainconfig.ImplementSpawnData;

@ConfigSerializable
public class ImplementMainConfig implements MainConfig {

	public ImplementMainConfig() {}

	@Setting("Afk")
	private ImplementAfk afk = new ImplementAfk();
	@Setting("Economy")
	private ImplementEconomy economy = new ImplementEconomy();
	@Setting("Punishment")
	private ImplementPunishment punishment = new ImplementPunishment();
	@Setting("RandomTeleport")
	private ImplementRandomTeleport randomTeleport = new ImplementRandomTeleport();
	@Setting("RestrictEntitySpawn")
	private ImplementRestrictEntitySpawn restrictEntitySpawn = new ImplementRestrictEntitySpawn();
	@Setting("RestrictMods")
	private ImplementRestrictMods restrictMods = new ImplementRestrictMods();
	@Setting("SpawnData")
	private ImplementSpawnData spawnData = new ImplementSpawnData();
	@Setting("HideTeleportCommandSource")
	private String hideTeleportCommandSource = "If true, the player will not see who applied the teleportation command to him with administrative permission.";
	@Setting("EnableMotd")
	private String enableMotd = "The message to the player at login will be taken from the localization files.\nThe player must have the permission: \'" + Permissions.MOTD_ACCESS + "\'.";
	@Setting("ChangeConnectionMessages")
	private String changeConnectionMessages = "The messages are in the localization files.";
	@Setting("PrintPlayerMods")
	private String printPlayerMods = "If true, then a message will be sent to the console with a list of mods in the player when he connects to the server.\nThis option only works if the server uses Forge.";
	@Setting("MySQL")
	private String mySQL = "Configure this if you need to store player punishment data in a MySQL database.";
	@Setting("FixTopCommand")
	private String fixTopCommand = "Instead of teleporting to the very top of the world, an attempt will be made to find a suitable location under the bedrock.";
	@Setting("PreventDamage")
	private String preventDamage = "Prevent damage to other players if the player has invulnerability or invisibility.\nThese settings focus on the effects the player receives from the plugin commands.";
	@Setting("ItemSerializer")
	private String itemSerializer = "Selecting serialization variant for items.\n1 - All NBT tags will be written in 1 line. This option is the most reliable, but significantly complicates manual editing of NBT tags in config.\n2 - Advanced recording. Easier to make manual changes to the config. If you have problems with this type of serialization, you should report errors to the LocaleAPI plugin developer.\n3 - Using Sponge serializer. Some data will be written in 1 line. If you encounter problems with this type of serialization, you should report bugs to the Sponge developers.\nOptions 1 and 2 are fully interchangeable and can load data saved by option 3. Option 3 cannot load data stored by other options.";

	@Override
	public Afk getAfk() {
		return afk;
	}

	@Override
	public Economy getEconomy() {
		return economy;
	}

	@Override
	public Punishment getPunishment() {
		return punishment;
	}

	@Override
	public RandomTeleport getRandomTeleport() {
		return randomTeleport;
	}

	@Override
	public RestrictEntitySpawn getRestrictEntitySpawn() {
		return restrictEntitySpawn;
	}

	@Override
	public RestrictMods getRestrictMods() {
		return restrictMods;
	}

	@Override
	public SpawnData getSpawnData() {
		return spawnData;
	}

	@Override
	public String getHideTeleportCommandSource() {
		return hideTeleportCommandSource;
	}

	@Override
	public String getEnableMotd() {
		return enableMotd;
	}

	@Override
	public String getChangeConnectionMessages() {
		return changeConnectionMessages;
	}

	@Override
	public String getPrintPlayerMods() {
		return printPlayerMods;
	}

	@Override
	public String getMySQL() {
		return mySQL;
	}

	@Override
	public String getFixTopCommand() {
		return fixTopCommand;
	}

	@Override
	public String getPreventDamage() {
		return preventDamage;
	}

	@Override
	public String getItemSerializer() {
		return itemSerializer;
	}

}
