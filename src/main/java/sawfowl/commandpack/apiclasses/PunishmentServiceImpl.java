package sawfowl.commandpack.apiclasses;

import java.net.InetAddress;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.api.service.ban.Ban.Profile;
import org.spongepowered.api.service.ban.BanTypes;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.PunishmentService;
import sawfowl.commandpack.apiclasses.punishment.storage.AbstractPunishmentStorage;
import sawfowl.commandpack.apiclasses.punishment.storage.FileStorage;

public class PunishmentServiceImpl implements PunishmentService {

	final CommandPack plugin;
	private AbstractPunishmentStorage storage;
	public PunishmentServiceImpl(CommandPack plugin) {
		this.plugin = plugin;
		switch (plugin.getMainConfig().getPunishment().getStorageType()) {

		default:
			storage = new FileStorage(plugin);
			break;
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

}
