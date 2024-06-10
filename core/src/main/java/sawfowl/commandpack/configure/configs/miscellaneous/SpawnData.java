package sawfowl.commandpack.configure.configs.miscellaneous;

import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.math.vector.Vector3d;

@ConfigSerializable
public class SpawnData {

	public SpawnData() {}
	public SpawnData(LocationData locationData) {
		this.locationData = locationData;
	}

	public SpawnData(ServerLocation serverLocation) {
		this.locationData = new LocationData(serverLocation);
	}

	public SpawnData(ServerLocation serverLocation, Vector3d rotation, boolean moveAfterJoin, boolean moveAfterRespawn) {
		this.locationData = new LocationData(serverLocation, rotation);
		this.moveAfterJoin = moveAfterJoin;
		this.moveAfterRespawn = moveAfterRespawn;
	}

	@Setting("Location")
	private LocationData locationData;
	@Setting("MoveAfterRespawn")
	@Comment("Teleport on player respawn.")
	private boolean moveAfterRespawn = false;
	@Setting("MoveAfterJoin")
	@Comment("Teleportation of a player at the join to the server.")
	private boolean moveAfterJoin = false;

	public LocationData getLocationData() {
		return locationData;
	}

	public boolean isMoveAfterRespawn() {
		return moveAfterRespawn;
	}

	public boolean isMoveAfterJoin() {
		return moveAfterJoin;
	}

	@Override
	public String toString() {
		return "SpawnData [Location=" + locationData + ", MoveAfterRespawn=" + moveAfterRespawn + ", MoveAfterJoin="
				+ moveAfterJoin + "]";
	}

}
