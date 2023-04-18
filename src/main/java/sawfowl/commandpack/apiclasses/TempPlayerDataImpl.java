package sawfowl.commandpack.apiclasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerLocation;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.commands.PluginCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class TempPlayerDataImpl implements sawfowl.commandpack.api.TempPlayerData {

	private final CommandPack plugin;
	private Map<String, List<UUID>> trackingCommandDelay = new HashMap<>();
	private Component notTracking;
	private Set<UUID> tptoggleSet = new HashSet<>();
	private Map<UUID, ServerLocation> locations = new HashMap<>();
	private Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();
	public TempPlayerDataImpl(CommandPack plugin) {
		this.plugin = plugin;
		notTracking = plugin.getLocales().getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_NOT_TRACKING);
		plugin.getConfigManager().getCommandsConfig().node().childrenMap().keySet().forEach(key -> {
			trackingCommandDelay.put(key.toString().toLowerCase(), new ArrayList<>());
		});
	}

	@Override
	public void addCommandTracking(String command, ServerPlayer player) {
		if(!trackingCommandDelay.containsKey(command)) {
			if(plugin.getCommandsConfig().getCommandConfig(command).getAliases().length == 0) {
				plugin.getLogger().error(TextUtils.replace(notTracking, Placeholders.COMMAND, command));
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
			plugin.getLogger().error(TextUtils.replace(notTracking, Placeholders.COMMAND, command));
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
		addTrackingCooldownCommand(command);
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

}
