package sawfowl.commandpack.configure;

import java.io.File;
import java.nio.file.Path;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.reference.ConfigurationReference;
import org.spongepowered.configurate.reference.ValueReference;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.api.data.player.Warp;
import sawfowl.commandpack.apiclasses.PlayersDataImpl;
import sawfowl.commandpack.configure.configs.MainConfig;
import sawfowl.commandpack.configure.configs.commands.CommandsConfig;
import sawfowl.commandpack.configure.configs.kits.KitData;
import sawfowl.commandpack.configure.configs.miscellaneous.JoinCommands;
import sawfowl.commandpack.configure.configs.player.PlayerData;
import sawfowl.commandpack.configure.configs.player.WarpData;

import sawfowl.localeapi.api.TextUtils;
import sawfowl.localeapi.api.serializetools.SerializeOptions;

public class ConfigManager {

	private final CommandPack plugin;
	private ConfigurationReference<CommentedConfigurationNode> mainConfigReference;
	private ValueReference<MainConfig, CommentedConfigurationNode> mainConfig;
	private ConfigurationReference<CommentedConfigurationNode> joinCommandsConfigReference;
	private ValueReference<JoinCommands, CommentedConfigurationNode> joinCommandsConfig;
	private ConfigurationReference<CommentedConfigurationNode> commandsConfigReference;
	private ValueReference<CommandsConfig, CommentedConfigurationNode> commandsConfig;
	private final Path playerDataPath;
	private ConfigurationLoader<CommentedConfigurationNode> warpsConfigLoader;
	private CommentedConfigurationNode warpsNode;
	private final Path kitsPath;
	public ConfigManager(CommandPack plugin) {
		this.plugin = plugin;
		playerDataPath = plugin.getConfigDir().resolve("PlayerData");
		kitsPath = plugin.getConfigDir().resolve("Kits");
		saveMainConfig();
		saveJoinCommandsConfig();
		saveMainCommandsConfig();
		createWarpsConfig();
	}

	public MainConfig getMainConfig() {
		return mainConfig.get();
	}

	public JoinCommands getJoinCommands() {
		return joinCommandsConfig.get();
	}

	public ValueReference<CommandsConfig, CommentedConfigurationNode> getCommandsConfig() {
		return commandsConfig;
	}

	public void reloadConfigs() {
		try {
			mainConfigReference.load();
			mainConfig = mainConfigReference.referenceTo(MainConfig.class);
			joinCommandsConfigReference.load();
			joinCommandsConfig = joinCommandsConfigReference.referenceTo(JoinCommands.class);
			commandsConfigReference.load();
			commandsConfig = commandsConfigReference.referenceTo(CommandsConfig.class);
			commandsConfig.get().updateCommandMap(commandsConfig);
			plugin.getKitService().getKits().forEach(kit -> {
				plugin.getKitService().removeKit(kit);
			});
			loadKits();
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	public void updateMainConfig() {
		mainConfig.setAndSave(getMainConfig());
	}

	public void savePlayerData(PlayerData data) {
		if(!playerDataPath.toFile().exists()) playerDataPath.toFile().mkdir();
		try {
			ConfigurationReference<CommentedConfigurationNode> configReference = SerializeOptions.createHoconConfigurationLoader(getMainConfig().getItemSerializer()).path(playerDataPath.resolve(data.getUniqueId().toString() + ".conf")).build().loadToReference();
			ValueReference<PlayerData, CommentedConfigurationNode> config = configReference.referenceTo(PlayerData.class);
			config.setAndSave(data);
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	public void loadPlayersData() {
		if(playerDataPath.toFile().exists() && playerDataPath.toFile().listFiles().length > 0) for(File file : playerDataPath.toFile().listFiles()) if(file.getName().endsWith(".conf")) loadPlayerData(file);
	}

	public void loadKits() {
		if(kitsPath.toFile().exists() && kitsPath.toFile().listFiles().length > 0) for(File file : kitsPath.toFile().listFiles()) if(file.getName().endsWith(".conf")) loadKit(file);
	}

	public void saveAdminWarp(Warp warp) {
		try {
			warpsNode.node(warp.getPlainName()).set(WarpData.class, (WarpData) warp);
			warpsConfigLoader.save(warpsNode);
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	public void deleteAdminWarp(String name) {
		if(!warpsNode.node(name).virtual()) warpsNode.removeChild(name);
		try {
			warpsConfigLoader.save(warpsNode);
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	public void deleteKit(String kit) {
		kitsPath.resolve(TextUtils.clearDecorations(kit) + ".conf").toFile().delete();
	}

	public void saveKit(Kit kit) {
		KitData data = (KitData) (kit instanceof KitData ? kit : Kit.builder().copyFrom(kit));
		if(!kitsPath.toFile().exists()) kitsPath.toFile().mkdir();
		try {
			ConfigurationReference<CommentedConfigurationNode> configReference = SerializeOptions.createHoconConfigurationLoader(getMainConfig().getItemSerializer()).path(kitsPath.resolve(TextUtils.clearDecorations(data.id()) + ".conf")).build().loadToReference();
			ValueReference<KitData, CommentedConfigurationNode> config = configReference.referenceTo(KitData.class);
			config.setAndSave(data);
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	private void loadPlayerData(File playerConfig) {
		try {
			ConfigurationReference<CommentedConfigurationNode> configReference = SerializeOptions.createHoconConfigurationLoader(getMainConfig().getItemSerializer()).path(playerConfig.toPath()).build().loadToReference();
			ValueReference<PlayerData, CommentedConfigurationNode> config = configReference.referenceTo(PlayerData.class);
			((PlayersDataImpl) plugin.getPlayersData()).addPlayerData(config.get());
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	private void loadKit(File kitConfig) {
		try {
			ConfigurationReference<CommentedConfigurationNode> configReference = SerializeOptions.createHoconConfigurationLoader(getMainConfig().getItemSerializer()).path(kitConfig.toPath()).build().loadToReference();
			ValueReference<KitData, CommentedConfigurationNode> config = configReference.referenceTo(KitData.class);
			plugin.getKitService().addKit(config.get());
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	private void createWarpsConfig() {
		warpsConfigLoader = SerializeOptions.createHoconConfigurationLoader(getMainConfig().getItemSerializer()).path(plugin.getConfigDir().resolve("Warps.conf")).build();
		try {
			warpsNode = warpsConfigLoader.load();
			if(!warpsNode.childrenMap().isEmpty()) for(CommentedConfigurationNode node : warpsNode.childrenMap().values()) plugin.getPlayersData().addAdminWarp(node.get(WarpData.class));
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	private void saveMainConfig() {
		try {
			mainConfigReference = SerializeOptions.createHoconConfigurationLoader(1).path(plugin.getConfigDir().resolve("Config.conf")).build().loadToReference();
			mainConfig = mainConfigReference.referenceTo(MainConfig.class);
			mainConfigReference.save();
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	private void saveJoinCommandsConfig() {
		try {
			joinCommandsConfigReference = SerializeOptions.createHoconConfigurationLoader(getMainConfig().getItemSerializer()).path(plugin.getConfigDir().resolve("JoinCommands.conf")).build().loadToReference();
			joinCommandsConfig = joinCommandsConfigReference.referenceTo(JoinCommands.class);
			joinCommandsConfigReference.save();
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	private void saveMainCommandsConfig() {
		try {
			commandsConfigReference = SerializeOptions.createHoconConfigurationLoader(getMainConfig().getItemSerializer()).path(plugin.getConfigDir().resolve("Commands.conf")).build().loadToReference();
			commandsConfig = commandsConfigReference.referenceTo(CommandsConfig.class);
			commandsConfigReference.save();
			commandsConfig.get().updateCommandMap(commandsConfig);
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

}
