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

/**
 * @author SawFowl
 */
public interface Location extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	static Location of(Locatable locatable) {
		return builder().setLocation(locatable.serverLocation()).build();
	}

	static Location of(ServerLocation location) {
		return builder().setLocation(location).build();
	}

	static Location of(Entity entity) {
		return builder().setLocationAndRotation(entity.serverLocation(), Point.builder().setValue(entity.rotation())).build();
	}

	/**
	 * The identifier of the world to which the location belongs.
	 */
	ResourceKey worldKey();

	/**
	 * Location coordinates.
	 */
	Position getPosition();

	/**
	 * Getting a {@link ServerLocation}
	 */
	Optional<ServerLocation> getServerLocation();

	/**
	 * Getting a {@link ServerWorld}
	 */
	Optional<ServerWorld> getWorld();

	/**
	 * Teleporting an entity to this location.
	 * Returns false if teleportation is unsuccessful or impossible.
	 */
	boolean moveHere(Entity entity);

	interface Builder extends AbstractBuilder<Location>, org.spongepowered.api.util.Builder<Location, Builder> {

		Builder setWorld(ServerWorld world);

		Builder setLocation(ServerLocation location);

		Builder setLocationAndRotation(ServerLocation location, Point point);

		Builder setPosition(Position position);

	}

}
