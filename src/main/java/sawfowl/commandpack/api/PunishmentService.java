package sawfowl.commandpack.api;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.api.service.ban.Ban.Profile;
import org.spongepowered.api.service.ban.BanService;

import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warn;
import sawfowl.commandpack.api.data.punishment.Warns;

public interface PunishmentService extends BanService {

	void saveBans(Profile profile, IP ip);

	Collection<Ban> getAllBans();

	Collection<Ban.Profile> getAllProfileBans();

	Collection<IP> getAllIPBans();

	Optional<Mute> getMute(UUID uuid);

	Optional<Mute> getMute(ServerPlayer player);

	Collection<Mute> getAllMutes();

	public void addMute(Mute mute);

	public boolean removeMute(Mute mute);

	Optional<Warns> getWarns(UUID uuid);

	Optional<Warns> getWarns(ServerPlayer player);

	Collection<Warns> getAllWarns();

	void addWarns(Warns warns);

	void addWarn(User user, Warn warns);

	void removeWarn(User user, Warn warn);

	void removeWarn(UUID user, Warn warn);

	boolean removeWarns(UUID player);

}
