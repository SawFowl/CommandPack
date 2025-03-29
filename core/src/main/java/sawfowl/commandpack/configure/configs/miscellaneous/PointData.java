package sawfowl.commandpack.configure.configs.miscellaneous;

import java.util.Optional;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.math.vector.Vector3d;
import org.spongepowered.math.vector.Vector3i;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

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
	public JsonObject asJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("X", x);
		jsonObject.addProperty("Y", y);
		jsonObject.addProperty("Z", z);
		return jsonObject;
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
		public Point build() {
			return PointData.this;
		}

		@Override
		public Optional<Point> fromJson(JsonObject json) {
			if(json.has("X") && json.has("Y") && json.has("Z")) {
				var xJson = json.get("X");
				var yJson = json.get("Y");
				var zJson = json.get("Z");
				if(xJson instanceof JsonPrimitive x && x.isNumber() && yJson instanceof JsonPrimitive y && y.isNumber() && zJson instanceof JsonPrimitive z && z.isNumber()) {
					PointData.this.x = x.getAsDouble();
					PointData.this.y = y.getAsDouble();
					PointData.this.z = z.getAsDouble();
					xJson = null;
					yJson = null;
					zJson = null;
					return Optional.ofNullable(PointData.this);
				}
				xJson = null;
				yJson = null;
				zJson = null;
			}
			return Optional.empty();
		}

	}

}
