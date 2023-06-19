package sawfowl.commandpack.configure.configs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.configure.configs.commands.RandomTeleportConfig;
import sawfowl.commandpack.configure.configs.miscellaneous.AfkConfig;
import sawfowl.commandpack.configure.configs.miscellaneous.MySqlConfig;
import sawfowl.commandpack.configure.configs.miscellaneous.Punishment;
import sawfowl.commandpack.configure.configs.miscellaneous.RestrictEntitySpawn;
import sawfowl.commandpack.configure.configs.miscellaneous.RestrictMods;
import sawfowl.commandpack.configure.configs.miscellaneous.SpawnData;

@ConfigSerializable
public class MainConfig {

	public MainConfig() {}

	@Setting("AutoCompleteRawCommands")
	private boolean autoCompleteRawCommands = false;
	@Setting("JsonLocales")
	private boolean jsonLocales = false;
	@Setting("DebugEconomy")
	private boolean debugEconomy = true;
	@Setting("HideTeleportCommandSource")
	@Comment("If true, the player will not see who applied the teleportation command to him with administrative permission.")
	private boolean hideTeleportCommandSource = true;
	@Setting("BlackListHatItems")
	private List<String> blackListHatItems = new ArrayList<>(Arrays.asList("minecraft:diamond_chestplate"));
	@Setting("Spawn")
	private SpawnData spawnData;
	@Setting("RandomTeleport")
	private RandomTeleportConfig rtpConfig = new RandomTeleportConfig();
	@Setting("AfkSettings")
	private AfkConfig afkConfig = new AfkConfig();
	@Setting("EnableMotd")
	@Comment("The message to the player at login will be taken from the localization files.\nThe player must have the permission: \'" + Permissions.MOTD_ACCESS + "\'.")
	private boolean enableMotd = false;
	@Setting("ChangeConnectionMessages")
	@Comment("The messages are in the localization files.")
	private boolean changeConnectionMessages = true;
	@Setting("PrintPlayerMods")
	@Comment("If true, then a message will be sent to the console with a list of mods in the player when he connects to the server.\nThis option only works if the server uses Forge.")
	private boolean printPlayerMods = true;
	@Setting("RestrictMods")
	@Comment("Use this configuration section to control which with mods a player can join into the server.\nThis option only works if the server uses Forge.")
	private RestrictMods restrictMods = new RestrictMods();
	@Setting("RestrictEntitySpawn")
	@Comment("Use this configuration section to control which entities can spawn on the server.\nSettings for worlds have a higher priority than global settings.\nAn entity with the id \"minecraft:player\" will always be able to spawn regardless of these settings.")
	private RestrictEntitySpawn restrictEntitySpawn = new RestrictEntitySpawn();
	@Setting("MySQL")
	@Comment("Configure this if you need to store player punishment data in a MySQL database.")
	private MySqlConfig mySqlConfig = new MySqlConfig();
	@Setting("Punishment")
	private Punishment punishment = new Punishment();

	public boolean isAutoCompleteRawCommands() {
		return autoCompleteRawCommands;
	}

	public boolean isJsonLocales() {
		return jsonLocales;
	}

	public boolean isDebugEconomy() {
		return debugEconomy;
	}

	public boolean isHideTeleportCommandSource() {
		return hideTeleportCommandSource;
	}

	public boolean isBlackListHat(ItemStack itemStack) {
		return blackListHatItems.contains(itemFullID(itemStack)) || blackListHatItems.contains(itemID(itemStack));
	}

	private static String itemFullID(ItemStack item) {
		return Sponge.game().registry(RegistryTypes.ITEM_TYPE).valueKey(item.type()).asString();
	}

	private static String itemID(ItemStack item) {
		return Sponge.game().registry(RegistryTypes.ITEM_TYPE).valueKey(item.type()).value();
	}

	public Optional<SpawnData> getSpawnData() {
		return Optional.ofNullable(spawnData);
	}

	public void setSpawnData(SpawnData spawnData) {
		this.spawnData = spawnData;
	}

	public RandomTeleportConfig getRtpConfig() {
		return rtpConfig;
	}

	public AfkConfig getAfkConfig() {
		return afkConfig;
	}

	public boolean isEnableMotd() {
		return enableMotd;
	}

	public boolean isChangeConnectionMessages() {
		return changeConnectionMessages;
	}

	public boolean isPrintPlayerMods() {
		return printPlayerMods;
	}

	public RestrictMods getRestrictMods() {
		return restrictMods;
	}

	public RestrictEntitySpawn getRestrictEntitySpawn() {
		return restrictEntitySpawn;
	}

	public MySqlConfig getMySqlConfig() {
		return mySqlConfig;
	}

	public Punishment getPunishment() {
		return punishment;
	}

}
