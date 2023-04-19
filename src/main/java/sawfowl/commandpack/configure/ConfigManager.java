package sawfowl.commandpack.configure;

import java.io.File;
import java.nio.file.Path;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.reference.ConfigurationReference;
import org.spongepowered.configurate.reference.ValueReference;

import sawfowl.commandpack.CommandPackPlugin;
import sawfowl.commandpack.api.data.player.Warp;
import sawfowl.commandpack.configure.configs.MainConfig;
import sawfowl.commandpack.configure.configs.commands.CommandsConfig;
import sawfowl.commandpack.configure.configs.player.PlayerData;
import sawfowl.commandpack.configure.configs.player.WarpData;

public class ConfigManager {

	private final CommandPackPlugin plugin;
	private final ConfigurationOptions options;
	private ConfigurationReference<CommentedConfigurationNode> mainConfigReference;
	private ValueReference<MainConfig, CommentedConfigurationNode> mainConfig;
	private ConfigurationReference<CommentedConfigurationNode> commandsConfigReference;
	private ValueReference<CommandsConfig, CommentedConfigurationNode> commandsConfig;
	private final Path playerDataPath;
	private ConfigurationLoader<CommentedConfigurationNode> warpsConfigLoader;
	private CommentedConfigurationNode warpsNode;
	public ConfigManager(CommandPackPlugin plugin, ConfigurationOptions options) {
		this.plugin = plugin;
		this.options = options;
		playerDataPath = plugin.getConfigDir().resolve("PlayerData");
		saveMainConfig();
		saveMainCommandsConfig();
		createWarpsConfig();
		loadPlayersData();
	}

	public MainConfig getMainConfig() {
		return mainConfig.get();
	}

	public ValueReference<CommandsConfig, CommentedConfigurationNode> getCommandsConfig() {
		return commandsConfig;
	}

	public void reloadConfigs() {
		try {
			mainConfigReference.load();
			mainConfig = mainConfigReference.referenceTo(MainConfig.class);
			commandsConfigReference.load();
			commandsConfig = commandsConfigReference.referenceTo(CommandsConfig.class);
			commandsConfig.get().updateCommandMap(commandsConfig);
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
			ConfigurationReference<CommentedConfigurationNode> configReference = HoconConfigurationLoader.builder().defaultOptions(options).path(playerDataPath.resolve(data.getUniqueId().toString() + ".conf")).build().loadToReference();
			ValueReference<PlayerData, CommentedConfigurationNode> config = configReference.referenceTo(PlayerData.class);
			config.setAndSave(data);
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	public void loadPlayersData() {
		if(playerDataPath.toFile().exists() && playerDataPath.toFile().listFiles().length > 0) for(File file : playerDataPath.toFile().listFiles()) if(file.getName().endsWith(".conf")) loadPlayerData(file);
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

	private void loadPlayerData(File playerConfig) {
		try {
			ConfigurationReference<CommentedConfigurationNode> configReference = HoconConfigurationLoader.builder().defaultOptions(options).path(playerConfig.toPath()).build().loadToReference();
			ValueReference<PlayerData, CommentedConfigurationNode> config = configReference.referenceTo(PlayerData.class);
			plugin.getPlayersData().addPlayerData(config.get());
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	private void createWarpsConfig() {
		warpsConfigLoader = HoconConfigurationLoader.builder().defaultOptions(options).path(plugin.getConfigDir().resolve("Warps.conf")).build();
		try {
			warpsNode = warpsConfigLoader.load();
			if(!warpsNode.childrenMap().isEmpty()) for(CommentedConfigurationNode node : warpsNode.childrenMap().values()) plugin.getPlayersData().addAdminWarp(node.get(WarpData.class));
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	private void saveMainConfig() {
		try {
			mainConfigReference = HoconConfigurationLoader.builder().defaultOptions(options).path(plugin.getConfigDir().resolve("Config.conf")).build().loadToReference();
			mainConfig = mainConfigReference.referenceTo(MainConfig.class);
			/*if(!plugin.getConfigDir().resolve("Config.conf").toFile().exists())*/ mainConfigReference.save();
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	private void saveMainCommandsConfig() {
		try {
			commandsConfigReference = HoconConfigurationLoader.builder().defaultOptions(options).path(plugin.getConfigDir().resolve("Commands.conf")).build().loadToReference();
			commandsConfig = commandsConfigReference.referenceTo(CommandsConfig.class);
			/*if(!plugin.getConfigDir().resolve("Commands.conf").toFile().exists())*/ commandsConfigReference.save();
			commandsConfig.get().updateCommandMap(commandsConfig);
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

}
