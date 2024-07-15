package sawfowl.commandpack.api.storages;

import java.net.InetAddress;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.api.service.ban.Ban.Profile;

import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warns;

public interface PunishmentStorage {

	boolean isSupportCombined();

	void saveBans(Profile profile, IP ip);

	Map<UUID, Profile> getBans();

	Map<InetAddress, IP> getBansIP();

	Optional<Ban.Profile> getBan(UUID uuid);

	Optional<Ban.IP> getBanIP(InetAddress address);

	void load();

	void saveBan(Profile ban);

	boolean deleteBan(GameProfile profile);

	void saveBan(IP ban);

	boolean deleteIPBan(InetAddress address);

	Optional<Mute> getMute(UUID uuid);

	void saveMute(Mute mute);

	boolean deleteMute(Mute mute);

	Map<UUID, Mute> getMutes();

	void saveWarns(Warns warns);

	boolean deleteWarns(UUID player);

	Map<UUID, Warns> getWarns();

	Optional<Warns> getWarns(UUID uuid);

}
