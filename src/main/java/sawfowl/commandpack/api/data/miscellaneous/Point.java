package sawfowl.commandpack.api.data.miscellaneous;

import org.spongepowered.api.Sponge;
import org.spongepowered.math.vector.Vector3d;
import org.spongepowered.math.vector.Vector3i;

import net.kyori.adventure.builder.AbstractBuilder;

/**
 * @author SawFowl
 */
public interface Point {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	Vector3d asVector3d();

	Vector3i toInt();

	interface Builder extends AbstractBuilder<Point>, org.spongepowered.api.util.Builder<Point, Builder> {

		Point setValue(Vector3d vector3d);

	}

}
