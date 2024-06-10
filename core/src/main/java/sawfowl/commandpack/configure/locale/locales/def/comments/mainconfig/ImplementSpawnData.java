package sawfowl.commandpack.configure.locale.locales.def.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.SpawnData;

@ConfigSerializable
public class ImplementSpawnData implements SpawnData {

	public ImplementSpawnData() {}

	@Setting("MoveAfterRespawn")
	private String moveAfterRespawn = "Teleport when the player respawns.";
	@Setting("MoveAfterJoin")
	private String moveAfterJoin = "Teleportation of a player at the join to the server.";

	@Override
	public String getMoveAfterRespawn() {
		return moveAfterRespawn;
	}

	@Override
	public String getMoveAfterJoin() {
		return moveAfterJoin;
	}

}
