package sawfowl.commandpack.api.game.server;

import java.util.Optional;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.DefaultWorldKeys;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3i;

import sawfowl.commandpack.api.game.PortalShape;

/**
 * This interface adds additional functionality to the world class.
 */
public interface CPServerWorld extends ServerWorld {

	static CPServerWorld cast(ServerWorld world) {
		return (CPServerWorld) world;
	}

	static Optional<CPServerWorld> findWorld(ResourceKey key) {
		return Sponge.server().worldManager().world(key).map(world -> CPServerWorld.cast(world));
	}

	/**
	 * If true, all processes in the world will be stopped until the freeze is turned off.<br>
	 * Blocks will not drop when destroyed.<br>
	 * Entities will be stopped and will be inactive.<br>
	 * Time and weather changes will be disabled.<br>
	 * Objects lying on the ground will be impossible to pick up.<br>
	 * And so on.
	 */
	boolean isFreezeTicks();

	/**
	 * If true, all processes in the world will be stopped until the freeze is turned off.<br>
	 * Blocks will not drop when destroyed.<br>
	 * Entities will be stopped and will be inactive.<br>
	 * Time and weather changes will be disabled.<br>
	 * Objects lying on the ground will be impossible to pick up.<br>
	 * And so on.<br>
	 * Setting false will disable freezing.
	 */
	void setFreezeTicks(boolean enable);

	/**
	 * Getting the world ticking time.
	 */
	double getTickTime();

	/**
	 * Getting the TPS of the world.
	 */
	double getTPS();

	/**
	 * Finding the shape of the portal in the world.
	 */
	Optional<PortalShape> findPortalShape(boolean empty, int x, int y, int z, Direction direction, @Nullable Predicate<PortalShape> predicate);

	/**
	 * Finding the shape of the portal in the world.
	 */
	default Optional<PortalShape> findPortalShape(boolean empty, Vector3i blockPos, Direction direction, @Nullable Predicate<PortalShape> predicate) {
		return findPortalShape(empty, blockPos.x(), blockPos.y(), blockPos.z(), direction, predicate);
	}

	/**
	 * Search for an empty portal shape in the world.
	 */
	default Optional<PortalShape> findEmptyPortalShape(Vector3i blockPos, Direction direction) {
		return findPortalShape(true, blockPos, direction, null);
	}

	/**
	 * Search for an empty portal shape in the world.
	 */
	default Optional<PortalShape> findEmptyPortalShape(int x, int y, int z, Direction direction) {
		return findPortalShape(true, x, y, z, direction, null);
	}

	/**
	 * Search for an empty portal shape in the world.
	 */
	default Optional<PortalShape> findEmptyPortalShape(Vector3i blockPos, Direction direction, @Nullable Predicate<PortalShape> predicate) {
		return findPortalShape(true, blockPos, direction, predicate);
	}

	/**
	 * Search for an empty portal shape in the world.
	 */
	default Optional<PortalShape> findEmptyPortalShape(int x, int y, int z, Direction direction, @Nullable Predicate<PortalShape> predicate) {
		return findPortalShape(true, x, y, z, direction, predicate);
	}

	/**
	 * Finding the position of the exit portal.
	 * @param blockPos - The starting position of the search.
	 * @param isNether - If true, the search will be performed within a radius of 16 blocks. If false, the search will be performed within a radius of 128 blocks.
	 */
	Optional<Vector3i> findClosestPortalPosition(Vector3i blockPos, boolean isNether);

	enum Defaults {

		OVERWORLD {
			@Override
			public Optional<CPServerWorld> get() {
				return Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).map(world -> CPServerWorld.cast(world));
			}
		},
		NETHER {
			@Override
			public Optional<CPServerWorld> get() {
				return Sponge.server().worldManager().world(DefaultWorldKeys.THE_NETHER).map(world -> CPServerWorld.cast(world));
			}
		},
		END {
			@Override
			public Optional<CPServerWorld> get() {
				return Sponge.server().worldManager().world(DefaultWorldKeys.THE_END).map(world -> CPServerWorld.cast(world));
			}
		};

		public abstract Optional<CPServerWorld> get();

	}

}
