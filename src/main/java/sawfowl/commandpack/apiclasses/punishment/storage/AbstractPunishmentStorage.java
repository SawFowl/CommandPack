package sawfowl.commandpack.apiclasses.punishment.storage;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.api.service.ban.Ban.Profile;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warns;

public abstract class AbstractPunishmentStorage {

	final CommandPack plugin;
	Map<UUID, Ban.Profile> bans = new HashMap<>();
	Map<InetAddress, Ban.IP> bansIP = new HashMap<>();
	Map<UUID, Mute> mutes = new HashMap<>();
	Map<UUID, Warns> warns = new HashMap<>();
	public AbstractPunishmentStorage(CommandPack plugin) {
		this.plugin = plugin;
		load();
	}

	public Map<UUID, Ban.Profile> getBans() {
		return bans;
	}

	public Map<InetAddress, Ban.IP> getBansIP() {
		return bansIP;
	}

	public Map<UUID, Mute> getMutes() {
		return mutes;
	}

	public Map<UUID, Warns> getWarns() {
		return warns;
	}

	public Optional<Ban.Profile> getBan(UUID uuid) {
		return Optional.ofNullable(bans.getOrDefault(uuid, null));
	}

	public Optional<Ban.IP> getBanIP(InetAddress address) {
		return Optional.ofNullable(bansIP.getOrDefault(address, null));
	}

	public Optional<Mute> getMute(UUID uuid) {
		return Optional.ofNullable(mutes.getOrDefault(uuid, null));
	}

	public Optional<Warns> getWarns(UUID uuid) {
		return Optional.ofNullable(warns.getOrDefault(uuid, null));
	}

	public abstract void saveBan(Profile ban);

	public abstract boolean deleteBan(GameProfile profile);

	public abstract void saveBan(IP ban);

	public abstract boolean deleteIPBan(InetAddress address);

	public abstract void saveMute(Mute mute);

	public abstract void deleteMute(Mute mute);

	public abstract void saveWarns(Warns warns);

	public abstract void deleteWarns(Warns warns);

	public abstract void load();

}
