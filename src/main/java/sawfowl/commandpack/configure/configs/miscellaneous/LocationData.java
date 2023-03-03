package sawfowl.commandpack.configure.configs.miscellaneous;

import java.util.Optional;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.math.vector.Vector3d;

import sawfowl.commandpack.api.data.miscellaneous.Location;
import sawfowl.commandpack.api.data.miscellaneous.Position;

@ConfigSerializable
public class LocationData implements Location {

	@Setting("World")
	private String world;
	@Setting("Position")
	private PositionData position;
	public LocationData() {}
	public LocationData(ServerLocation location) {
		world = location.world().key().asString();
		position = new PositionData(location.position());
	}

	public LocationData(ServerLocation location, Vector3d rotation) {
		world = location.world().key().asString();
		position = new PositionData(location.position(), rotation);
	}

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public Optional<ServerLocation> getServerLocation() {
		return getWorld().filter(ServerWorld::isLoaded).map(world -> (world.location(getPosition().position())));
	}

	@Override
	public Optional<ServerWorld> getWorld() {
		return Sponge.server().worldManager().world(ResourceKey.resolve(world));
	}

	@Override
	public boolean moveToThis(Entity entity) {
		if(!getServerLocation().isPresent()) return false;
		entity.setLocation(getServerLocation().get());
		getPosition().getRotation().ifPresent(rotation -> {
			entity.setRotation(rotation.rotation());
		});
		return true;
	}

	@Override
	public String toString() {
		return "LocationData [world=" + world + ", position=" + position + "]";
	}

}
