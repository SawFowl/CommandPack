package sawfowl.commandpack.api;

import java.util.Collection;

import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.api.service.ban.BanService;

public interface PunishmentService extends BanService {

	Collection<Ban> getAllBans();

	Collection<Ban.Profile> getAllProfileBans();

	Collection<IP> getAllIPBans();

}
