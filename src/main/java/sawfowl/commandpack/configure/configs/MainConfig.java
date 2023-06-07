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

}
