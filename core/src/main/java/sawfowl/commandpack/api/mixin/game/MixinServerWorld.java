package sawfowl.commandpack.api.mixin.game;

import java.util.Optional;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.DefaultWorldKeys;
import org.spongepowered.api.world.server.ServerWorld;

/**
 * This interface adds additional functionality to the world class.
 */
public interface MixinServerWorld extends ServerWorld {

	static MixinServerWorld cast(ServerWorld world) {
		return (MixinServerWorld) world;
	}

	static Optional<MixinServerWorld> findWorld(ResourceKey key) {
		return Sponge.server().worldManager().world(key).map(world -> MixinServerWorld.cast(world));
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

	enum Defaults {

		OVERWORLD {
			@Override
			public Optional<MixinServerWorld> get() {
				return Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT).map(world -> MixinServerWorld.cast(world));
			}
		},
		NETHER {
			@Override
			public Optional<MixinServerWorld> get() {
				return Sponge.server().worldManager().world(DefaultWorldKeys.THE_NETHER).map(world -> MixinServerWorld.cast(world));
			}
		},
		END {
			@Override
			public Optional<MixinServerWorld> get() {
				return Sponge.server().worldManager().world(DefaultWorldKeys.THE_END).map(world -> MixinServerWorld.cast(world));
			}
		};

		public abstract Optional<MixinServerWorld> get();

	}

}
