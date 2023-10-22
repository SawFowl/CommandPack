package sawfowl.commandpack.apiclasses.punishment.storage;

import java.net.InetAddress;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.api.service.ban.Ban.Profile;
import org.spongepowered.configurate.ConfigurationOptions;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warns;
import sawfowl.commandpack.utils.StorageType;
import sawfowl.localeapi.api.serializetools.SerializeOptions;

public abstract class AbstractPunishmentStorage extends Thread {

	final CommandPack plugin;
	Map<UUID, Ban.Profile> bans = new HashMap<>();
	Map<InetAddress, Ban.IP> bansIP = new HashMap<>();
	Map<UUID, Mute> mutes = new HashMap<>();
	Map<UUID, Warns> warns = new HashMap<>();
	ConfigurationOptions options;
	public AbstractPunishmentStorage(CommandPack plugin) {
		this.plugin = plugin;
		options = SerializeOptions.selectOptions(plugin.getMainConfig().getItemSerializer());
		load();
		Sponge.asyncScheduler().submit(Task.builder().interval(Duration.ofMinutes(1)).plugin(plugin.getPluginContainer()).execute(() -> {
			Map<UUID, Ban.Profile> bans = new HashMap<>(this.bans);
			bans.values().forEach(ban -> {
				if(ban.expirationDate().isPresent() && ban.expirationDate().get().toEpochMilli() <= System.currentTimeMillis()) deleteBan(ban.profile());
			});
			Map<InetAddress, Ban.IP> bansIP = new HashMap<>(this.bansIP);
			bansIP.values().forEach(ban -> {
				if(ban.expirationDate().isPresent() && ban.expirationDate().get().toEpochMilli() <= System.currentTimeMillis()) deleteIPBan(ban.address());
			});
			Map<UUID, Mute> mutes = new HashMap<>(this.mutes);
			mutes.values().forEach(mute -> {
				if(mute.isExpired()) deleteMute(mute);
			});
			Map<UUID, Warns> warns = new HashMap<>(this.warns);
			warns.values().forEach(w -> {
				w.checkExpired();
			});
			bans = null;
			bansIP = null;
			mutes = null;
			warns = null;
		}).build());
	}

	public abstract void load();

	public abstract void saveBan(Profile ban);

	public abstract boolean deleteBan(GameProfile profile);

	public abstract void saveBan(IP ban);

	public abstract boolean deleteIPBan(InetAddress address);

	public abstract void saveMute(Mute mute);

	public abstract boolean deleteMute(Mute mute);

	public abstract void saveWarns(Warns warns);

	public abstract boolean deleteWarns(UUID player);

	public abstract StorageType getStorageType();

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
		Optional<Mute> mute = Optional.ofNullable(mutes.getOrDefault(uuid, null));
		if(mute.isPresent() && mute.get().isExpired()) {
			deleteMute(mute.get());
			return Optional.empty();
		}
		return mute;
	}

	public Optional<Warns> getWarns(UUID uuid) {
		Optional<Warns> optWarns = Optional.ofNullable(this.warns.getOrDefault(uuid, null));
		if(optWarns.isPresent()) optWarns.get().checkExpired();
		return optWarns;
	}

}
