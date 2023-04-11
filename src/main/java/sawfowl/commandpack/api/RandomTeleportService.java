package sawfowl.commandpack.api;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;


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
	 * Async mode.
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

	interface PostEvent extends Event {

		RandomTeleportService getService();

	}

	/**
	 * These teleportation options to a random position must be used to find a position.
	 */
	interface RandomTeleportOptions {

		/**
		 * Copying a class with options.
		 */
		RandomTeleportOptions copy();

		/**
		 * The ID of the target world. If the world is not found or the ID is null, the player's world will be selected for the position search.
		 */
		ResourceKey getWorldKey();

		/**
		 * Specifies a new ID for the target world.
		 */
		void setWorldKey(ResourceKey worldKey);

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

	}

}
