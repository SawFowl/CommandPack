package sawfowl.commandpack.configure;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.reference.ConfigurationReference;
import org.spongepowered.configurate.reference.ValueReference;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.configure.configs.MainConfig;

public class ConfigManager {

	private final CommandPack plugin;
	private final ConfigurationOptions options;
	private ConfigurationReference<CommentedConfigurationNode> mainConfigReference;
	private ValueReference<MainConfig, CommentedConfigurationNode> mainConfig;

	public ConfigManager(CommandPack plugin, ConfigurationOptions options) {
		this.plugin = plugin;
		this.options = options;
		saveMainConfig();
	}

	public MainConfig getMainConfig() {
		return mainConfig.get();
	}

	public void reloadConfigs() {
		try {
			mainConfigReference.load();
			mainConfig = mainConfigReference.referenceTo(MainConfig.class);
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	private void saveMainConfig() {
		try {
			mainConfigReference = HoconConfigurationLoader.builder().defaultOptions(options).path(plugin.getConfigDir().resolve("MainConfig.conf")).build().loadToReference();
			mainConfig = mainConfigReference.referenceTo(MainConfig.class);
			if(!plugin.getConfigDir().resolve("MainConfig.conf").toFile().exists()) mainConfigReference.save();
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

}
