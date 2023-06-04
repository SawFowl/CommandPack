package sawfowl.commandpack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
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
import org.spongepowered.api.world.biome.Biomes;
import org.spongepowered.api.world.generation.ChunkGenerator;
import org.spongepowered.api.world.generation.config.FlatGeneratorConfig;
import org.spongepowered.api.world.generation.config.flat.LayerConfig;
import org.spongepowered.api.world.generation.config.structure.StructureGenerationConfig;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import com.google.inject.Inject;

import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import sawfowl.localeapi.api.TextUtils;
import sawfowl.localeapi.event.LocaleServiseEvent;
import sawfowl.commandpack.api.KitService;
import sawfowl.commandpack.api.PlayersData;
import sawfowl.commandpack.api.RandomTeleportService;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
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
import sawfowl.commandpack.commands.settings.ParameterSettingsImpl;
import sawfowl.commandpack.commands.settings.RawArgumentImpl;
import sawfowl.commandpack.configure.ConfigManager;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.MainConfig;
import sawfowl.commandpack.configure.configs.commands.CancelRulesData;
import sawfowl.commandpack.configure.configs.commands.CommandPrice;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.configs.commands.CommandsConfig;
import sawfowl.commandpack.configure.configs.commands.DelayData;
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
import sawfowl.commandpack.listeners.PlayerChatListener;
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
	private sawfowl.commandpack.api.CommandPack api;
	private Map<String, ChunkGenerator> generators = new HashMap<>();

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

	public PlayersData getPlayersData() {
		return playersData;
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

	public sawfowl.commandpack.api.CommandPack getAPI() {
		return api;
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
		kitService = new KitServiceImpl(instance);
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
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerChatListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerCommandListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerConnectionListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerInteractBlockListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerInteractEntityListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerInteractItemListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerInventoryListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerMoveListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerRespawnListener(instance));
		configManager.loadKits();
		generators.put("empty", ChunkGenerator.flat(((AbstractBuilder<FlatGeneratorConfig>) FlatGeneratorConfig.builder().structureConfig(StructureGenerationConfig.none()).biome(Biomes.THE_VOID).addLayer(LayerConfig.of(0, BlockTypes.AIR.get().defaultState()))).build()));
		api = new sawfowl.commandpack.api.CommandPack() {

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

			@Override
			public void registerCustomGenerator(String name, ChunkGenerator chunkGenerator) {
				generators.put(name, chunkGenerator);
			}

			@Override
			public Optional<ChunkGenerator> getCustomGenerator(String name) {
				return Optional.ofNullable(generators.getOrDefault(name, null));
			}

			@Override
			public Set<String> getAvailableGenerators() {
				return new HashSet<>(generators.keySet());
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
				if(getPlayersData().getTempData().getLastActivity(player) > 0) {
					if(getPlayersData().getTempData().isAfk(player) && !player.hasPermission(Permissions.AFK_UNLIMIT)) {
						if((playersData.getTempData().getLastActivity(player) + getMainConfig().getAfkConfig().getTurnOnDlay() +  getMainConfig().getAfkConfig().getKickDelay()) - Duration.ofMillis(System.currentTimeMillis()).getSeconds() <= 0) {
							player.kick(getLocales().getText(player.locale(), LocalesPaths.COMMANDS_AFK_KICK));
							getPlayersData().getTempData().updateLastActivity(player);
						}
					} else if(getPlayersData().getTempData().getLastActivity(player) < Duration.ofMillis(System.currentTimeMillis()).getSeconds() - getMainConfig().getAfkConfig().getTurnOnDlay()) getPlayersData().getTempData().setAfkStatus(player);
				}
			});
		}).build());
		Sponge.asyncScheduler().submit(Task.builder().delay(15, TimeUnit.SECONDS).interval(1, TimeUnit.SECONDS).plugin(pluginContainer).execute(() -> {
			Sponge.server().onlinePlayers().forEach(player -> {
				if(getPlayersData().getTempData().isAfk(player)) {
					if(player.hasPermission(Permissions.AFK_UNLIMIT)) {
						if(getConfigManager().getMainConfig().getAfkConfig().getAfkTitlesConfig().isUnlimit()) player.sendTitlePart(TitlePart.TITLE, locales.getText(player.locale(), LocalesPaths.COMMANDS_AFK_TITLE));
					} else {
						if(getConfigManager().getMainConfig().getAfkConfig().getAfkTitlesConfig().isBeforeKick()) player.showTitle(Title.title(locales.getText(player.locale(), LocalesPaths.COMMANDS_AFK_TITLE), TextUtils.replace(locales.getText(player.locale(), LocalesPaths.COMMANDS_AFK_SUBTITLE), Placeholders.VALUE, timeFormat((playersData.getTempData().getLastActivity(player) + getMainConfig().getAfkConfig().getTurnOnDlay() +  getMainConfig().getAfkConfig().getKickDelay() + 1) - Duration.ofMillis(System.currentTimeMillis()).getSeconds(), player.locale()))));
					}
				}
			});
		}).build());
		serverStartedTime = System.currentTimeMillis();
	}

	@Listener
	public void onReload(RefreshGameEvent event) {
		configManager.reloadConfigs();
		((PlayersDataImpl) playersData).reload();
	}

	@Listener
	public void registerParameterizedCommands(RegisterCommandEvent<Parameterized> event) {
		getCommandsConfig().registerParameterized(event, instance);
	}

	@Listener
	public void registerRawCommands(RegisterCommandEvent<Raw> event) {
		getCommandsConfig().registerRaw(event, instance);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
				return new ParameterSettingsImpl().builder();
			}
		});
		event.register(Price.Builder.class, new Supplier<Price.Builder>() {
			@Override
			public Price.Builder get() {
				return new CommandPrice().builder();
			}
		});
		event.register(CancelRules.Builder.class, new Supplier<CancelRules.Builder>() {
			@Override
			public CancelRules.Builder get() {
				return new CancelRulesData().builder();
			}
		});
		event.register(Delay.Builder.class, new Supplier<Delay.Builder>() {
			@Override
			public Delay.Builder get() {
				return new DelayData().builder();
			}
		});
		event.register(Settings.Builder.class, new Supplier<Settings.Builder>() {
			@Override
			public Settings.Builder get() {
				return new CommandSettings().builder();
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
		event.register(RawArgument.Builder.class, new Supplier<RawArgument.Builder>() {
			@Override
			public RawArgument.Builder get() {
				return new RawArgumentImpl<Object>().builder();
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

	private Component timeFormat(long second, Locale locale) {
		long minute = TimeUnit.SECONDS.toMinutes(second);
		long hour = TimeUnit.SECONDS.toHours(second);
		long days = TimeUnit.SECONDS.toDays(second);
		if(days == 0) {
			if(hour == 0) {
				if(minute == 0) {
					return TextUtils.replace(Component.text(String.format((second > 9 ? "%02d" : "%01d"), second) + "%second%"), "%second%", CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.TIME_SECOND));
				} else return TextUtils.replaceToComponents(Component.text(String.format((minute > 9 ? "%02d" : "%01d"), minute) + "%minute%" + (second - (minute * 60) > 0 ? " " + String.format((second - (minute * 60) > 9 ? "%02d" : "%01d"), second - (minute * 60)) + "%second%" : "")), new String[] {"%minute%", "%second%"}, new Component[] {CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.TIME_MINUTE), CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.TIME_SECOND)});
			} else return TextUtils.replaceToComponents(Component.text(String.format((hour > 9 ? "%02d" : "%01d"), hour) + "%hour%" + (minute - (hour * 60) > 0 ? " " + String.format((minute - (hour * 60) > 9 ? "%02d" : "%01d"), minute - (hour * 60)) + "%minute%" : "")), new String[] {"%hour%", "%minute%"}, new Component[] {CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.TIME_HOUR), CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.TIME_MINUTE)});
		}
		return TextUtils.replaceToComponents(Component.text(String.format((days > 9 ? "%02d" : "%01d"), days) + "%days% " + String.format((hour - (days * 24) > 9 ? "%02d" : "%01d"), hour - (days * 24)) + "%hour%" + (minute - (hour * 60) > 0 ? " " + String.format((minute - (hour * 60) > 9 ? "%02d" : "%01d"), minute - (hour * 60)) + "%minute%" : "")), new String[] {"%days%", "%hour%", "%minute%"}, new Component[] {CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.TIME_DAYS), CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.TIME_HOUR), CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.TIME_MINUTE)});
	}

}
