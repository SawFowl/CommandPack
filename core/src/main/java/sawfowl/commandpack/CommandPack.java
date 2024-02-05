package sawfowl.commandpack;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.nio.file.Path;

import java.time.Duration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.Command.Raw;
import org.spongepowered.api.command.registrar.tree.CommandTreeNode;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.lifecycle.ProvideServiceEvent;
import org.spongepowered.api.event.lifecycle.RefreshGameEvent;
import org.spongepowered.api.event.lifecycle.RegisterBuilderEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.scheduler.ScheduledTask;
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

import sawfowl.localeapi.api.TextUtils;
import sawfowl.localeapi.api.event.LocaleServiseEvent;
import sawfowl.commandpack.api.KitService;
import sawfowl.commandpack.api.PlayersData;
import sawfowl.commandpack.api.RandomTeleportService;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.data.command.CancelRules;
import sawfowl.commandpack.api.data.command.Delay;
import sawfowl.commandpack.api.data.command.Price;
import sawfowl.commandpack.api.data.command.RawSettings;
import sawfowl.commandpack.api.data.command.Settings;
import sawfowl.commandpack.api.data.command.UpdateTree;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.api.data.kits.KitPrice;
import sawfowl.commandpack.api.data.miscellaneous.Location;
import sawfowl.commandpack.api.data.miscellaneous.ModContainer;
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
import sawfowl.commandpack.apiclasses.CustomPacketImpl;
import sawfowl.commandpack.apiclasses.KitServiceImpl;
import sawfowl.commandpack.apiclasses.PlayersDataImpl;
import sawfowl.commandpack.apiclasses.RTPService;
import sawfowl.commandpack.apiclasses.punishment.PunishmentServiceImpl;
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
import sawfowl.commandpack.configure.configs.commands.RawSettingsImpl;
import sawfowl.commandpack.configure.configs.commands.UpdateRawTree;
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
import sawfowl.commandpack.configure.locale.LocalesPaths;
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
import sawfowl.commandpack.utils.Logger;
import sawfowl.commandpack.utils.MariaDB;
import sawfowl.commandpack.utils.ModListGetter;

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
	private PunishmentService punishmentService;
	private MariaDB mariaDB;
	private Map<String, ChunkGenerator> generators = new HashMap<>();
	private Collection<PluginContainer> containers = new HashSet<>();
	private Collection<ModContainer> mods = new HashSet<>();
	private List<RawUpdater> registeredCommands = new ArrayList<RawUpdater>();
	private ScheduledTask rawTask;
	private boolean isStarted = false;
	private SpongeCommandManager manager;

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

	public PunishmentService getPunishmentService() {
		return punishmentService;
	}

	public Optional<MariaDB> getMariaDB() {
		return Optional.ofNullable(mariaDB);
	}

	public void updateRawTrees() {
		registeredCommands.forEach(RawUpdater::forceUpdate);
	}

	public boolean isStarted() {
		return isStarted;
	}

	public Optional<CommandTreeNode.Root> getRootCommandNode(RawCommand raw) {
		return registeredCommands.stream().filter(command -> command.command == raw || command.command.command().equals(raw.command())).findFirst().filter(command -> command.root != null).map(command -> command.root);
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
		configManager = new ConfigManager(instance);
		locales = new Locales(event.getLocaleService(), getMainConfig().isJsonLocales());
		configManager.loadPlayersData();
		isForge = checkForge();
		economy = new Economy(instance);
		Sponge.eventManager().registerListeners(pluginContainer, economy);
		fillLists();
		AverageTPS averageTPS = new AverageTPS() {
			
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
		TPS tps = new TPS() {
			
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
			public Collection<PluginContainer> getPluginContainers() {
				return containers;
			}

			@Override
			public Collection<ModContainer> getModContainers() {
				return mods;
			}

			@Override
			public void updateCommandTree(String command) {
				registeredCommands.stream().filter(updater -> updater.command.command().equals(command) || (updater.command.getCommandSettings() != null && updater.command.getCommandSettings().containsAlias(command))).findFirst().ifPresent(updater -> {
					updater.forceUpdate();
				});
			}

			@Override
			public void registerRawCommand(RawCommand raw) throws IllegalStateException {
				if(manager == null && isStarted) throw new IllegalStateException("Registration of commands through CommandPack is no longer available. Perform registration as soon as you receive the API.");
				if(raw.getContainer() != null && raw.isEnable()) registeredCommands.add(new RawUpdater(raw));
			}

			@Override
			public void updateCommandsTree(String... commands) {
				if(commands != null && commands.length > 0) Stream.of(commands).forEach(this::updateCommandTree);
			}

		};
		if(getMainConfig().getMySqlConfig().isEnable()) {
			mariaDB = new MariaDB(instance);
			if(mariaDB.openConnection() == null) mariaDB = null;
		}
	}

	@Listener
	public void onServerStarted(StartedEngineEvent<Server> event) {
		isStarted = true;
		manager = (SpongeCommandManager) Sponge.server().commandManager();
		if(!Sponge.server().serviceProvider().economyService().isPresent()) logger.warn(locales.getText(Sponge.server().locale(), LocalesPaths.ECONOMY_NOT_FOUND));
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
		configManager.loadKits();
		generators.put("empty", ChunkGenerator.flat(((AbstractBuilder<FlatGeneratorConfig>) FlatGeneratorConfig.builder().structureSets(null).biome(Biomes.THE_VOID).addLayer(LayerConfig.of(0, BlockTypes.AIR.get().defaultState()))).build()));
		generators.put("overworld", ChunkGenerator.overworld());
		generators.put("end", ChunkGenerator.theEnd());
		generators.put("nether", ChunkGenerator.theNether());
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
			if(getMainConfig().getAfkConfig().isEnable()) Sponge.server().onlinePlayers().forEach(player -> {
				if(getPlayersData().getTempData().getLastActivity(player) > 0) {
					if(getPlayersData().getTempData().isAfk(player) && !player.hasPermission(Permissions.AFK_UNLIMIT)) {
						if((playersData.getTempData().getLastActivity(player) + getMainConfig().getAfkConfig().getTurnOnDlay() +  getMainConfig().getAfkConfig().getKickDelay()) - Duration.ofMillis(System.currentTimeMillis()).getSeconds() <= 0) {
							player.kick(getLocales().getComponent(player.locale(), LocalesPaths.COMMANDS_AFK_KICK));
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
						if(getConfigManager().getMainConfig().getAfkConfig().getAfkTitlesConfig().isUnlimit()) player.sendTitlePart(TitlePart.TITLE, locales.getComponent(player.locale(), LocalesPaths.COMMANDS_AFK_TITLE));
					} else {
						if(getConfigManager().getMainConfig().getAfkConfig().getAfkTitlesConfig().isBeforeKick()) player.showTitle(Title.title(locales.getComponent(player.locale(), LocalesPaths.COMMANDS_AFK_TITLE), locales.getText(player.locale(), LocalesPaths.COMMANDS_AFK_SUBTITLE).replace(Placeholders.VALUE, timeFormat((playersData.getTempData().getLastActivity(player) + getMainConfig().getAfkConfig().getTurnOnDlay() +  getMainConfig().getAfkConfig().getKickDelay() + 1) - Duration.ofMillis(System.currentTimeMillis()).getSeconds(), player.locale())).get()));
					}
				}
			});
		}).build());
		serverStartedTime = System.currentTimeMillis();
		registeredCommands.forEach(RawUpdater::register);
		createRawTask();
		manager = null;
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
	public void onReload(RefreshGameEvent event) {
		configManager.reloadConfigs();
		((PlayersDataImpl) playersData).reload();
		createRawTask();
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
		event.register(RawSettings.Builder.class, () -> new RawSettingsImpl().builder());
		event.register(UpdateTree.Builder.class, () -> new UpdateRawTree().builder());
	}

	@Listener
	public void onStop(StoppingEngineEvent<Server> event) {
		Sponge.asyncScheduler().tasks(pluginContainer).forEach(task -> {
			task.cancel();
		});
	}

	void fillLists() {
		if(isForgeServer()) {
			mods = ModListGetter.getMods();
			containers = ModListGetter.getPlugins(mods);
		} else containers = Collections.unmodifiableCollection(new ArrayList<>(Sponge.pluginManager().plugins()));
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
		return TextUtils.timeFormat(second, locale, getLocales().getComponent(locale, LocalesPaths.TIME_DAYS), getLocales().getComponent(locale, LocalesPaths.TIME_HOUR), getLocales().getComponent(locale, LocalesPaths.TIME_MINUTE), getLocales().getComponent(locale, LocalesPaths.TIME_SECOND));
	}

	private void createRawTask() {
		if(rawTask != null) {
			rawTask.cancel();
			rawTask = null;
		}
		rawTask = Sponge.asyncScheduler().submit(Task.builder()
			.interval(20, TimeUnit.SECONDS)
			.plugin(pluginContainer)
			.execute(() -> {
				registeredCommands.forEach(RawUpdater::updateRawTree);
			})
			.build()
		);
	}

	class RawUpdater {

		private RawCommand command;
		private long lastUpdate;
		private CommandTreeNode.Root root;
		RawUpdater(RawCommand command) {
			this.command = command;
			root = command.commandTree();
			lastUpdate = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		}

		void register() {
			if(command.getCommandSettings() != null && command.getCommandSettings().getAliases() != null && command.getCommandSettings().getAliases().length > 0) {
				manager.registrar(Raw.class).get().register(command.getContainer(), command, command.command(), command.getCommandSettings().getAliases());
			} else manager.registrar(Raw.class).get().register(command.getContainer(), command, command.command());
		}

		void forceUpdate() {
			if(command.getCommandSettings() == null || command.getCommandSettings().getRawSettings() == null || !command.getCommandSettings().getRawSettings().isGenerateRawTree()) return;
			Sponge.asyncScheduler().executor(pluginContainer).execute(() -> {
				root.redirect(command.buildNewCommandTree());
				lastUpdate = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
			});
		}

		void updateRawTree() {
			if(
				command.getCommandSettings() == null ||
				command.getCommandSettings().getRawSettings() == null ||
				command.getCommandSettings().getRawSettings().getUpdateTree() == null ||
				!command.getCommandSettings().getRawSettings().getUpdateTree().isEnable() ||
				command.getCommandSettings().getRawSettings().getUpdateTree().getInterval() <= 0 ||
				lastUpdate + command.getCommandSettings().getRawSettings().getUpdateTree().getInterval() >= TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
			) return;
			root.redirect(command.buildNewCommandTree());
			lastUpdate = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		}

		CommandTreeNode.Root getCommandNodeRoot() {
			return root;
		}

	}

}
