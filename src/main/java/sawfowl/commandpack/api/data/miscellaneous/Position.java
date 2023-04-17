package sawfowl.commandpack.api.data.miscellaneous;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.math.vector.Vector3d;
import net.kyori.adventure.builder.AbstractBuilder;

@ConfigSerializable
public interface Position extends Point, DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	static Position of(Vector3d location) {
		return builder().setPosition(location).build();
	}

	static Position of(Vector3d location, Vector3d rotation) {
		return builder().setPosition(rotation).setRotation(rotation).build();
	}

	Optional<Point> getRotation();

	interface Builder extends AbstractBuilder<Position>, org.spongepowered.api.util.Builder<Position, Builder> {

		Builder setPosition(Vector3d position);

		Builder setRotation(Vector3d rotation);

	}

}
