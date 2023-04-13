package sawfowl.commandpack;

import java.nio.file.Path;
import java.util.function.Supplier;

import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.Command.Raw;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RefreshGameEvent;
import org.spongepowered.api.event.lifecycle.RegisterBuilderEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import com.google.inject.Inject;

import sawfowl.localeapi.event.LocaleServiseEvent;
import sawfowl.commandpack.api.PlayersData;
import sawfowl.commandpack.api.RandomTeleportService;
import sawfowl.commandpack.api.TempPlayerData;
import sawfowl.commandpack.api.data.commands.CommandSettings;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.configure.ConfigManager;
import sawfowl.commandpack.configure.configs.MainConfig;
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

	private static CommandPack instance;
	private PluginContainer pluginContainer;
	private Path configDir;
	private Locales locales;
	private ConfigManager configManager;
	private Economy economy;
	private TempPlayerData tempPlayerData;
	private PlayersData playersData;
	private RandomTeleportService rtpService;

	public static CommandPack getInstance() {
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

	public RandomTeleportService getRTPService() {
		return rtpService;
	}

	@Inject
	public CommandPack(PluginContainer pluginContainer, @ConfigDir(sharedRoot = false) Path configDirectory) {
		instance = this;
		this.pluginContainer = pluginContainer;
		configDir = configDirectory;
		logger = new Logger();
		rtpService = new sawfowl.commandpack.apiclasses.RandomTeleportService(instance);
	}

	@Listener
	public void onLocaleServicePostEvent(LocaleServiseEvent.Construct event) {
		playersData = new sawfowl.commandpack.apiclasses.PlayersData(instance);
		configManager = new ConfigManager(instance, event.getLocaleService().getConfigurationOptions());
		locales = new Locales(event.getLocaleService(), getMainConfig().isJsonLocales());
		tempPlayerData = new sawfowl.commandpack.apiclasses.TempPlayerData(instance);
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
		Sponge.eventManager().post(new RandomTeleportService.PostEvent() {
			@Override
			public Cause cause() {
				return Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, pluginContainer).build(), pluginContainer);
			}
			@Override
			public RandomTeleportService getService() {
				return rtpService;
			}
		});
	}

	@Listener
	public void onReload(RefreshGameEvent event) {
		configManager.reloadConfigs();
		getPlayersData().reload();
	}

	@Listener
	public void registerParameterizedCommands(RegisterCommandEvent<Parameterized> event) {
		getCommandsConfig().registerParameterized(event, instance);
	}

	@Listener
	public void registerRawCommands(RegisterCommandEvent<Raw> event) {
		getCommandsConfig().registerRaw(event, instance);
	}

	@Listener
	public void registerBuilders(RegisterBuilderEvent event) {
		event.register(ParameterSettings.Builder.class, new Supplier<ParameterSettings.Builder>() {
			@Override
			public ParameterSettings.Builder get() {
				return new sawfowl.commandpack.commands.settings.ParameterSettings().builder();
			}
		});
		event.register(CommandSettings.Builder.class, new Supplier<CommandSettings.Builder>() {
			@Override
			public CommandSettings.Builder get() {
				return new sawfowl.commandpack.configure.configs.commands.CommandSettings().builder();
			}
		});
	}

}
