package sawfowl.commandpack.api;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.registry.RegistryReference;
import org.spongepowered.api.world.biome.Biome;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.builder.AbstractBuilder;


public interface RandomTeleportService {

	/**
	 * Search for a teleportation position in the target world.
	 * 
	 * @param currentLocation - The current location of the entity.
	 * @param options - Options for finding a random position.
	 * @return {@link ServerLocation}
	 */
	public Optional<ServerLocation> getLocation(ServerLocation currentLocation, RandomTeleportOptions options);

	/**
	 * Search for a teleportation position in the target world.<br>
	 * Async mode. Teleportation is only possible in the main thread.
	 * 
	 * @param currentLocation - The current location of the entity.
	 * @param options - Options for finding a random position.
	 * @return {@link ServerLocation}
	 */
	public CompletableFuture<Optional<ServerLocation>> getLocationFuture(ServerLocation currentLocation, RandomTeleportOptions options);

	/**
	 * Getting options from the plugin configuration for the specified world.<br>
	 * If no world is found, the default options will be selected.<br>
	 * The method always returns a copy from the plugin configuration. Changing the copy will not affect the original values.
	 * 
	 * @return {@link RandomTeleportOptions}
	 */
	public RandomTeleportOptions getOptions(ServerWorld world);

	/**
	 * Getting options from the plugin configuration for the specified world.<br>
	 * If no world is found, the default options will be selected.<br>
	 * The method always returns a copy from the plugin configuration. Changing the copy will not affect the original values.
	 * 
	 * @return {@link RandomTeleportOptions}
	 */
	public RandomTeleportOptions getOptions(ResourceKey worldKey);

	/**
	 * Default teleportation options.<br>
	 * The method always returns a copy from the plugin configuration. Changing the copy will not affect the original values.
	 * 
	 * @return {@link RandomTeleportOptions}
	 */
	public RandomTeleportOptions getDefault();

	/**
	 * Create new random teleport options.
	 */
	static RandomTeleportOptions.Builder createOptions() {
		return RandomTeleportOptions.builder();
	}

	/**
	 * These teleportation options to a random position must be used to find a position.
	 */
	interface RandomTeleportOptions extends DataSerializable {

		static Builder builder() {
			return Sponge.game().builderProvider().provide(Builder.class);
		}

		static Builder of(String world) throws Exception {
			return builder().setWorldID(world);
		}

		/**
		 * Copying a class with options.
		 */
		RandomTeleportOptions copy();

		/**
		 * The ID of the target world. If the world is not found or the ID is null, the player's world will be selected for the position search.
		 */
		ResourceKey getWorldKey();

		/**
		 * The number of attempts to find the position.
		 */
		int getAttempts();

		/**
		 * Specifies the target world for teleportation by the player's current world.<br>
		 * If the player's current world is not listed in this worlds map as a source world, the player will be teleported to the same world he is in.
		 */
		boolean isStartFromWorldSpawn();

		/**
		 * Minimum teleportation distance.
		 */
		int getMinRadius();

		/**
		 * Maximum teleportation distance.
		 */
		int getRadius();

		/**
		 * Maximum height for finding a position.
		 */
		int getMaxY();

		/**
		 * Minimum height for finding a position.
		 */
		int getMinY();

		/**
		 * Biomes specified in this list will not be available for teleportation by random coordinates.
		 */
		Set<String> getProhibitedBiomes();

		/**
		 * If true, the player will always move to the surface.
		 */
		boolean isOnlySurface();

		interface Builder extends AbstractBuilder<RandomTeleportOptions>, org.spongepowered.api.util.Builder<RandomTeleportOptions, Builder> {

			Builder setAttempts(int value);

			/**
			 * Specifies the ID of the target world.
			 */
			Builder setWorldKey(ResourceKey value);

			/**
			 * Specifies the ID of the target world.
			 */
			Builder setWorldID(String value) throws Exception;

			/**
			 * Search for a random point around the world spawn.
			 */
			Builder setStartFromWorldSpawn(boolean value);

			/**
			 * Minimum search radius for a random position.
			 */
			Builder setMinRadius(int value);

			/**
			 * Radius of search for a random position.
			 */
			Builder setRadius(int value);

			/**
			 * Maximum height when searching for a position.
			 */
			Builder setMaxY(int value);

			/**
			 * Minimum height when searching for a position.
			 */
			Builder setMinY(int value);

			/**
			 * Biomes that should be excluded from the position search.
			 */
			Builder setProhibitedBiomes(Set<String> value);

			/**
			 * Biomes that should be excluded from the position search.
			 */
			Builder setProhibitedBiomesRegistry(Set<Biome> value);

			/**
			 * Biomes that should be excluded from the position search.
			 */
			Builder setProhibitedBiomesRegistryReference(Set<RegistryReference<Biome>> value);

			/**
			 * If true, teleportation will always take place on the surface.
			 */
			Builder setOnlySurface(boolean value);

		}

	}

}
