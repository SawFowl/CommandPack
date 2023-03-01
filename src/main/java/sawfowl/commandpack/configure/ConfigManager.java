package sawfowl.commandpack.configure;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.reference.ConfigurationReference;
import org.spongepowered.configurate.reference.ValueReference;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.configure.configs.CommandsConfig;
import sawfowl.commandpack.configure.configs.MainConfig;

public class ConfigManager {

	private final CommandPack plugin;
	private final ConfigurationOptions options;
	private ConfigurationReference<CommentedConfigurationNode> mainConfigReference;
	private ValueReference<MainConfig, CommentedConfigurationNode> mainConfig;
	private ConfigurationReference<CommentedConfigurationNode> commandsConfigReference;
	private ValueReference<CommandsConfig, CommentedConfigurationNode> commandsConfig;

	public ConfigManager(CommandPack plugin, ConfigurationOptions options) {
		this.plugin = plugin;
		this.options = options;
		saveMainConfig();
		saveMainCommandsConfig();
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
			if(!plugin.getConfigDir().resolve("PlayerData").toFile().exists()) plugin.getConfigDir().resolve("PlayerData").toFile().mkdir();
			commandsConfigReference.load();
			commandsConfig = commandsConfigReference.referenceTo(CommandsConfig.class);
			commandsConfig.get().updateCommandMap(commandsConfig);
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	private void saveMainConfig() {
		try {
			mainConfigReference = HoconConfigurationLoader.builder().defaultOptions(options).path(plugin.getConfigDir().resolve("Config.conf")).build().loadToReference();
			mainConfig = mainConfigReference.referenceTo(MainConfig.class);
			if(!plugin.getConfigDir().resolve("Config.conf").toFile().exists()) mainConfigReference.save();
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	private void saveMainCommandsConfig() {
		if(!plugin.getConfigDir().resolve("PlayerData").toFile().exists()) plugin.getConfigDir().resolve("PlayerData").toFile().mkdir();
		try {
			commandsConfigReference = HoconConfigurationLoader.builder().defaultOptions(options).path(plugin.getConfigDir().resolve("Commands.conf")).build().loadToReference();
			commandsConfig = commandsConfigReference.referenceTo(CommandsConfig.class);
			if(!plugin.getConfigDir().resolve("Commands.conf").toFile().exists()) commandsConfigReference.save();
			commandsConfig.get().updateCommandMap(commandsConfig);
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

}
