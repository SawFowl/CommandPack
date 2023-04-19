package sawfowl.commandpack.apiclasses;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.TempPlayerData;
import sawfowl.commandpack.api.data.player.PlayerData;
import sawfowl.commandpack.api.data.player.Warp;
import sawfowl.localeapi.api.TextUtils;

public class PlayersDataImpl implements sawfowl.commandpack.api.PlayersData {

	final CommandPack plugin;
	final TempPlayerData tempData;
	public PlayersDataImpl(CommandPack plugin) {
		this.plugin = plugin;
		tempData = new TempPlayerDataImpl(plugin);
	}

	private Map<UUID, PlayerData> players = new HashMap<>();

	private Map<String, Warp> adminWarps = new HashMap<>();

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

	public void reload() {
		players.clear();
		plugin.getConfigManager().loadPlayersData();
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
		return getPlayersWarps().stream().filter(warp -> (name.equals(TextUtils.clearDecorations(warp.asComponent())))).findFirst();
	}

	@Override
	public Optional<Warp> getWarp(String name, Predicate<Warp> filter) {
		return getPlayersWarps().stream().filter(warp -> (name.equals(TextUtils.clearDecorations(warp.asComponent())))).filter(filter).findFirst();
	}

	@Override
	public Map<String, Warp> getAdminWarps() {
		return adminWarps;
	}

	@Override
	public void addAdminWarp(Warp warp) {
		adminWarps.put(TextUtils.clearDecorations(warp.asComponent()), warp);
	}

	@Override
	public void addAndSaveAdminWarp(Warp warp) {
		adminWarps.put(TextUtils.clearDecorations(warp.asComponent()), warp);
		plugin.getConfigManager().saveAdminWarp(warp);
	}

	@Override
	public boolean removeAdminWarp(String name) {
		if(!adminWarps.containsKey(name)) return false;
		adminWarps.remove(name);
		plugin.getConfigManager().deleteAdminWarp(name);
		return true;
	}

	@Override
	public Optional<Warp> getAdminWarp(String name) {
		return adminWarps.containsKey(name) ? Optional.ofNullable(adminWarps.get(name)) : Optional.empty();
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
