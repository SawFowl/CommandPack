package sawfowl.commandpack;

import java.nio.file.Path;
import java.util.Optional;

import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.Command.Raw;
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
import sawfowl.commandpack.api.TempPlayerData;
import sawfowl.commandpack.commands.parameterized.ClearInventory;
import sawfowl.commandpack.commands.parameterized.Repair;
import sawfowl.commandpack.commands.parameterized.Spawn;
import sawfowl.commandpack.commands.parameterized.player.Hat;
import sawfowl.commandpack.commands.parameterized.player.Suicide;
import sawfowl.commandpack.commands.parameterized.player.teleports.SetSpawn;
import sawfowl.commandpack.commands.parameterized.player.teleports.SetWarp;
import sawfowl.commandpack.commands.parameterized.player.teleports.Teleport;
import sawfowl.commandpack.commands.parameterized.player.teleports.TeleportHere;
import sawfowl.commandpack.commands.parameterized.player.teleports.TeleportHereAll;
import sawfowl.commandpack.commands.parameterized.player.teleports.Tpa;
import sawfowl.commandpack.commands.parameterized.player.teleports.TpaHere;
import sawfowl.commandpack.commands.parameterized.player.teleports.TpaHereAll;
import sawfowl.commandpack.commands.parameterized.player.teleports.home.Home;
import sawfowl.commandpack.commands.parameterized.player.teleports.home.SetHome;
import sawfowl.commandpack.commands.raw.Warp;
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
	private TempPlayerData tempPlayerData;
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

	public TempPlayerData getTempPlayerData() {
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
	public void onLocaleServicePostEvent(LocaleServiseEvent.Construct event) {
		configManager = new ConfigManager(instance, event.getLocaleService().getConfigurationOptions());
		locales = new Locales(event.getLocaleService(), getMainConfig().isJsonLocales());
		tempPlayerData = new sawfowl.commandpack.apiclasses.TempPlayerData(instance);
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
/*
	@Listener
	public void establishNewRegistries(final RegisterRegistryEvent.GameScoped event) {
		event.register(ResourceKey.of("commandpack", "warp"), true, new Supplier<Map<ResourceKey, Warp>>() {

			@Override
			public Map<ResourceKey, Warp> get() {
				return new HashMap<>();
			}
		});
	}
*/
	@Listener
	public void onReload(RefreshGameEvent event) {
		configManager.reloadConfigs();
		getPlayersData().reload();
	}

	@Listener
	public void registerParameterizedCommands(RegisterCommandEvent<Parameterized> event) {
		getCommandSettings("hat").ifPresent(settings -> {
			new Hat(instance, "hat", settings).register(event);
		});
		getCommandSettings("home").ifPresent(settings -> {
			new Home(instance, "home", settings).register(event);
		});
		getCommandSettings("sethome").ifPresent(settings -> {
			new SetHome(instance, "sethome", settings).register(event);
		});
		getCommandSettings("setspawn").ifPresent(settings -> {
			new SetSpawn(instance, "setspawn", settings).register(event);
		});
		getCommandSettings("spawn").ifPresent(settings -> {
			new Spawn(instance, "spawn", settings).register(event);
		});
		getCommandSettings("suicide").ifPresent(settings -> {
			new Suicide(instance, "suicide", settings).register(event);
		});
		getCommandSettings("setwarp").ifPresent(settings -> {
			new SetWarp(instance, "setwarp", settings).register(event);
		});
		getCommandSettings("tpa").ifPresent(settings -> {
			new Tpa(instance, "tpa", settings).register(event);
		});
		getCommandSettings("tpahere").ifPresent(settings -> {
			new TpaHere(instance, "tpahere", settings).register(event);
		});
		getCommandSettings("tpahereall").ifPresent(settings -> {
			new TpaHereAll(instance, "tpahereall", settings).register(event);
		});
		getCommandSettings("teleport").ifPresent(settings -> {
			new Teleport(instance, "teleport", settings).register(event);
		});
		getCommandSettings("teleporthere").ifPresent(settings -> {
			new TeleportHere(instance, "teleporthere", settings).register(event);
		});
		getCommandSettings("teleporthereall").ifPresent(settings -> {
			new TeleportHereAll(instance, "teleporthereall", settings).register(event);
		});
		getCommandSettings("clearinventory").ifPresent(settings -> {
			new ClearInventory(instance, "clearinventory", settings).register(event);
		});
		getCommandSettings("repair").ifPresent(settings -> {
			new Repair(instance, "repair", settings).register(event);
		});
	}

	@Listener
	public void registerRawCommands(RegisterCommandEvent<Raw> event) {
		getCommandSettings("warp").ifPresent(settings -> {
			new Warp(instance, "warp", settings).register(event);
		});
	}

	private Optional<CommandSettings> getCommandSettings(String command) {
		return getCommandsConfig().getOptCommandSettings(command);
	}

}
