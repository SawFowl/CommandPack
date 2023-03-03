package sawfowl.commandpack.configure.configs.miscellaneous;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.math.vector.Vector3d;

import sawfowl.commandpack.api.data.miscellaneous.Rotation;

@ConfigSerializable
public class RotationData implements Rotation {

	public RotationData() {}

	@Setting("X")
	private double x;
	@Setting("Y")
	private double y;
	@Setting("Z")
	private double z;
	public RotationData(Vector3d rotation) {
		x = rotation.x();
		y = rotation.y();
		z = rotation.z();
	}

	@Override
	public Vector3d rotation() {
		return Vector3d.from(x, y, z);
	}

	@Override
	public String toString() {
		return "RotationData [x=" + x + ", y=" + y + ", z=" + z + "]";
	}

}
