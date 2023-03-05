package sawfowl.commandpack.apiclasses;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.player.PlayerData;

public class PlayersData implements sawfowl.commandpack.api.PlayersData {

	final CommandPack plugin;
	public PlayersData(CommandPack plugin) {
		this.plugin = plugin;
	}

	private Map<UUID, PlayerData> players = new HashMap<>();

	@Override
	public Optional<PlayerData> getPlayerData(UUID uuid) {
		return players.containsKey(uuid) ? Optional.ofNullable(players.get(uuid)) : Optional.empty();
	}

	@Override
	public PlayerData getOrCreatePlayerData(ServerPlayer player) {
		return getPlayerData(player.uniqueId()).orElse(new sawfowl.commandpack.configure.configs.player.PlayerData(player));
	}

	public void addPlayerData(PlayerData playerData) {
		players.put(playerData.getUniqueId(), playerData);
	}

	public void reload() {
		players.clear();
		plugin.getConfigManager().loadPlayersData();
	}

}
