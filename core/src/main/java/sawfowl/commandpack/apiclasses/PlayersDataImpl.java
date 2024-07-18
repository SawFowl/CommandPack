package sawfowl.commandpack.apiclasses;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.TempPlayerData;
import sawfowl.commandpack.api.data.player.PlayerData;
import sawfowl.commandpack.api.data.player.Warp;
import sawfowl.localeapi.api.TextUtils;

public class PlayersDataImpl implements sawfowl.commandpack.api.PlayersData {

	final CommandPackInstance plugin;
	final TempPlayerData tempData;
	public PlayersDataImpl(CommandPackInstance plugin) {
		this.plugin = plugin;
		tempData = new TempPlayerDataImpl(plugin);
	}

	private Map<UUID, PlayerData> players = new HashMap<>();

	private Map<String, Warp> adminWarps = new HashMap<>();

	private Set<Warp> allWarps = new HashSet<>();

	@Override
	public Optional<PlayerData> getPlayerData(UUID uuid) {
		return players.containsKey(uuid) ? Optional.ofNullable(players.get(uuid)) : Optional.empty();
	}

	@Override
	public PlayerData getOrCreatePlayerData(ServerPlayer player) {
		return getPlayerData(player.uniqueId()).orElse(new sawfowl.commandpack.configure.configs.player.PlayerData(player)).save();
	}

	@Override
	public Collection<PlayerData> getPlayersData() {
		return players.values();
	}

	public void addPlayerData(PlayerData playerData) {
		players.put(playerData.getUniqueId(), playerData);
	}

	public void addWarps(PlayerData playerData) {
		allWarps.addAll(playerData.getWarps());
	}

	public void reload() {
		players.clear();
		allWarps.clear();
		allWarps.addAll(adminWarps.values());
		plugin.getConfigManager().loadPlayersData();
	}

	@Override
	public Stream<Warp> streamAllWarps() {
		return allWarps.stream();
	}

	@Override
	public Map<String, Warp> getAdminWarps() {
		return adminWarps;
	}

	@Override
	public Collection<Warp> getPlayersWarps() {
		return players.values().stream().map(PlayerData::getWarps).flatMap(list -> (list.stream())).collect(Collectors.toList());
	}

	@Override
	public Collection<Warp> getPlayersWarps(Predicate<Warp> filter) {
		return players.values().stream().map(PlayerData::getWarps).flatMap(list -> (list.stream())).filter(filter).collect(Collectors.toList());
	}

	@Override
	public Optional<Warp> getWarp(String name) {
		return adminWarps.containsKey(name) ? Optional.ofNullable(adminWarps.get(name)) : getPlayersWarps().stream().filter(warp -> (name.equals(TextUtils.clearDecorations(warp.asComponent())))).findFirst();
	}

	@Override
	public Optional<Warp> getWarp(String name, Predicate<Warp> filter) {
		return allWarps.stream().filter(warp -> (name.equals(TextUtils.clearDecorations(warp.asComponent())))).filter(filter).findFirst();
	}

	@Override
	public void addWarp(Warp warp, PlayerData data) {
		if(data == null) {
			adminWarps.put(TextUtils.clearDecorations(warp.asComponent()), warp);
		} else {
			plugin.getConfigManager().savePlayerData(((sawfowl.commandpack.configure.configs.player.PlayerData) data).addWarp(warp));
		}
		allWarps.add(warp);
	}

	@Override
	public void addAndSaveWarp(Warp warp, PlayerData data) {
		allWarps.add(warp);
		if(data == null) {
			adminWarps.put(TextUtils.clearDecorations(warp.asComponent()), warp);
			plugin.getConfigManager().saveAdminWarp(warp);
		} else {
			plugin.getConfigManager().savePlayerData(((sawfowl.commandpack.configure.configs.player.PlayerData) data).addWarp(warp));
		}
	}

	@Override
	public boolean removeWarp(String name, PlayerData data) {
		allWarps.removeIf(warp -> warp.getPlainName().equals(name));
		if(data == null) {
			if(!adminWarps.containsKey(name)) return false;
			adminWarps.remove(name);
			plugin.getConfigManager().deleteAdminWarp(name);
		} else {
			plugin.getConfigManager().savePlayerData(((sawfowl.commandpack.configure.configs.player.PlayerData) data).removeWarp(name));
		}
		return true;
	}

	@Override
	public Optional<Warp> getAdminWarp(String name, Predicate<Warp> predicate) {
		return adminWarps.containsKey(name) && predicate.test(adminWarps.get(name)) ? Optional.ofNullable(adminWarps.get(name)) : Optional.empty();
	}

	@Override
	public TempPlayerData getTempData() {
		return tempData;
	}

}
