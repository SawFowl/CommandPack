package sawfowl.commandpack.api;

import java.util.Optional;

import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;

import sawfowl.commandpack.api.data.miscellaneous.RandomTeleportOptions;

public interface RandomTeleportService {

	public Optional<ServerLocation> getLocation(ServerWorld source, ServerWorld target, RandomTeleportOptions options);

}
