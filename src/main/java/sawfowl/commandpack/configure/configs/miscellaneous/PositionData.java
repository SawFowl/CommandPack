package sawfowl.commandpack.configure.configs.miscellaneous;

import java.util.Optional;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.math.vector.Vector3d;
import org.spongepowered.math.vector.Vector3i;

import sawfowl.commandpack.api.data.miscellaneous.Position;
import sawfowl.commandpack.api.data.miscellaneous.Rotation;

@ConfigSerializable
public class PositionData implements Position {

	@Setting("X")
	private double x;
	@Setting("Y")
	private double y;
	@Setting("Z")
	private double z;
	@Setting("Rotation")
	private RotationData rotation;
	public PositionData() {}
	public PositionData(Vector3d position) {
		x = position.x();
		y = position.y();
		z = position.z();
	}

	public PositionData(Vector3d position, Vector3d rotation) {
		x = position.x();
		y = position.y();
		z = position.z();
		this.rotation = new RotationData(rotation);
	}

	@Override
	public Vector3d position() {
		return Vector3d.from(x, y, z);
	}

	@Override
	public Vector3i toInt() {
		return Vector3i.from((int) x, (int) y, (int) z);
	}

	@Override
	public Optional<Rotation> getRotation() {
		return Optional.ofNullable(rotation);
	}

}
