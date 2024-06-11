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
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.configs.commands.RandomTeleportConfig;
import sawfowl.commandpack.configure.configs.economy.EconomyConfig;
import sawfowl.commandpack.configure.configs.miscellaneous.AfkConfig;
import sawfowl.commandpack.configure.configs.miscellaneous.MySqlConfig;
import sawfowl.commandpack.configure.configs.miscellaneous.PreventDamage;
import sawfowl.commandpack.configure.configs.miscellaneous.RestrictEntitySpawn;
import sawfowl.commandpack.configure.configs.miscellaneous.RestrictMods;
import sawfowl.commandpack.configure.configs.miscellaneous.SpawnData;
import sawfowl.commandpack.configure.configs.punishment.Punishment;
import sawfowl.localeapi.api.LocalisedComment;

@ConfigSerializable
public class MainConfig {

	public MainConfig() {}

	@Setting("DebugEconomy")
	private boolean debugEconomy = true;
	@Setting("HideTeleportCommandSource")
	@LocalisedComment(path = {"Comments", "MainConfig", "HideTeleportCommandSource"}, plugin = "commandpack")
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
	@LocalisedComment(path = {"Comments", "MainConfig", "EnableMotd"}, plugin = "commandpack")
	private boolean enableMotd = false;
	@Setting("ChangeConnectionMessages")
	@LocalisedComment(path = {"Comments", "MainConfig", "ChangeConnectionMessages"}, plugin = "commandpack")
	private boolean changeConnectionMessages = true;
	@Setting("PrintPlayerMods")
	@LocalisedComment(path = {"Comments", "MainConfig", "PrintPlayerMods"}, plugin = "commandpack")
	private boolean printPlayerMods = true;
	@Setting("RestrictMods")
	@LocalisedComment(path = {"Comments", "MainConfig", "RestrictMods", "Title"}, plugin = "commandpack")
	private RestrictMods restrictMods = new RestrictMods();
	@Setting("RestrictEntitySpawn")
	@LocalisedComment(path = {"Comments", "MainConfig", "RestrictEntitySpawn", "Title"}, plugin = "commandpack")
	private RestrictEntitySpawn restrictEntitySpawn = new RestrictEntitySpawn();
	@Setting("MySQL")
	@LocalisedComment(path = {"Comments", "MainConfig", "MySQL"}, plugin = "commandpack")
	private MySqlConfig mySqlConfig = new MySqlConfig();
	@Setting("Punishment")
	private Punishment punishment = new Punishment();
	@Setting("Economy")
	private EconomyConfig economy = new EconomyConfig();
	@Setting("FixTopCommand")
	@LocalisedComment(path = {"Comments", "MainConfig", "FixTopCommand"}, plugin = "commandpack")
	private List<String> fixTop = Arrays.asList("minecraft:the_nether");
	@Setting("PreventDamage")
	@LocalisedComment(path = {"Comments", "MainConfig", "PreventDamage"}, plugin = "commandpack")
	private PreventDamage preventDamage = new PreventDamage();
	@Setting("ItemSerializer")
	@LocalisedComment(path = {"Comments", "MainConfig", "ItemSerializer"}, plugin = "commandpack")
	private int itemSerializer = 2;

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
