package sawfowl.commandpack.configure.configs.miscellaneous;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.math.vector.Vector3d;

import sawfowl.commandpack.api.data.miscellaneous.Location;
import sawfowl.commandpack.api.data.miscellaneous.Position;
import sawfowl.commandpack.api.data.miscellaneous.Point;

@ConfigSerializable
public class LocationData implements Location {

	@Setting("World")
	private String world;
	@Setting("Position")
	private PositionData position;

	public LocationData() {}

	public LocationData(Location location) {
		world = location.worldKey().asString();
		position = location.getPosition() instanceof PositionData ? (PositionData) location.getPosition() : (location.getPosition().getRotation().isPresent() ? new PositionData(location.getPosition().asVector3d(), location.getPosition().getRotation().get().asVector3d()) : new PositionData(location.getPosition().asVector3d()));
	}

	public LocationData(ResourceKey world, PositionData positionData) {
		this.world = world.asString();
		position = positionData;
	}

	public LocationData(ResourceKey world, Position position) {
		this.world = world.asString();
		this.position = (PositionData) (position instanceof PositionData ? position : (position.getRotation().isPresent() ? new PositionData(position.asVector3d(), position.getRotation().get().asVector3d()) : new PositionData(position.asVector3d())));
	}

	public LocationData(ServerLocation location) {
		world = location.world().key().asString();
		position = new PositionData(location.position());
	}

	public LocationData(ServerLocation location, Vector3d rotation) {
		world = location.world().key().asString();
		position = new PositionData(location.position(), rotation);
	}

	public Builder builder() {
		return new Builder();
	}

	@Override
	public ResourceKey worldKey() {
		return ResourceKey.resolve(world);
	}

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public Optional<ServerLocation> getServerLocation() {
		return world == null ? Optional.empty() : getWorld().filter(ServerWorld::isLoaded).map(world -> (world.location(getPosition().asVector3d())));
	}

	@Override
	public Optional<ServerWorld> getWorld() {
		return Sponge.server().worldManager().world(ResourceKey.resolve(world));
	}

	@Override
	public boolean moveHere(Entity entity) {
		if(!getServerLocation().isPresent()) return false;
		entity.setLocation(getServerLocation().get());
		getPosition().getRotation().ifPresent(rotation -> {
			entity.setRotation(rotation.asVector3d());
		});
		return true;
	}

	@Override
	public String toString() {
		return "LocationData [world=" + world + ", position=" + position + "]";
	}

	@Override
	public int contentVersion() {
		return 1;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(DataQuery.of("World"), world)
				.set(DataQuery.of("Position"), position)
				.set(Queries.CONTENT_VERSION, contentVersion());
	}

	public class Builder implements Location.Builder {

		@Override
		public Builder setWorld(ServerWorld world) {
			LocationData.this.world = world.key().asString();
			return this;
		}

		@Override
		public Builder setLocation(ServerLocation location) {
			LocationData.this.world = location.world().key().asString();
			LocationData.this.position = new PositionData(location.position());
			return this;
		}

		@Override
		public Builder setLocationAndRotation(ServerLocation location, Point point) {
			LocationData.this.world = location.world().key().asString();
			LocationData.this.position = new PositionData(location.position(), new PointData(point.asVector3d()));
			return this;
		}

		@Override
		public Builder setPosition(Position position) {
			LocationData.this.position = new PositionData(position.asVector3d(), position.getRotation().map(r -> (new PointData(r.asVector3d()))).orElse(null));
			return this;
		}

		@Override
		public @NotNull Location build() {
			return LocationData.this;
		}

	}

}
