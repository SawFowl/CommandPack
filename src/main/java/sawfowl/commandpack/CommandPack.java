package sawfowl.commandpack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import com.google.inject.Inject;

import sawfowl.localeapi.event.LocaleServiseEvent;
import sawfowl.commandpack.api.PlayersData;
import sawfowl.commandpack.api.RandomTeleportService;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.commands.settings.CancelRulesSettings;
import sawfowl.commandpack.api.data.commands.settings.CommandSettings;
import sawfowl.commandpack.api.data.commands.settings.DelaySettings;
import sawfowl.commandpack.api.data.commands.settings.PriceSettings;
import sawfowl.commandpack.api.data.miscellaneous.Location;
import sawfowl.commandpack.api.data.miscellaneous.Point;
import sawfowl.commandpack.api.data.miscellaneous.Position;
import sawfowl.commandpack.api.data.player.Home;
import sawfowl.commandpack.api.data.player.PlayerBackpack;
import sawfowl.commandpack.api.data.player.Warp;
import sawfowl.commandpack.configure.ConfigManager;
import sawfowl.commandpack.configure.configs.MainConfig;
import sawfowl.commandpack.configure.configs.commands.CommandPrice;
import sawfowl.commandpack.configure.configs.commands.CommandsConfig;
import sawfowl.commandpack.configure.configs.commands.Delay;
import sawfowl.commandpack.configure.configs.commands.RandomTeleportWorldConfig;
import sawfowl.commandpack.configure.configs.miscellaneous.LocationData;
import sawfowl.commandpack.configure.configs.miscellaneous.PositionData;
import sawfowl.commandpack.configure.configs.miscellaneous.PointData;
import sawfowl.commandpack.configure.configs.player.BackpackData;
import sawfowl.commandpack.configure.configs.player.HomeData;
import sawfowl.commandpack.configure.configs.player.WarpData;
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
	private PlayersData playersData;
	private RandomTeleportService rtpService;
	private boolean isForge;
	private Map<Long, Double> tps1m = new HashMap<>();
	private Map<Long, Double> tps5m = new HashMap<>();
	private Map<Long, Double> tps10m = new HashMap<>();
	private long serverStartedTime;

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

	public sawfowl.commandpack.apiclasses.PlayersDataImpl getPlayersData() {
		return (sawfowl.commandpack.apiclasses.PlayersDataImpl) playersData;
	}

	public RandomTeleportService getRTPService() {
		return rtpService;
	}

	public boolean isForgeServer() {
		return isForge;
	}

	public double getAverageTPS1m() {
		return BigDecimal.valueOf(tps1m.values().stream().mapToDouble(d -> d).average().orElse(Sponge.server().ticksPerSecond())).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	public double getAverageTPS5m() {
		return BigDecimal.valueOf(tps5m.values().stream().mapToDouble(d -> d).average().orElse(Sponge.server().ticksPerSecond())).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	public double getAverageTPS10m() {
		return BigDecimal.valueOf(tps10m.values().stream().mapToDouble(d -> d).average().orElse(Sponge.server().ticksPerSecond())).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	public long getServerUptime() {
		return (System.currentTimeMillis() - serverStartedTime) / 1000;
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
		rtpService = new sawfowl.commandpack.apiclasses.RTPService(instance);
		playersData = new sawfowl.commandpack.apiclasses.PlayersDataImpl(instance);
		configManager = new ConfigManager(instance, event.getLocaleService().getConfigurationOptions());
		locales = new Locales(event.getLocaleService(), getMainConfig().isJsonLocales());
		configManager.loadPlayersData();
		isForge = checkForge();
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
		Sponge.eventManager().post(new sawfowl.commandpack.api.CommandPack.PostAPI() {

			@Override
			public Cause cause() {
				return Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, pluginContainer).build(), pluginContainer);
			}

			@Override
			public sawfowl.commandpack.api.CommandPack getAPI() {
				return new sawfowl.commandpack.api.CommandPack() {

					@Override
					public PlayersData playersData() {
						return playersData;
					}

					@Override
					public RandomTeleportService randomTeleportService() {
						return rtpService;
					}

					@Override
					public boolean isForgeServer() {
						return isForge;
					}

					@Override
					public double getAverageTPS1m() {
						return instance.getAverageTPS1m();
					}

					@Override
					public double getAverageTPS5m() {
						return instance.getAverageTPS5m();
					}

					@Override
					public double getAverageTPS10m() {
						return instance.getAverageTPS10m();
					}

				};

			}

		});
		Sponge.asyncScheduler().submit(Task.builder().delay(15, TimeUnit.SECONDS).interval(5, TimeUnit.SECONDS).plugin(pluginContainer).execute(() -> {
			long currentTime = System.currentTimeMillis() / 1000;
			tps1m.keySet().removeIf(time -> (currentTime - 60 > time));
			tps5m.keySet().removeIf(time -> (currentTime - 300 > time));
			tps10m.keySet().removeIf(time -> (currentTime - 600 > time));
			tps1m.put(currentTime, Sponge.server().ticksPerSecond());
			tps5m.put(currentTime, Sponge.server().ticksPerSecond());
			tps10m.put(currentTime, Sponge.server().ticksPerSecond());
		}).build());
		serverStartedTime = System.currentTimeMillis();
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
		event.register(RandomTeleportService.RandomTeleportOptions.Builder.class, new Supplier<RandomTeleportService.RandomTeleportOptions.Builder>() {
			@Override
			public RandomTeleportService.RandomTeleportOptions.Builder get() {
				return new RandomTeleportWorldConfig().builder();
			}
		});
		event.register(ParameterSettings.Builder.class, new Supplier<ParameterSettings.Builder>() {
			@Override
			public ParameterSettings.Builder get() {
				return new sawfowl.commandpack.commands.settings.ParameterSettings().builder();
			}
		});
		event.register(PriceSettings.Builder.class, new Supplier<PriceSettings.Builder>() {
			@Override
			public PriceSettings.Builder get() {
				return new CommandPrice().builder();
			}
		});
		event.register(CancelRulesSettings.Builder.class, new Supplier<CancelRulesSettings.Builder>() {
			@Override
			public CancelRulesSettings.Builder get() {
				return new sawfowl.commandpack.configure.configs.commands.CancelRules().builder();
			}
		});
		event.register(DelaySettings.Builder.class, new Supplier<DelaySettings.Builder>() {
			@Override
			public DelaySettings.Builder get() {
				return new Delay().builder();
			}
		});
		event.register(CommandSettings.Builder.class, new Supplier<CommandSettings.Builder>() {
			@Override
			public CommandSettings.Builder get() {
				return new sawfowl.commandpack.configure.configs.commands.CommandSettings().builder();
			}
		});
		event.register(Point.Builder.class, new Supplier<Point.Builder>() {
			@Override
			public Point.Builder get() {
				return new PointData().builder();
			}
		});
		event.register(Position.Builder.class, new Supplier<Position.Builder>() {
			@Override
			public Position.Builder get() {
				return new PositionData().builder();
			}
		});
		event.register(Location.Builder.class, new Supplier<Location.Builder>() {
			@Override
			public Location.Builder get() {
				return new LocationData().builder();
			}
		});
		event.register(Home.Builder.class, new Supplier<Home.Builder>() {
			@Override
			public Home.Builder get() {
				return new HomeData().builder();
			}
		});
		event.register(Warp.Builder.class, new Supplier<Warp.Builder>() {
			@Override
			public Warp.Builder get() {
				return new WarpData().builder();
			}
		});
		event.register(PlayerBackpack.Builder.class, new Supplier<PlayerBackpack.Builder>() {
			@Override
			public PlayerBackpack.Builder get() {
				return new BackpackData().builder();
			}
		});
	}

	@Listener
	public void onStop(StoppingEngineEvent<Server> event) {
		Sponge.asyncScheduler().tasks(pluginContainer).forEach(task -> {
			task.cancel();
		});
	}

	private boolean checkForge() {
		try {
			Class.forName("net.minecraftforge.fml.javafmlmod.FMLModContainer");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
