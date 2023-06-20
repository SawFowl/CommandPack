package sawfowl.commandpack.api;

import java.util.Optional;
import java.util.Set;

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
	 * Registration of the custom chunk generator.<br>
	 * All registered generators will be available in the command `/world create`.
	 */
	void registerCustomGenerator(String name, ChunkGenerator chunkGenerator);

	/**
	 * Getting a custom chunk generator.
	 */
	Optional<ChunkGenerator> getCustomGenerator(String name);

	/**
	 * Get a {@link Set} of names of all registered custom chunk generators.
	 */
	Set<String> getAvailableGenerators();

	/**
	 * A system for punishing players.
	 */
	Optional<PunishmentService> getPunishmentService();

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
