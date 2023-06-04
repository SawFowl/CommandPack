package sawfowl.commandpack.apiclasses;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.world.server.ServerLocation;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.PluginCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class TempPlayerDataImpl implements sawfowl.commandpack.api.TempPlayerData {

	private final CommandPack plugin;
	private Map<String, List<UUID>> trackingCommandDelay = new HashMap<>();
	private Set<UUID> tptoggleSet = new HashSet<>();
	private Map<UUID, ServerLocation> locations = new HashMap<>();
	private Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();
	private Map<UUID, Long> lastActivity = new HashMap<>();
	private Set<UUID> afk = new HashSet<>();
	private Set<UUID> commandSpy = new HashSet<>();
	public TempPlayerDataImpl(CommandPack plugin) {
		this.plugin = plugin;
		Sponge.eventManager().registerListeners(plugin.getPluginContainer(), this);
	}

	@Override
	public void addCommandTracking(String command, ServerPlayer player) {
		if(!trackingCommandDelay.containsKey(command)) {
			if(plugin.getCommandsConfig().getCommandConfig(command).getAliases().length == 0) {
				plugin.getLogger().error(TextUtils.replace(plugin.getLocales().getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_NOT_TRACKING), Placeholders.COMMAND, command));
				return;
			} else {
				trackingCommandDelay.keySet().stream().filter(c -> (!plugin.getCommandsConfig().getCommandConfig(c).getAliasesList().contains(command))).findFirst().ifPresent(c -> {
					trackingCommandDelay.get(c).add(player.uniqueId());
					return;
				});
			}
		} else trackingCommandDelay.get(command).add(player.uniqueId());
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
			plugin.getLogger().error(TextUtils.replace(plugin.getLocales().getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_NOT_TRACKING), Placeholders.COMMAND, command));
			return;
		}
		trackingCommandDelay.get(command).removeIf(u -> (u.equals(uuid)));
	}

	@Override
	public Optional<Map<String, CommandSettings>> getTrackingPlayerCommands(ServerPlayer player) {
		return getTrackingPlayerCommands(player.uniqueId());
	}

	@Override
	public Optional<Map<String, CommandSettings>> getTrackingPlayerCommands(UUID uuid) {
		Map<String, CommandSettings> commands = new HashMap<>();
		trackingCommandDelay.entrySet().stream().filter(entry -> (entry.getValue().contains(uuid))).forEach(entry -> {
			CommandSettings commandSettings = plugin.getCommandsConfig().getCommandConfig(entry.getKey());
			commands.put(entry.getKey(), commandSettings);
		});
		return !isTrackingPlayer(uuid) || commands.isEmpty() ? Optional.empty() : Optional.ofNullable(commands);
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
		if(Sponge.server().commandManager().knownAliases().contains(command)) return new HashMap<>();
		return !cooldowns.containsKey(command) ? cooldowns.put(command, new HashMap<>()) : cooldowns.get(command);
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
					Sponge.systemSubject().sendMessage(TextUtils.replace(plugin.getLocales().getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_AFK_DISABLE), Placeholders.PLAYER, player.get(Keys.CUSTOM_NAME).orElse(Component.text(player.name()))));
					Sponge.server().onlinePlayers().forEach(p -> {
						p.sendMessage(TextUtils.replace(plugin.getLocales().getText(p.locale(), LocalesPaths.COMMANDS_AFK_DISABLE), Placeholders.PLAYER, player.get(Keys.CUSTOM_NAME).orElse(Component.text(player.name()))));
					});
				} else player.sendMessage(plugin.getLocales().getText(player.locale(), LocalesPaths.COMMANDS_AFK_DISABLE_IN_VANISH));
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
			Sponge.systemSubject().sendMessage(TextUtils.replace(plugin.getLocales().getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_AFK_ENABLE), Placeholders.PLAYER, player.get(Keys.CUSTOM_NAME).orElse(Component.text(player.name()))));
			Sponge.server().onlinePlayers().forEach(p -> {
				p.sendMessage(TextUtils.replace(plugin.getLocales().getText(p.locale(), LocalesPaths.COMMANDS_AFK_ENABLE), Placeholders.PLAYER, player.get(Keys.CUSTOM_NAME).orElse(Component.text(player.name()))));
			});
		} else player.sendMessage(plugin.getLocales().getText(player.locale(), LocalesPaths.COMMANDS_AFK_ENABLE_IN_VANISH));
	}

	@Override
	public void switchSpyCommand(ServerPlayer player) {
		if(isSpyCommand(player)) {
			commandSpy.remove(player.uniqueId());
		} else if(player.hasPermission(Permissions.COMMANDSPY)) commandSpy.add(player.uniqueId());
	}

	@Override
	public boolean isSpyCommand(ServerPlayer player) {
		if(!player.hasPermission(Permissions.COMMANDSPY)) {
			if(commandSpy.contains(player.uniqueId())) commandSpy.remove(player.uniqueId());
			return false;
		}
		return commandSpy.contains(player.uniqueId());
	}

	@Listener
	public void onDisconnect(ServerSideConnectionEvent.Disconnect event) {
		if(lastActivity.containsKey(event.player().uniqueId())) lastActivity.remove(event.player().uniqueId());
		if(isAfk(event.player())) afk.remove(event.player().uniqueId());
	}

}
