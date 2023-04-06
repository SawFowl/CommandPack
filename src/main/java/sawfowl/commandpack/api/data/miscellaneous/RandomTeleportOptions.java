package sawfowl.commandpack.api.data.miscellaneous;

import java.util.Set;

public interface RandomTeleportOptions {

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
