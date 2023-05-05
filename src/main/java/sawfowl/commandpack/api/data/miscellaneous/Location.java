package sawfowl.commandpack.api.data.miscellaneous;

import java.util.Optional;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.Locatable;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.builder.AbstractBuilder;

import sawfowl.commandpack.configure.configs.miscellaneous.LocationData;

/**
 * @author SawFowl
 */
public interface Location extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	static Location of(Locatable locatable) {
		return of(locatable.serverLocation());
	}

	static Location of(ServerLocation location) {
		return new LocationData(location);
	}

	static Location of(Entity entity) {
		return new LocationData(entity.serverLocation(), entity.rotation());
	}

	ResourceKey worldKey();

	Position getPosition();

	Optional<ServerLocation> getServerLocation();

	Optional<ServerWorld> getWorld();

	boolean moveHere(Entity entity);

	interface Builder extends AbstractBuilder<Location>, org.spongepowered.api.util.Builder<Location, Builder> {

		Builder setWorld(ServerWorld world);

		Builder setLocation(ServerLocation location);

		Builder setLocationAndRotation(ServerLocation location, Point point);

		Builder setPosition(Position position);

	}

}
