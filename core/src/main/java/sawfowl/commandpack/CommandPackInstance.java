package sawfowl.commandpack;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Duration;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.Command.Raw;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.lifecycle.ProvideServiceEvent;
import org.spongepowered.api.event.lifecycle.RefreshGameEvent;
import org.spongepowered.api.event.lifecycle.RegisterBuilderEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.RegisterRegistryValueEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.world.biome.Biomes;
import org.spongepowered.api.world.generation.ChunkGenerator;
import org.spongepowered.api.world.generation.config.flat.FlatGeneratorConfig;
import org.spongepowered.api.world.generation.config.flat.LayerConfig;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.common.bridge.server.level.ServerLevelBridge;
import org.spongepowered.common.command.manager.SpongeCommandManager;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import com.google.inject.Inject;

import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;

import sawfowl.localeapi.api.Logger;
import sawfowl.localeapi.api.TextUtils;
import sawfowl.localeapi.api.event.LocaleServiseEvent;
import sawfowl.localeapi.api.placeholders.Placeholders;
import sawfowl.commandpack.api.CommandPack;
import sawfowl.commandpack.api.ContainersCollection;
import sawfowl.commandpack.api.KitService;
import sawfowl.commandpack.api.PlayersData;
import sawfowl.commandpack.api.RandomTeleportService;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.commands.parameterized.ParameterizedCommand;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
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
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warn;
import sawfowl.commandpack.api.data.punishment.Warns;
import sawfowl.commandpack.api.mixin.network.CustomPacket;
import sawfowl.commandpack.api.services.CPEconomyService;
import sawfowl.commandpack.api.services.PunishmentService;
import sawfowl.commandpack.api.tps.AverageTPS;
import sawfowl.commandpack.api.tps.TPS;
import sawfowl.commandpack.apiclasses.ContainersImpl;
import sawfowl.commandpack.apiclasses.CustomPacketImpl;
import sawfowl.commandpack.apiclasses.KitServiceImpl;
import sawfowl.commandpack.apiclasses.PlayersDataImpl;
import sawfowl.commandpack.apiclasses.RTPService;
import sawfowl.commandpack.apiclasses.TempPlayerDataImpl;
import sawfowl.commandpack.apiclasses.punishment.PunishmentServiceImpl;
import sawfowl.commandpack.commands.settings.ParameterSettingsImpl;
import sawfowl.commandpack.commands.settings.RawArgumentImpl;
import sawfowl.commandpack.commands.settings.RawArgumentsMapImpl;
import sawfowl.commandpack.configure.ConfigManager;
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
import sawfowl.commandpack.configure.configs.punishment.MuteData;
import sawfowl.commandpack.configure.configs.punishment.WarnData;
import sawfowl.commandpack.configure.configs.punishment.WarnsData;
import sawfowl.commandpack.configure.locale.Locales;
import sawfowl.commandpack.listeners.CommandLogListener;
import sawfowl.commandpack.listeners.EntityDamageListener;
import sawfowl.commandpack.listeners.PlayerChatListener;
import sawfowl.commandpack.listeners.PlayerCommandListener;
import sawfowl.commandpack.listeners.PlayerConnectionListener;
import sawfowl.commandpack.listeners.PlayerInteractBlockListener;
import sawfowl.commandpack.listeners.PlayerInteractEntityListener;
import sawfowl.commandpack.listeners.PlayerInteractItemListener;
import sawfowl.commandpack.listeners.PlayerInventoryListener;
import sawfowl.commandpack.listeners.PlayerMoveListener;
import sawfowl.commandpack.listeners.PlayerDeathAndRespawnListener;
import sawfowl.commandpack.utils.Economy;
import sawfowl.commandpack.utils.MariaDB;

@Plugin("commandpack")
public class CommandPackInstance {
	private Logger logger;

	private static CommandPackInstance instance;
	private PluginContainer pluginContainer;
	private Path configDir;
	private Locales locales;
	private ConfigManager configManager;
	private Economy economy;
	private PlayersData playersData;
	private RandomTeleportService rtpService;
	private KitService kitService;
	private boolean isForge;
	private boolean isNeo;
	private Map<Long, Double> tps1m = new HashMap<>();
	private Map<Long, Double> tps5m = new HashMap<>();
	private Map<Long, Double> tps10m = new HashMap<>();
	private long serverStartedTime;
	private CommandPack api;
	private PunishmentService punishmentService;
	private MariaDB mariaDB;
	private Map<String, ChunkGenerator> generators = new HashMap<>();
	private Set<RawCommand> registeredRawCommands = new HashSet<RawCommand>();
	private Set<ParameterizedCommand> registeredParameterizedCommands = new HashSet<ParameterizedCommand>();
	private boolean isStarted = false;
	private SpongeCommandManager manager;

	public static CommandPackInstance getInstance() {
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

	public boolean isNeoForgeServer() {
		return isNeo;
	}

	public boolean isModifiedServer() {
		return isForge || isNeo;
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

	public CommandPack getAPI() {
		return api;
	}

	public PunishmentService getPunishmentService() {
		return punishmentService;
	}

	public Optional<MariaDB> getMariaDB() {
		return Optional.ofNullable(mariaDB);
	}

	public boolean isStarted() {
		return isStarted;
	}

	@Inject
	public CommandPackInstance(PluginContainer pluginContainer, @ConfigDir(sharedRoot = false) Path configDirectory) {
		instance = this;
		this.pluginContainer = pluginContainer;
		configDir = configDirectory;
		logger = Logger.createApacheLogger("CommandPack");
	}

	@Listener
	public void onLocaleInit(LocaleServiseEvent.Construct event) {
		locales = new Locales(event.getLocaleService());
		rtpService = new RTPService(instance);
		kitService = new KitServiceImpl(instance);
		playersData = new PlayersDataImpl(instance);
		configManager = new ConfigManager(instance);
		configManager.loadPlayersData();
		isForge = checkForge();
		isNeo = checkNeo();
		economy = new Economy(instance);
		Sponge.eventManager().registerListeners(pluginContainer, economy);
		createAPI();
		if(getMainConfig().getMySqlConfig().isEnable()) {
			mariaDB = new MariaDB(instance);
			if(mariaDB.openConnection() == null) mariaDB = null;
		}
	}

	@Listener
	public void onServerStarted(StartedEngineEvent<Server> event) {
		isStarted = true;
		manager = (SpongeCommandManager) Sponge.server().commandManager();
		if(!Sponge.server().serviceProvider().economyService().isPresent()) logger.warn(locales.getSystemLocale().getDebug().getEconomy().getNotFound());
		registerListeners();
		configManager.loadKits();
		generators.put("empty", ChunkGenerator.flat(((AbstractBuilder<FlatGeneratorConfig>) FlatGeneratorConfig.builder().structureSets(null).biome(Biomes.THE_VOID).addLayer(LayerConfig.of(0, BlockTypes.AIR.get().defaultState()))).build()));
		generators.put("overworld", ChunkGenerator.overworld());
		generators.put("end", ChunkGenerator.theEnd());
		generators.put("nether", ChunkGenerator.theNether());
		Sponge.eventManager().post(new CommandPack.PostAPI() {

			@Override
			public Cause cause() {
				return Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, pluginContainer).build(), pluginContainer);
			}

			@Override
			public CommandPack getAPI() {
				return api;
			}

		});
		createTasks();
		serverStartedTime = System.currentTimeMillis();
		registeredRawCommands.forEach(this::registerRaw);
		registeredParameterizedCommands.forEach(this::registerParameterized);
		manager = null;
		registeredRawCommands.clear();
		registeredParameterizedCommands.clear();
		registeredRawCommands = null;
		registeredParameterizedCommands = null;
		Sponge.server().userManager().streamAll().forEach(profile -> {
			if(!profile.name().isPresent()) {
				Sponge.server().userManager().load(profile).thenAccept(optUser -> {
					optUser.ifPresent(user -> ((TempPlayerDataImpl) playersData.getTempData()).registerUser(user.name()));
				}).thenAccept(v -> {
					Sponge.server().userManager().removeFromCache(profile.uuid());
				});
			} else ((TempPlayerDataImpl) playersData.getTempData()).registerUser(profile.name().get());
		});
		registerPlaceholders();
	}

	@Listener
	public void onProvideBanService(ProvideServiceEvent<BanService> event) {
		if(getMainConfig().getPunishment().isEnable()) event.suggest(() -> punishmentService = new PunishmentServiceImpl(instance));
	}

	@Listener
	public void onProvideEconomyService(ProvideServiceEvent<EconomyService> event) {
		if(getMainConfig().getEconomy().isEnable()) economy.createEconomy(event);
	}

	@Listener
	private void onRegisterRegistry(final RegisterRegistryValueEvent.GameScoped event) {
		if(getMainConfig().getEconomy().isEnable()) economy.getEconomyServiceImpl().getCurrenciesMap().forEach((ch, currency) -> {
			getMainConfig().getEconomy().getCurrency(ch).ifPresent(config -> {
				event.registry(RegistryTypes.CURRENCY).register(ResourceKey.resolve(config.getKey()), currency);
			});
		});
	}

	@Listener
	public void onReload(RefreshGameEvent event) {
		configManager.reloadConfigs();
		((PlayersDataImpl) playersData).reload();
	}

	@Listener(order = Order.FIRST)
	public void registerParameterizedCommands(RegisterCommandEvent<Parameterized> event) {
		getCommandsConfig().registerParameterized(event, instance);
	}

	@Listener(order = Order.FIRST)
	public void registerRawCommands(RegisterCommandEvent<Raw> event) {
		getCommandsConfig().registerRaw(event, instance);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listener
	public void registerBuilders(RegisterBuilderEvent event) {
		event.register(RandomTeleportService.RandomTeleportOptions.Builder.class, () -> new RandomTeleportWorldConfig().builder());
		event.register(ParameterSettings.Builder.class, () -> new ParameterSettingsImpl().builder());
		event.register(Price.Builder.class, () -> new CommandPrice().builder());
		event.register(CancelRules.Builder.class, () -> new CancelRulesData().builder());
		event.register(Delay.Builder.class, () -> new DelayData().builder());
		event.register(Settings.Builder.class, () -> new CommandSettings().builder());
		event.register(Point.Builder.class, () -> new PointData().builder());
		event.register(Position.Builder.class, () -> new PositionData().builder());
		event.register(Location.Builder.class, () -> new LocationData().builder());
		event.register(Home.Builder.class, () -> new HomeData().builder());
		event.register(Warp.Builder.class, () -> new WarpData().builder());
		event.register(Backpack.Builder.class, () -> new BackpackData().builder());
		event.register(KitPrice.Builder.class, () -> new KitPriceData().builder());
		event.register(Kit.Builder.class, () -> new KitData().builder());
		event.register(RawArgument.Builder.class, () -> new RawArgumentImpl().builder());
		event.register(Mute.Builder.class, () -> new MuteData().builder());
		event.register(Warn.Builder.class, () -> new WarnData().builder());
		event.register(Warns.Builder.class, () -> new WarnsData().builder());
		event.register(CustomPacket.Builder.class, () -> new CustomPacketImpl().builder());
		event.register(RawArgumentsMap.Builder.class, () -> new RawArgumentsMapImpl().builder());
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

	private boolean checkNeo() {
		try {
			Class.forName("net.neoforged.neoforge.common.NeoForge");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private Component timeFormat(long second, Locale locale) {
		return TextUtils.timeFormat(second, locale, getLocales().getLocale(locale).getTime().getDay(), getLocales().getLocale(locale).getTime().getHour(), getLocales().getLocale(locale).getTime().getMinute(), getLocales().getLocale(locale).getTime().getSecond());
	}

	void registerRaw(RawCommand command) {
		if(command.getCommandSettings() != null && command.getCommandSettings().getAliases() != null && command.getCommandSettings().getAliases().length > 0) {
			manager.registrar(Raw.class).get().register(command.getContainer(), command, command.command(), command.getCommandSettings().getAliases());
		} else manager.registrar(Raw.class).get().register(command.getContainer(), command, command.command());
	}

	void registerParameterized(ParameterizedCommand command) {
		if(command.getCommandSettings() != null && command.getCommandSettings().getAliases() != null && command.getCommandSettings().getAliases().length > 0) {
			manager.registrar(Parameterized.class).get().register(command.getContainer(), command.build(), command.command(), command.getCommandSettings().getAliases());
		} else manager.registrar(Parameterized.class).get().register(command.getContainer(), command.build(), command.command());
	}

	private void registerPlaceholders() {
		Placeholders.register(ServerWorld.class, "WorldTPS", (text, world, def) -> (text.replace("%world-tps%", BigDecimal.valueOf(getAPI().getTPS().getWorldTPS(world)).setScale(2, RoundingMode.HALF_UP).doubleValue())));
		Placeholders.register(ServerWorld.class, "WorldTicks", (text, world, def) -> (text.replace("%world-ticks%", BigDecimal.valueOf(getAPI().getTPS().getWorldTickTime(world)).setScale(2, RoundingMode.HALF_UP).doubleValue())));
		if(getMainConfig().getPunishment().isEnable()) {
			Placeholders.register(ServerPlayer.class, "PlayerWarns", (text, player, def) -> (text.replace("%player-warns%", getPunishmentService().getWarns(player).map(warns -> warns.totalWarns() + "/" + warns.inAllTime()).orElse("0"))));
			Placeholders.register(ServerPlayer.class, "PlayerMuteExpire", (text, player, def) -> (text.replace("%player-mute-expire%", getPunishmentService().getMute(player).map(mute -> expire(player.locale(), mute)).orElse(Component.empty()))));
		}
	}

	private Component expire(Locale locale, sawfowl.commandpack.api.data.punishment.Mute mute) {
		if(!mute.getExpiration().isPresent()) return getLocales().getLocale(locale).getCommands().getMuteInfo().getPermanent();
		SimpleDateFormat format = new SimpleDateFormat(getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(mute.getExpiration().get().toEpochMilli());
		return TextUtils.deserialize(format.format(calendar.getTime()));
	}

	private void createAPI() {
		AverageTPS averageTPS = createAverageTPS();
		TPS tps = createTPS(averageTPS);
		api = createAPI(tps);
	}

	private AverageTPS createAverageTPS() {
		return new AverageTPS() {
			@Override
			public double get1m() {
				return getAverageTPS1m();
			}
			@Override
			public double get5m() {
				return getAverageTPS5m();
			}
			@Override
			public double get10m() {
				return getAverageTPS10m();
			}
		};
	}

	private TPS createTPS(AverageTPS averageTPS) {
		return new TPS() {
			@Override
			public double getWorldTickTime(ServerWorld world) {
				long[] tickTimes = ((ServerLevelBridge) world).bridge$recentTickTimes();
				long $$1 = 0L;
				for(long $$2 : tickTimes) $$1 += $$2;
				return ((double)$$1 / (double)tickTimes.length) * 1.0E-6D;
			}
			@Override
			public double getWorldTPS(ServerWorld world) {
				return Math.min(1000.0 / (getWorldTickTime(world)), 20.0);
			}
			@Override
			public AverageTPS getAverageTPS() {
				return averageTPS;
			}
		};
	}

	private CommandPack createAPI(TPS tps) {
		return new CommandPack() {
			ContainersCollection colletions = new ContainersImpl(getInstance());
			@Override
			public PlayersData getPlayersData() {
				return playersData;
			}
			@Override
			public RandomTeleportService getRandomTeleportService() {
				return rtpService;
			}
			@Override
			public boolean isForgeServer() {
				return isForge;
			}
			@Override
			public boolean isNeoForgeServer() {
				return isNeo;
			}
			@Override
			public boolean isModifiedServer() {
				return isForge || isNeo;
			}
			@Override
			public KitService getKitService() {
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
			@Override
			public Optional<PunishmentService> getPunishmentService() {
				return Optional.ofNullable(punishmentService);
			}
			@Override
			public Optional<CPEconomyService> getEconomyService() {
				return Optional.ofNullable(economy.getEconomyServiceImpl());
			}
			@Override
			public TPS getTPS() {
				return tps;
			}
			@Override
			public void registerCommand(RawCommand command) throws IllegalStateException {
				if(manager == null && isStarted) throw new IllegalStateException("Registration of commands through CommandPack is no longer available. Perform registration as soon as you receive the API.");
				if(command.getContainer() != null && command.isEnable() && !registeredRawCommands.stream().filter(raw -> raw.command().equals(command.command())).findFirst().isPresent()) registeredRawCommands.add(command);
			}
			@Override
			public void registerCommand(ParameterizedCommand command) throws IllegalStateException {
				if(manager == null && isStarted) throw new IllegalStateException("Registration of commands through CommandPack is no longer available. Perform registration as soon as you receive the API.");
				if(command.getContainer() != null && command.isEnable() && !registeredParameterizedCommands.stream().filter(parameterized -> parameterized.command().equals(command.command())).findFirst().isPresent()) registeredParameterizedCommands.add(command);
			}
			@Override
			public ContainersCollection getContainersCollection() {
				return colletions;
			}
		};
	}

	private void registerListeners() {
		Sponge.eventManager().registerListeners(pluginContainer, new CommandLogListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerChatListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerCommandListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerConnectionListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerInteractBlockListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerInteractEntityListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerInteractItemListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerInventoryListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerMoveListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new PlayerDeathAndRespawnListener(instance));
		Sponge.eventManager().registerListeners(pluginContainer, new EntityDamageListener(instance));
	}

	private void createTasks() {
		Sponge.asyncScheduler().submit(Task.builder().delay(15, TimeUnit.SECONDS).interval(5, TimeUnit.SECONDS).plugin(pluginContainer).execute(() -> {
			long currentTime = System.currentTimeMillis() / 1000;
			tps1m.keySet().removeIf(time -> (currentTime - 60 > time));
			tps5m.keySet().removeIf(time -> (currentTime - 300 > time));
			tps10m.keySet().removeIf(time -> (currentTime - 600 > time));
			tps1m.put(currentTime, Sponge.server().ticksPerSecond());
			tps5m.put(currentTime, Sponge.server().ticksPerSecond());
			tps10m.put(currentTime, Sponge.server().ticksPerSecond());
			if(getMainConfig().getAfkConfig().isEnable()) Sponge.server().onlinePlayers().forEach(player -> {
				if(getPlayersData().getTempData().getLastActivity(player) > 0) {
					if(getPlayersData().getTempData().isAfk(player) && !player.hasPermission(Permissions.AFK_UNLIMIT)) {
						if((playersData.getTempData().getLastActivity(player) + getMainConfig().getAfkConfig().getTurnOnDlay() +  getMainConfig().getAfkConfig().getKickDelay()) - Duration.ofMillis(System.currentTimeMillis()).getSeconds() <= 0) {
							player.kick(getLocales().getLocale(player.locale()).getCommands().getAfk().getKick());
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
						if(getConfigManager().getMainConfig().getAfkConfig().getAfkTitlesConfig().isUnlimit()) player.sendTitlePart(TitlePart.TITLE, locales.getLocale(player.locale()).getCommands().getAfk().getTitle());
					} else {
						if(getConfigManager().getMainConfig().getAfkConfig().getAfkTitlesConfig().isBeforeKick()) player.showTitle(Title.title(locales.getLocale(player.locale()).getCommands().getAfk().getTitle(), locales.getLocale(player.locale()).getCommands().getAfk().getSubtitle(timeFormat((playersData.getTempData().getLastActivity(player) + getMainConfig().getAfkConfig().getTurnOnDlay() +  getMainConfig().getAfkConfig().getKickDelay() + 1) - Duration.ofMillis(System.currentTimeMillis()).getSeconds(), player.locale()))));
					}
				}
			});
		}).build());
	}

}
