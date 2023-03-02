package sawfowl.commandpack.api.data.miscellaneous;

import java.util.Optional;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.math.vector.Vector3d;
import org.spongepowered.math.vector.Vector3i;

import sawfowl.commandpack.configure.configs.miscellaneous.PositionData;

@ConfigSerializable
public interface Position {

	static Position create(Vector3d location) {
		return new PositionData(location);
	}

	static Position create(Vector3d location, Vector3d rotation) {
		return new PositionData(location, rotation);
	}

	Vector3d position();

	Vector3i toInt();

	Optional<Rotation> getRotation();

}
