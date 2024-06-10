package sawfowl.commandpack.configure.configs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.configure.configs.commands.RandomTeleportConfig;
import sawfowl.commandpack.configure.configs.economy.EconomyConfig;
import sawfowl.commandpack.configure.configs.miscellaneous.AfkConfig;
import sawfowl.commandpack.configure.configs.miscellaneous.MySqlConfig;
import sawfowl.commandpack.configure.configs.miscellaneous.PreventDamage;
import sawfowl.commandpack.configure.configs.miscellaneous.RestrictEntitySpawn;
import sawfowl.commandpack.configure.configs.miscellaneous.RestrictMods;
import sawfowl.commandpack.configure.configs.miscellaneous.SpawnData;
import sawfowl.commandpack.configure.configs.punishment.Punishment;

@ConfigSerializable
public class MainConfig {

	public MainConfig() {}

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
	@Setting("Economy")
	private EconomyConfig economy = new EconomyConfig();
	@Setting("FixTopCommand")
	@Comment("Instead of teleporting to the very top of the world, an attempt will be made to find a suitable location under the bedrock.")
	private List<String> fixTop = Arrays.asList("minecraft:the_nether");
	@Setting("PreventDamage")
	@Comment("Prevent damage to other players if the player has invulnerability or invisibility.\nThese settings focus on the effects the player receives from the plugin commands.")
	private PreventDamage preventDamage = new PreventDamage();
	@Setting("ItemSerializer")
	@Comment("Selecting serialization variant for items.\n1 - All NBT tags will be written in 1 line. This option is the most reliable, but significantly complicates manual editing of NBT tags in config.\n2 - Advanced recording. Easier to make manual changes to the config. If you have problems with this type of serialization, you should report errors to the LocaleAPI plugin developer.\n3 - Using Sponge serializer. Some data will be written in 1 line. If you encounter problems with this type of serialization, you should report bugs to the Sponge developers.\nOptions 1 and 2 are fully interchangeable and can load data saved by option 3. Option 3 cannot load data stored by other options.")
	private int itemSerializer = 1;

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

	public EconomyConfig getEconomy() {
		return economy;
	}

	public boolean isFixTopCommand(ServerWorld world) {
		return fixTop.contains(world.key().asString());
	}

	public PreventDamage getPreventDamage() {
		return preventDamage;
	}

	public int getItemSerializer() {
		return itemSerializer;
	}

}
