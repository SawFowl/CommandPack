package sawfowl.commandpack.apiclasses;

import java.net.InetAddress;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.api.service.ban.Ban.Profile;

import sawfowl.commandpack.api.CommandPack;
import sawfowl.commandpack.api.PunishmentService;

public class PunishmentServiceImpl implements PunishmentService {

	final CommandPack plugin;
	public PunishmentServiceImpl(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Override
	public CompletableFuture<Collection<? extends Ban>> bans() {
		return null;
	}

	@Override
	public CompletableFuture<Collection<Profile>> profileBans() {
		return null;
	}

	@Override
	public CompletableFuture<Collection<IP>> ipBans() {
		return null;
	}

	@Override
	public CompletableFuture<Optional<Profile>> find(GameProfile profile) {
		return null;
	}

	@Override
	public CompletableFuture<Optional<IP>> find(InetAddress address) {
		return null;
	}

	@Override
	public CompletableFuture<Boolean> pardon(GameProfile profile) {
		return null;
	}

	@Override
	public CompletableFuture<Boolean> pardon(InetAddress address) {
		return null;
	}

	@Override
	public CompletableFuture<Boolean> remove(Ban ban) {
		return null;
	}

	@Override
	public CompletableFuture<Optional<? extends Ban>> add(Ban ban) {
		return null;
	}

}
