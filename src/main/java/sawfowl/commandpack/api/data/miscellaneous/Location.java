package sawfowl.commandpack.api.data.miscellaneous;

import java.util.Optional;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.Locatable;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;

import sawfowl.commandpack.configure.configs.miscellaneous.LocationData;

public interface Location {

	static Location create(Locatable locatable) {
		return create(locatable.serverLocation());
	}

	static Location create(ServerLocation location) {
		return new LocationData(location);
	}

	static Location create(Entity entity) {
		return new LocationData(entity.serverLocation(), entity.rotation());
	}

	Position getPosition();

	Optional<ServerLocation> getServerLocation();

	Optional<ServerWorld> getWorld();

	boolean moveToThis(Entity entity);

}
