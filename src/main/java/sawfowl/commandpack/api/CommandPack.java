package sawfowl.commandpack.api;

import org.spongepowered.api.event.Event;
import org.spongepowered.api.world.generation.ChunkGenerator;

/**
 * Plugin API.
 * 
 * @author SawFowl
 */
public interface CommandPack {

	/**
	 * Viewing and changing player data.
	 */
	PlayersData playersData();

	/**
	 * Interface for working with teleportation to random coordinates.
	 */
	RandomTeleportService randomTeleportService();

	/**
	 * Whether the plugin is running on the server with Forge.
	 */
	boolean isForgeServer();

	/**
	 * Kits API.
	 */
	KitService kitService();

	/**
	 * 	Empty world generator
	 */
	ChunkGenerator getEmptyWorldGenerator();

	public double getAverageTPS1m();

	public double getAverageTPS5m();

	public double getAverageTPS10m();

	/**
	 * Event for getting the plugin API.
	 */
	interface PostAPI extends Event {

		public CommandPack getAPI();

	}

}
