package sawfowl.commandpack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.time.Duration;
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
import sawfowl.commandpack.api.KitService;
import sawfowl.commandpack.api.PlayersData;
import sawfowl.commandpack.api.RandomTeleportService;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.command.CancelRules;
import sawfowl.commandpack.api.data.command.Delay;
import sawfowl.commandpack.api.data.command.Price;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.api.data.kits.KitPrice;
import sawfowl.commandpack.api.data.miscellaneous.Location;
import sawfowl.commandpack.api.data.miscellaneous.Point;
import sawfowl.commandpack.api.data.miscellaneous.Position;
import sawfowl.commandpack.api.data.player.Home;
import sawfowl.commandpack.api.data.player.Backpack;
import sawfowl.commandpack.api.data.player.Warp;
import sawfowl.commandpack.apiclasses.KitServiceImpl;
import sawfowl.commandpack.apiclasses.PlayersDataImpl;
import sawfowl.commandpack.apiclasses.RTPService;
import sawfowl.commandpack.configure.ConfigManager;
import sawfowl.commandpack.configure.configs.MainConfig;
import sawfowl.commandpack.configure.configs.commands.CommandsConfig;
import sawfowl.commandpack.configure.configs.commands.RandomTeleportWorldConfig;
import sawfowl.commandpack.configure.configs.kits.KitData;
import sawfowl.commandpack.configure.configs.kits.KitPriceData;
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
import sawfowl.commandpack.listeners.PlayerInteractBlockListener;
import sawfowl.commandpack.listeners.PlayerInteractEntityListener;
import sawfowl.commandpack.listeners.PlayerInteractItemListener;
import sawfowl.commandpack.listeners.PlayerInventoryListener;
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
	private KitService kitService;
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

	public KitService getKitService() {
		return kitService;
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
		rtpService = new RTPService(instance);
		kitService = new KitServiceImpl();
		playersData = new PlayersDataImpl(instance);
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
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerConnectionListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerInteractBlockListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerInteractEntityListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerInteractItemListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerInventoryListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerMoveListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerRespawnListener(instance));
		configManager.loadKits();
		sawfowl.commandpack.api.CommandPack api = new sawfowl.commandpack.api.CommandPack() {

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

			@Override
			public KitService kitService() {
				return kitService;
			}

		};

		Sponge.eventManager().post(new sawfowl.commandpack.api.CommandPack.PostAPI() {

			@Override
			public Cause cause() {
				return Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, pluginContainer).build(), pluginContainer);
			}

			@Override
			public sawfowl.commandpack.api.CommandPack getAPI() {
				return api;
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
			Sponge.server().onlinePlayers().forEach(player -> {
				if(getPlayersData().getTempData().isAfk(player) && !player.hasPermission(Permissions.AFK_UNLIMIT)) {
					if(getPlayersData().getTempData().getLastActivity(player) < Duration.ofMillis(System.currentTimeMillis()).getSeconds() - (getMainConfig().getAfkConfig().getTurnOnDlay() + getMainConfig().getAfkConfig().getKickDelay())) {
						player.kick(); // Добавить сообщение кика.
					}
				} else if(getPlayersData().getTempData().getLastActivity(player) < Duration.ofMillis(System.currentTimeMillis()).getSeconds() - getMainConfig().getAfkConfig().getTurnOnDlay()) {
					getPlayersData().getTempData().setAfkStatus(player);
					// Добавить сообщение чата о смене статуса.
				}
			});
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
		event.register(Price.Builder.class, new Supplier<Price.Builder>() {
			@Override
			public Price.Builder get() {
				return new sawfowl.commandpack.configure.configs.commands.CommandPrice().builder();
			}
		});
		event.register(CancelRules.Builder.class, new Supplier<CancelRules.Builder>() {
			@Override
			public CancelRules.Builder get() {
				return new sawfowl.commandpack.configure.configs.commands.CancelRules().builder();
			}
		});
		event.register(Delay.Builder.class, new Supplier<Delay.Builder>() {
			@Override
			public Delay.Builder get() {
				return new sawfowl.commandpack.configure.configs.commands.Delay().builder();
			}
		});
		event.register(Settings.Builder.class, new Supplier<Settings.Builder>() {
			@Override
			public Settings.Builder get() {
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
		event.register(Backpack.Builder.class, new Supplier<Backpack.Builder>() {
			@Override
			public Backpack.Builder get() {
				return new BackpackData().builder();
			}
		});
		event.register(KitPrice.Builder.class, new Supplier<KitPrice.Builder>() {
			@Override
			public KitPrice.Builder get() {
				return new KitPriceData().builder();
			}
		});
		event.register(Kit.Builder.class, new Supplier<Kit.Builder>() {
			@Override
			public Kit.Builder get() {
				return new KitData().builder();
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
