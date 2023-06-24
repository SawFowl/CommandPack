package sawfowl.commandpack.api;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.api.service.ban.BanService;

import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warns;

public interface PunishmentService extends BanService {

	Collection<Ban> getAllBans();

	Collection<Ban.Profile> getAllProfileBans();

	Collection<IP> getAllIPBans();

	Optional<Mute> getMute(UUID uuid);

	Optional<Mute> getMute(ServerPlayer player);

	public void addMute(Mute mute);

	public boolean removeMute(Mute mute);

	Optional<Warns> getWarns(UUID uuid);

	Optional<Warns> getWarns(ServerPlayer player);

	void addWarns(Warns warns);

	boolean removeWarns(Warns warns);

}
