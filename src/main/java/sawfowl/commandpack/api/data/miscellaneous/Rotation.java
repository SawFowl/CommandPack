package sawfowl.commandpack.api.data.miscellaneous;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.math.vector.Vector3d;

@ConfigSerializable
public interface Rotation {

	Vector3d rotation();

}
