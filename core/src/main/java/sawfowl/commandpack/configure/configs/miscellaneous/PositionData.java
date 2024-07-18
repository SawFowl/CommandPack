package sawfowl.commandpack.configure.configs.miscellaneous;

import java.util.Optional;

import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.math.vector.Vector3d;
import org.spongepowered.math.vector.Vector3i;

import sawfowl.commandpack.api.data.miscellaneous.Position;
import sawfowl.commandpack.api.data.miscellaneous.Point;

@ConfigSerializable
public class PositionData implements Position {

	@Setting("X")
	private double x;
	@Setting("Y")
	private double y;
	@Setting("Z")
	private double z;
	@Setting("Point")
	private PointData rotation;
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
		this.rotation = new PointData(rotation);
	}

	public PositionData(Vector3d position, PointData rotation) {
		x = position.x();
		y = position.y();
		z = position.z();
		this.rotation = rotation;
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
	public Optional<Point> getRotation() {
		return Optional.ofNullable(rotation);
	}

	@Override
	public String toString() {
		return "PositionData [x=" + x + ", y=" + y + ", z=" + z + ", rotation=" + rotation + "]";
	}
	@Override
	public int contentVersion() {
		return 1;
	}
	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(DataQuery.of("X"), x)
				.set(DataQuery.of("Y"), y)
				.set(DataQuery.of("Z"), z)
				.set(DataQuery.of("Point"), rotation)
				.set(Queries.CONTENT_VERSION, contentVersion());
	}

	public class Builder implements Position.Builder {

		@Override
		public Builder setPosition(Vector3d position) {
			x = position.x();
			y = position.y();
			z = position.z();
			return this;
		}

		@Override
		public Builder setRotation(Vector3d rotation) {
			PositionData.this.rotation = new PointData(rotation);
			return this;
		}

		@Override
		public Position build() {
			return PositionData.this;
		}
		
	}

}
