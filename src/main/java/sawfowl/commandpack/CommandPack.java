package sawfowl.commandpack;

import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
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
import sawfowl.commandpack.commands.parameterized.player.Suicide;
import sawfowl.commandpack.configure.ConfigManager;
import sawfowl.commandpack.configure.Locales;
import sawfowl.commandpack.configure.LocalesPaths;
import sawfowl.commandpack.configure.configs.CommandsConfig;
import sawfowl.commandpack.configure.configs.MainConfig;
import sawfowl.commandpack.listeners.CommandListener;
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
		return configManager.getCommandsConfig();
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

	@Inject
	public CommandPack(PluginContainer pluginContainer, @ConfigDir(sharedRoot = false) Path configDirectory) {
		instance = this;
		this.pluginContainer = pluginContainer;
		configDir = configDirectory;
		logger = new Logger(LogManager.getLogger("CommandPack"));
	}

	@Listener
	public void onLocaleServisePostEvent(LocaleServiseEvent.Construct event) {
		configManager = new ConfigManager(instance, event.getLocaleService().getConfigurationOptions());
		locales = new Locales(event.getLocaleService(), getMainConfig().isJsonLocales());
	}

	@Listener
	public void onServerStarted(StartedEngineEvent<Server> event) {
		if(Sponge.server().serviceProvider().economyService().isPresent()) {
			economy = new Economy(instance);
		} else {
			logger.warn(locales.getText(Sponge.server().locale(), LocalesPaths.ECONOMY_NOT_FOUND));
		}
		Sponge.eventManager().registerListeners(pluginContainer, new CommandListener(instance));
	}

	@Listener
	public void onReload(RefreshGameEvent event) {
		configManager.reloadConfigs();
	}

	@Listener
	public void registerParameterizedCommands(RegisterCommandEvent<Parameterized> event) {
		if(getCommandsConfig().getCommandConfig("suicide").isEnable()) new Suicide(instance, "suicide", null).register(event);
	}

}
