package sawfowl.commandpack.apiclasses.punishment;

import java.net.InetAddress;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.api.service.ban.Ban.Profile;
import org.spongepowered.api.service.ban.BanTypes;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.PunishmentService;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warn;
import sawfowl.commandpack.api.data.punishment.Warns;
import sawfowl.commandpack.apiclasses.punishment.storage.AbstractPunishmentStorage;
import sawfowl.commandpack.apiclasses.punishment.storage.FileStorage;
import sawfowl.commandpack.apiclasses.punishment.storage.H2Storage;
import sawfowl.commandpack.apiclasses.punishment.storage.MySqlStorage;
import sawfowl.commandpack.utils.MariaDB;
import sawfowl.commandpack.utils.StorageType;

public class PunishmentServiceImpl implements PunishmentService {

	final CommandPack plugin;
	private AbstractPunishmentStorage storage;
	public PunishmentServiceImpl(CommandPack plugin) {
		this.plugin = plugin;
		switch (plugin.getMainConfig().getPunishment().getStorageType()) {
			case H2:
				try {
					Class.forName("org.h2.Driver");
					storage = new H2Storage(plugin);
					plugin.getLogger().info("The h2 database is used to store the punishment system data.");
				} catch (Exception e) {
					storage = new FileStorage(plugin);
					plugin.getLogger().warn("H2 driver not found! Configuration files based data storage will be used.");
				}
				break;
			case MYSQL:
				Optional<MariaDB> optMariaDB = plugin.getMariaDB();
				if(optMariaDB.isPresent()) {
					storage = new MySqlStorage(plugin);
					plugin.getLogger().info("The MySql database is used to store the punishment system data.");
				} else {
					storage = new FileStorage(plugin);
					plugin.getLogger().error("MySql or MariaDB Driver not found! Configuration files based data storage will be used.");
				}
				break;
			default:
				storage = new FileStorage(plugin);
				break;
		}
	}

	@Override
	public void saveBans(Profile profile, IP ip) {
		if(storage.getStorageType() == StorageType.MYSQL) {
			((MySqlStorage) storage).saveBans(profile, ip);
		} else {
			if(profile != null) storage.saveBan(profile);
			if(ip != null) storage.saveBan(ip);
		}
	}

	@Override
	public CompletableFuture<Collection<? extends Ban>> bans() {
		return CompletableFuture.completedFuture(getAllBans());
	}

	@Override
	public Collection<Ban> getAllBans() {
		Collection<Ban> bans = storage.getBans().values().stream().map(b -> (Ban) b).collect(Collectors.toList());
		bans.addAll(storage.getBansIP().values());
		return bans;
	}

	@Override
	public CompletableFuture<Collection<Profile>> profileBans() {
		return CompletableFuture.completedFuture(getAllProfileBans());
	}

	@Override
	public Collection<Profile> getAllProfileBans() {
		return storage.getBans().values();
	}

	@Override
	public CompletableFuture<Collection<IP>> ipBans() {
		return CompletableFuture.completedFuture(getAllIPBans());
	}

	@Override
	public Collection<IP> getAllIPBans() {
		return storage.getBansIP().values();
	}

	@Override
	public CompletableFuture<Optional<Profile>> find(GameProfile profile) {
		return CompletableFuture.completedFuture(storage.getBan(profile.uniqueId()));
	}

	@Override
	public CompletableFuture<Optional<IP>> find(InetAddress address) {
		return CompletableFuture.completedFuture(storage.getBanIP(address));
	}

	@Override
	public CompletableFuture<Boolean> pardon(GameProfile profile) {
		return CompletableFuture.completedFuture(storage.deleteBan(profile));
	}

	@Override
	public CompletableFuture<Boolean> pardon(InetAddress address) {
		return CompletableFuture.completedFuture(storage.deleteIPBan(address));
	}

	@Override
	public CompletableFuture<Boolean> remove(Ban ban) {
		if(ban.type().equals(BanTypes.IP.get())) {
			return pardon(((IP) ban).address());
		} else return pardon(((Profile) ban).profile());
	}

	@Override
	public CompletableFuture<Optional<? extends Ban>> add(Ban ban) {
		if(ban.type().equals(BanTypes.IP.get())) {
			storage.saveBan((IP) ban);
		} else storage.saveBan((Profile) ban);
		return CompletableFuture.completedFuture(Optional.ofNullable(ban));
	}

	@Override
	public Optional<Profile> findBan(UUID uuid) {
		return storage.getBan(uuid);
	}

	@Override
	public Optional<Mute> getMute(UUID uuid) {
		return storage.getMute(uuid);
	}

	@Override
	public Optional<Mute> getMute(ServerPlayer player) {
		return getMute(player.uniqueId());
	}

	@Override
	public Collection<Mute> getAllMutes() {
		return storage.getMutes().values();
	}

	@Override
	public void addMute(Mute mute) {
		storage.saveMute(mute);
	}

	@Override
	public boolean removeMute(Mute mute) {
		return storage.deleteMute(mute);
	}

	@Override
	public Optional<Warns> getWarns(UUID uuid) {
		return storage.getWarns(uuid);
	}

	@Override
	public Optional<Warns> getWarns(ServerPlayer player) {
		return getWarns(player.uniqueId());
	}

	@Override
	public Collection<Warns> getAllWarns() {
		return storage.getWarns().values();
	}

	@Override
	public void addWarns(Warns warns) {
		storage.saveWarns(warns);
	}

	@Override
	public void addWarn(User user, Warn warn) {
		Optional<Warns> optWarns = getWarns(user.uniqueId());
		if(optWarns.isPresent()) {
			Warns warns = optWarns.get();
			warns.addWarn(warn);
			storage.saveWarns(warns);
		} else addWarns(Warns.builder().target(user).warn(warn).build());
	}

	@Override
	public void removeWarn(UUID user, Warn warn) {
		getWarns(user).ifPresent(warns -> {
			warns.removeWarn(warn);
			storage.saveWarns(warns);
		});
	}

	@Override
	public void removeWarn(User user, Warn warn) {
		removeWarn(user.uniqueId(), warn);
	}

	@Override
	public boolean removeWarns(UUID player) {
		return storage.deleteWarns(player);
	}

}
