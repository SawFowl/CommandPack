package sawfowl.commandpack.apiclasses;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.ServerLocation;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.PluginCommand;
import sawfowl.commandpack.api.data.command.Settings;

public class TempPlayerDataImpl implements sawfowl.commandpack.api.TempPlayerData {

	private final CommandPackInstance plugin;
	private Map<String, Set<UUID>> trackingCommandDelay = new HashMap<>();
	private Map<String, Settings> commandsSettings = new HashMap<>();
	private Set<UUID> tptoggleSet = new HashSet<>();
	private Map<UUID, ServerLocation> locations = new HashMap<>();
	private Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();
	private Map<UUID, Long> lastActivity = new HashMap<>();
	private Set<UUID> afk = new HashSet<>();
	private Set<UUID> commandSpy = new HashSet<>();
	private Map<UUID, Long> vanishTime = new HashMap<>();
	private Map<UUID, Audience> replyMap = new HashMap<>();
	private Set<String> onlinePlayers = new HashSet<String>();
	private Set<String> users = new HashSet<String>();
	public TempPlayerDataImpl(CommandPackInstance plugin) {
		this.plugin = plugin;
		Sponge.eventManager().registerListeners(plugin.getPluginContainer(), this);
	}

	public void registerPlayer(ServerPlayer player) {
		onlinePlayers.add(player.name());
		users.add(player.name());
	}

	public void registerUser(String user) {
		users.add(user);
	}

	public void unregisterPlayer(ServerPlayer player) {
		onlinePlayers.removeIf(name -> name.equals(player.name()));
	}

	@Override
	public Stream<String> streamOnlinePlayers() {
		return onlinePlayers.stream();
	}

	@Override
	public Stream<String> streamUsers() {
		return users.stream();
	}

	@Override
	public Optional<CompletableFuture<Optional<User>>> getUser(String name) {
		return users.contains(name) ? Optional.ofNullable(Sponge.server().userManager().load(name)) : Optional.empty();
	}

	@Override
	public void registerCommandTracking(PluginCommand command) {
		trackingCommandDelay.put(command.trackingName(), new HashSet<UUID>());
		commandsSettings.put(command.trackingName(), command.getCommandSettings());
	}

	@Override
	public void addCommandTracking(String command, ServerPlayer player) {
		if(!trackingCommandDelay.containsKey(command)) plugin.getLogger().error(plugin.getLocales().getSystemLocale().getDebug().getCommands().getNotTracking(command));
		if(!trackingCommandDelay.get(command).contains(player.uniqueId())) trackingCommandDelay.get(command).add(player.uniqueId());
	}

	@Override
	public boolean isTrackingPlayer(ServerPlayer player) {
		return isTrackingPlayer(player.uniqueId());
	}

	@Override
	public boolean isTrackingPlayer(UUID uuid) {
		return trackingCommandDelay.values().stream().filter(list -> (list.contains(uuid))).findFirst().isPresent();
	}

	@Override
	public void removeCommandTracking(String command, ServerPlayer player) {
		removeCommandTracking(command, player.uniqueId());
	}

	@Override
	public void removeCommandTracking(String command, UUID uuid) {
		if(!trackingCommandDelay.containsKey(command)) {
			plugin.getLogger().error(plugin.getLocales().getSystemLocale().getDebug().getCommands().getNotTracking(command));
			return;
		}
		trackingCommandDelay.get(command).removeIf(u -> (u.equals(uuid)));
	}

	@Override
	public Optional<Map<String, Settings>> getTrackingPlayerCommands(ServerPlayer player) {
		return getTrackingPlayerCommands(player.uniqueId());
	}

	@Override
	public Optional<Map<String, Settings>> getTrackingPlayerCommands(UUID uuid) {
		for(Entry<String, Set<UUID>> entry : trackingCommandDelay.entrySet()) {
			if(entry.getValue().contains(uuid) && commandsSettings.containsKey(entry.getKey())) {
				Settings commandSettings = commandsSettings.get(entry.getKey());
				Map<String, Settings> commands = new HashMap<>();
				commands.put(entry.getKey(), commandSettings);
				return Optional.ofNullable(commands);
			}
		}
		return Optional.empty();
	}

	@Override
	public void tpToggle(ServerPlayer player) {
		if(isDisableTpRequests(player)) {
			tptoggleSet.remove(player.uniqueId());
		} else tptoggleSet.add(player.uniqueId());
	}

	@Override
	public boolean isDisableTpRequests(ServerPlayer player) {
		return tptoggleSet.contains(player.uniqueId());
	}

	@Override
	public Optional<ServerLocation> getPreviousLocation(ServerPlayer player) {
		return Optional.ofNullable(locations.getOrDefault(player.uniqueId(), null));
	}

	@Override
	public void setPreviousLocation(ServerPlayer player) {
		if(locations.containsKey(player.uniqueId())) locations.remove(player.uniqueId());
		locations.put(player.uniqueId(), player.serverLocation());
	}

	@Override
	public void addTrackingCooldownCommand(PluginCommand command) {
		addTrackingCooldownCommand(command.trackingName());
	}

	@Override
	public void addTrackingCooldownCommand(String command) {
		cooldowns.put(command, new HashMap<>());
	}

	@Override
	public Map<UUID, Long> getTrackingMap(PluginCommand command) {
		return getTrackingMap(command.trackingName());
	}

	@Override
	public Map<UUID, Long> getTrackingMap(String command) {
		if(!cooldowns.containsKey(command)) cooldowns.put(command, new HashMap<>());
		return cooldowns.get(command);
	}

	@Override
	public void updateLastActivity(ServerPlayer player) {
		if(lastActivity.containsKey(player.uniqueId())) lastActivity.remove(player.uniqueId());
		lastActivity.put(player.uniqueId(), Duration.ofMillis(System.currentTimeMillis()).getSeconds());
		UUID uuid = player.uniqueId();
		Sponge.asyncScheduler().submit(Task.builder().delay(Ticks.of(10)).plugin(plugin.getPluginContainer()).execute(() -> {
			if(isAfk(player) && Sponge.server().player(uuid).isPresent()) {
				afk.remove(player.uniqueId());
				if(!player.get(Keys.VANISH_STATE).isPresent() || !player.get(Keys.VANISH_STATE).get().invisible()) {
					Sponge.systemSubject().sendMessage(plugin.getLocales().getSystemLocale().getCommands().getAfk().getDisableBroadcast(player));
					Sponge.server().onlinePlayers().forEach(p -> {
						p.sendMessage(plugin.getLocales().getLocale(p).getCommands().getAfk().getDisableBroadcast(player));
					});
				} else player.sendMessage(plugin.getLocales().getLocale(player).getCommands().getAfk().getDisableInVanish());
			}
		}).build());
	}

	@Override
	public long getLastActivity(ServerPlayer player) {
		return lastActivity.getOrDefault(player.uniqueId(), 0l);
	}

	@Override
	public boolean isAfk(ServerPlayer player) {
		return afk.contains(player.uniqueId()) && lastActivity.containsKey(player.uniqueId());
	}

	@Override
	public void setAfkStatus(ServerPlayer player) {
		if(isAfk(player)) return;
		afk.add(player.uniqueId());
		if(!player.get(Keys.VANISH_STATE).isPresent() || !player.get(Keys.VANISH_STATE).get().invisible()) {
			Sponge.systemSubject().sendMessage(plugin.getLocales().getSystemLocale().getCommands().getAfk().getEnableBroadcast(player));
			Sponge.server().onlinePlayers().forEach(p -> {
				p.sendMessage(plugin.getLocales().getLocale(p).getCommands().getAfk().getEnableBroadcast(player));
			});
		} else player.sendMessage(plugin.getLocales().getLocale(player).getCommands().getAfk().getEnableInVanish());
	}

	@Override
	public void switchSpyCommand(ServerPlayer player) {
		if(isSpyCommand(player)) {
			commandSpy.remove(player.uniqueId());
		} else if(player.hasPermission(Permissions.COMMANDSPY_STAFF)) commandSpy.add(player.uniqueId());
	}

	@Override
	public boolean isSpyCommand(ServerPlayer player) {
		if(!player.hasPermission(Permissions.COMMANDSPY_STAFF)) {
			if(commandSpy.contains(player.uniqueId())) commandSpy.remove(player.uniqueId());
			return false;
		}
		return commandSpy.contains(player.uniqueId());
	}

	@Override
	public void setVanishTime(ServerPlayer player) {
		removeVanishEnabledTime(player);
		vanishTime.put(player.uniqueId(), System.currentTimeMillis());
	}

	@Override
	public boolean isVanished(ServerPlayer player) {
		return vanishTime.containsKey(player.uniqueId()) && player.get(Keys.VANISH_STATE).isPresent() && player.get(Keys.VANISH_STATE).get().invisible();
	}

	@Override
	public Optional<Long> getVanishEnabledTime(ServerPlayer player) {
		return Optional.ofNullable(vanishTime.getOrDefault(player.uniqueId(), null));
	}


	@Override
	public void removeVanishEnabledTime(ServerPlayer player) {
		if(vanishTime.containsKey(player.uniqueId())) vanishTime.remove(player.uniqueId());
	}

	@Listener
	public void onDisconnect(ServerSideConnectionEvent.Leave event) {
		if(lastActivity.containsKey(event.player().uniqueId())) lastActivity.remove(event.player().uniqueId());
		if(isAfk(event.player())) afk.remove(event.player().uniqueId());
	}

	@Override
	public void addReply(ServerPlayer player, Audience audience) {
		removeReply(player);;
		replyMap.put(player.uniqueId(), audience);
	}

	@Override
	public void removeReply(ServerPlayer player) {
		if(replyMap.containsKey(player.uniqueId())) replyMap.remove(player.uniqueId());
	}

	@Override
	public Optional<Audience> getReply(ServerPlayer player) {
		return replyMap.containsKey(player.uniqueId()) ? (replyMap.get(player.uniqueId()) instanceof ServerPlayer ? Sponge.server().player(((ServerPlayer) replyMap.get(player.uniqueId())).uniqueId()).map(p -> (Audience) p) :  Optional.ofNullable(replyMap.get(player.uniqueId()))) : Optional.empty();
	}

}
