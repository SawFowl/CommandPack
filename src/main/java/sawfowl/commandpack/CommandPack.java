package sawfowl.commandpack;

import java.nio.file.Path;
import java.util.Optional;

import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RefreshGameEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import com.google.inject.Inject;

import sawfowl.localeapi.event.LocaleServiseEvent;
import sawfowl.commandpack.api.PlayersData;
import sawfowl.commandpack.api.iTempPlayerData;
import sawfowl.commandpack.apiclasses.TempPlayerData;
import sawfowl.commandpack.commands.parameterized.Spawn;
import sawfowl.commandpack.commands.parameterized.player.Hat;
import sawfowl.commandpack.commands.parameterized.player.Home;
import sawfowl.commandpack.commands.parameterized.player.SetHome;
import sawfowl.commandpack.commands.parameterized.player.SetSpawn;
import sawfowl.commandpack.commands.parameterized.player.Suicide;
import sawfowl.commandpack.configure.ConfigManager;
import sawfowl.commandpack.configure.configs.MainConfig;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.configs.commands.CommandsConfig;
import sawfowl.commandpack.configure.locale.Locales;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.commandpack.listeners.CommandLogListener;
import sawfowl.commandpack.listeners.PlayerCommandListener;
import sawfowl.commandpack.listeners.PlayerConnectionListener;
import sawfowl.commandpack.listeners.PlayerMoveListener;
import sawfowl.commandpack.listeners.PlayerRespawnListener;
import sawfowl.commandpack.utils.Economy;
import sawfowl.commandpack.utils.Logger;

@Plugin("commandpack")
public class CommandPack {
	private Logger logger;

	private CommandPack instance;
	private PluginContainer pluginContainer;
	private Path configDir;
	private Locales locales;
	private ConfigManager configManager;
	private Economy economy;
	private iTempPlayerData tempPlayerData;
	private PlayersData playersData;
	public CommandPack getInstance() {
		return instance;
	}

	public PluginContainer getPluginContainer() {
		return pluginContainer;
	}

	public Logger getLogger() {
		return logger;
	}

	public MainConfig getMainConfig() {
		return configManager.getMainConfig();
	}

	public CommandsConfig getCommandsConfig() {
		return configManager.getCommandsConfig().get();
	}

	public Locales getLocales() {
		return locales;
	}

	public Path getConfigDir() {
		return configDir;
	}

	public Economy getEconomy() {
		return economy;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public iTempPlayerData getTempPlayerData() {
		return tempPlayerData;
	}

	public sawfowl.commandpack.apiclasses.PlayersData getPlayersData() {
		return (sawfowl.commandpack.apiclasses.PlayersData) playersData;
	}

	@Inject
	public CommandPack(PluginContainer pluginContainer, @ConfigDir(sharedRoot = false) Path configDirectory) {
		instance = this;
		this.pluginContainer = pluginContainer;
		configDir = configDirectory;
		logger = new Logger();
	}

	@Listener
	public void onLocaleServisePostEvent(LocaleServiseEvent.Construct event) {
		configManager = new ConfigManager(instance, event.getLocaleService().getConfigurationOptions());
		locales = new Locales(event.getLocaleService(), getMainConfig().isJsonLocales());
		tempPlayerData = new TempPlayerData(instance);
		playersData = new sawfowl.commandpack.apiclasses.PlayersData(instance);
		configManager.loadPlayersData();
	}

	@Listener
	public void onServerStarted(StartedEngineEvent<Server> event) {
		if(!Sponge.server().serviceProvider().economyService().isPresent()) logger.warn(locales.getText(Sponge.server().locale(), LocalesPaths.ECONOMY_NOT_FOUND));
		economy = new Economy(instance);
		Sponge.eventManager().registerListeners(pluginContainer, new CommandLogListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerCommandListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerMoveListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerConnectionListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerRespawnListener(instance));
	}

	@Listener
	public void onReload(RefreshGameEvent event) {
		configManager.reloadConfigs();
		getPlayersData().reload();
	}

	@Listener
	public void registerParameterizedCommands(RegisterCommandEvent<Parameterized> event) {
		getCommandSettings("hat").ifPresent(settings -> {
			new Hat(instance, "hat").register(event);
		});
		getCommandSettings("home").ifPresent(settings -> {
			new Home(instance, "home").register(event);
		});
		getCommandSettings("sethome").ifPresent(settings -> {
			new SetHome(instance, "sethome").register(event);
		});
		getCommandSettings("setspawn").ifPresent(settings -> {
			new SetSpawn(instance, "setspawn").register(event);
		});
		getCommandSettings("spawn").ifPresent(settings -> {
			new Spawn(instance, "spawn").register(event);
		});
		getCommandSettings("suicide").ifPresent(settings -> {
			new Suicide(instance, "suicide").register(event);
		});
	}

	private Optional<CommandSettings> getCommandSettings(String command) {
		return getCommandsConfig().getOptCommandSettings(command);
	}

}
