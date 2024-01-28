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

	public SpawnData(ServerLocation serverLocation, Vector3d rotation, boolean moveAfterSpawn, boolean moveAfterRespawn) {
		this.locationData = new LocationData(serverLocation, rotation);
		this.moveAfterSpawn = moveAfterSpawn;
		this.moveAfterRespawn = moveAfterRespawn;
	}

	@Setting("Location")
	private LocationData locationData;
	@Setting("MoveAfterRespawn")
	@Comment("Teleport on player respawn.")
	private boolean moveAfterRespawn = false;
	@Setting("MoveAfterSpawn")
	@Comment("Teleportation of a player at the join to the server.")
	private boolean moveAfterSpawn = false;

	public LocationData getLocationData() {
		return locationData;
	}

	public boolean isMoveAfterRespawn() {
		return moveAfterRespawn;
	}

	public boolean isMoveAfterSpawn() {
		return moveAfterSpawn;
	}

	@Override
	public String toString() {
		return "SpawnData [Location=" + locationData + ", MoveAfterRespawn=" + moveAfterRespawn + ", MoveAfterSpawn="
				+ moveAfterSpawn + "]";
	}

}
