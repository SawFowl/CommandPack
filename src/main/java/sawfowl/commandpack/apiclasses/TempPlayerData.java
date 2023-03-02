package sawfowl.commandpack.apiclasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.iTempPlayerData;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.configs.commands.CommandSettings;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class TempPlayerData implements iTempPlayerData {

	private final CommandPack plugin;
	private Map<String, List<UUID>> trackingCommandDelay = new HashMap<>();
	private Component notTracking;
	public TempPlayerData(CommandPack plugin) {
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
		return trackingCommandDelay.values().stream().filter(list -> (list.contains(player.uniqueId()))).findFirst().isPresent();
	}

	@Override
	public void removeCommandTracking(String command, ServerPlayer player) {
		if(!trackingCommandDelay.containsKey(command)) {
			plugin.getLogger().error(TextUtils.replace(notTracking, Placeholders.COMMAND, command));
			return;
		}
		trackingCommandDelay.get(command).removeIf(uuid -> (uuid.equals(player.uniqueId())));
	}

	@Override
	public Optional<Map<String, CommandSettings>> getTrackingPlayerCommands(ServerPlayer player) {
		Map<String, CommandSettings> commands = new HashMap<>();
		trackingCommandDelay.entrySet().stream().filter(entry -> (entry.getValue().contains(player.uniqueId()))).forEach(entry -> {
			CommandSettings commandSettings = plugin.getCommandsConfig().getCommandConfig(entry.getKey());
			commands.put(entry.getKey(), commandSettings);
		});
		return !isTrackingPlayer(player) || commands.isEmpty() ? Optional.empty() : Optional.ofNullable(commands);
	}

}
