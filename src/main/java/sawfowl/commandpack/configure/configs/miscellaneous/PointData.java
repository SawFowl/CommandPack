package sawfowl.commandpack.configure.configs.miscellaneous;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.math.vector.Vector3d;
import org.spongepowered.math.vector.Vector3i;

import sawfowl.commandpack.api.data.miscellaneous.Point;

@ConfigSerializable
public class PointData implements Point {

	public PointData() {}

	@Setting("X")
	private double x;
	@Setting("Y")
	private double y;
	@Setting("Z")
	private double z;
	public PointData(Vector3d vector3d) {
		x = vector3d.x();
		y = vector3d.y();
		z = vector3d.z();
	}

	public Builder builder() {
		return new Builder();
	}

	@Override
	public Vector3d asVector3d() {
		return Vector3d.from(x, y, z);
	}

	@Override
	public Vector3i toInt() {
		return Vector3i.from((int) x, (int) y, (int) z);
	}

	@Override
	public String toString() {
		return "PointData [x=" + x + ", y=" + y + ", z=" + z + "]";
	}

	public class Builder implements Point.Builder {

		@Override
		public Point setValue(Vector3d vector3d) {
			x = vector3d.x();
			y = vector3d.y();
			z = vector3d.z();
			return PointData.this;
		}

		@Override
		public @NotNull Point build() {
			return PointData.this;
		}

	}

}
