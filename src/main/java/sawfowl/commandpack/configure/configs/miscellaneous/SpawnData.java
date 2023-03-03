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

	public SpawnData(ServerLocation serverLocation, Vector3d rotation, boolean forceSpawn, boolean forceRespawn) {
		this.locationData = new LocationData(serverLocation, rotation);
		this.forceSpawn = forceSpawn;
		this.forceRespawn = forceRespawn;
	}

	@Setting("Location")
	private LocationData locationData;
	@Setting("ForceRespawn")
	@Comment("Teleport on player respawn.")
	private boolean forceRespawn = false;
	@Setting("ForceSpawn")
	@Comment("Teleportation of a player at the join to the server.")
	private boolean forceSpawn = false;

	public LocationData getLocationData() {
		return locationData;
	}

	public boolean isForceRespawn() {
		return forceRespawn;
	}

	public boolean isForceSpawn() {
		return forceSpawn;
	}

	@Override
	public String toString() {
		return "SpawnData [locationData=" + locationData + ", forceRespawn=" + forceRespawn + ", forceSpawn="
				+ forceSpawn + "]";
	}

}
