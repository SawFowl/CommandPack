package sawfowl.commandpack.configure;

import java.io.File;
import java.nio.file.Path;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.reference.ValueReference;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.api.data.player.Warp;
import sawfowl.commandpack.apiclasses.PlayersDataImpl;
import sawfowl.commandpack.configure.configs.MainConfig;
import sawfowl.commandpack.configure.configs.commands.CommandsConfig;
import sawfowl.commandpack.configure.configs.kits.KitData;
import sawfowl.commandpack.configure.configs.miscellaneous.JoinCommands;
import sawfowl.commandpack.configure.configs.player.PlayerData;
import sawfowl.commandpack.configure.configs.player.WarpData;
import sawfowl.localeapi.api.ConfigTypes;
import sawfowl.localeapi.api.TextUtils;
import sawfowl.localeapi.api.config.Config;
import sawfowl.localeapi.api.config.ReferencedConfig;
import sawfowl.localeapi.api.serializetools.ItemStackSerializerType;
import sawfowl.localeapi.api.services.ConfigurationService;

public class ConfigManager {

	private final CommandPackInstance plugin;
	private ReferencedConfig<MainConfig> mainConfig;
	private ReferencedConfig<JoinCommands> joinCommandsConfig;
	private ReferencedConfig<CommandsConfig> commandsConfig;
	private Config warpsConfig;
	private final Path playerDataPath;
	private final Path kitsPath;
	public ConfigManager(CommandPackInstance plugin) {
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
		return commandsConfig.getValueReference();
	}

	public void reloadConfigs() {
		mainConfig.load();
		joinCommandsConfig.load();
		commandsConfig.load();
		commandsConfig.get().updateCommandMap(commandsConfig.getValueReference());
		plugin.getKitService().getKits().forEach(kit -> {
			plugin.getKitService().removeKit(kit);
		});
		loadKits();
	}

	public void updateMainConfig() {
		mainConfig.save(getMainConfig());
	}

	public void savePlayerData(PlayerData data) {
		if(!playerDataPath.toFile().exists()) playerDataPath.toFile().mkdir();
		ConfigurationService.getInstance().createReferencedConfig(data).setItemStackSerializerType(getMainConfig().getItemSerializer()).setPath(playerDataPath).setName(data.getUniqueId().toString()).build();
	}

	public void loadPlayersData() {
		if(playerDataPath.toFile().exists() && playerDataPath.toFile().listFiles().length > 0) for(File file : playerDataPath.toFile().listFiles()) if(file.getName().endsWith(".conf")) loadPlayerData(file);
	}

	public void loadKits() {
		if(kitsPath.toFile().exists() && kitsPath.toFile().listFiles().length > 0) for(File file : kitsPath.toFile().listFiles()) if(file.getName().endsWith(".conf")) loadKit(file);
	}

	public void saveAdminWarp(Warp warp) {
		try {
			warpsConfig.getRootNode().node(warp.getPlainName()).set(WarpData.class, (WarpData) warp);
			warpsConfig.save();
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	public void deleteAdminWarp(String name) {
		if(!warpsConfig.getRootNode().node(name).virtual()) warpsConfig.getRootNode().removeChild(name);
		warpsConfig.save();
	}

	public void deleteKit(String kit) {
		kitsPath.resolve(TextUtils.clearDecorations(kit) + ".conf").toFile().delete();
	}

	public void saveKit(Kit kit) {
		KitData data = (KitData) (kit instanceof KitData ? kit : Kit.builder().copyFrom(kit));
		if(!kitsPath.toFile().exists()) kitsPath.toFile().mkdir();
		ConfigurationService.getInstance().createReferencedConfig(data).setItemStackSerializerType(getMainConfig().getItemSerializer()).setName(TextUtils.clearDecorations(data.id())).setType(ConfigTypes.HOCON).build();
	}

	private void loadPlayerData(File playerConfig) {
		var data = ConfigurationService.getInstance().createReferencedConfig(PlayerData.class).fromFile(playerConfig).setItemStackSerializerType(getMainConfig().getItemSerializer()).build().get();
		((PlayersDataImpl) plugin.getPlayersData()).addPlayerData(data);
		((PlayersDataImpl) plugin.getPlayersData()).addWarps(data);
	}

	private void loadKit(File kitConfig) {
		plugin.getKitService().addKit(ConfigurationService.getInstance().createReferencedConfig(KitData.class).setItemStackSerializerType(getMainConfig().getItemSerializer()).fromFile(kitConfig).build().get());
	}

	private void createWarpsConfig() {
		warpsConfig = ConfigurationService.getInstance().createSimpleConfig().setItemStackSerializerType(ItemStackSerializerType.SIMPLE).setPath(plugin.getConfigDir()).setName("Warps").setType(ConfigTypes.HOCON).build();
		try {
			if(!warpsConfig.getRootNode().childrenMap().isEmpty()) for(ConfigurationNode node : warpsConfig.getRootNode().childrenMap().values()) plugin.getPlayersData().addWarp(node.get(WarpData.class), null);
		} catch (ConfigurateException e) {
			plugin.getLogger().warn(e.getLocalizedMessage());
		}
	}

	private void saveMainConfig() {
		mainConfig = ConfigurationService.getInstance().createReferencedConfig(MainConfig.class).setItemStackSerializerType(ItemStackSerializerType.SIMPLE).setPath(plugin.getConfigDir()).setName("Config").setType(ConfigTypes.HOCON).build();
	}

	private void saveJoinCommandsConfig() {
		joinCommandsConfig = ConfigurationService.getInstance().createReferencedConfig(JoinCommands.class).setItemStackSerializerType(getMainConfig().getItemSerializer()).setPath(plugin.getConfigDir()).setName("JoinCommands").setType(ConfigTypes.HOCON).build();
	}

	private void saveMainCommandsConfig() {
		commandsConfig = ConfigurationService.getInstance().createReferencedConfig(CommandsConfig.class).setItemStackSerializerType(getMainConfig().getItemSerializer()).setPath(plugin.getConfigDir()).setName("Commands").setType(ConfigTypes.HOCON).addSerializers(sawfowl.commandpack.api.CommandPack.COMMAND_SETTINGS_SERIALIZERS).build();
		commandsConfig.get().updateCommandMap(getCommandsConfig());
	}

}
