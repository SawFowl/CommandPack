package sawfowl.commandpack.api;

import org.spongepowered.api.event.Event;

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

	public double getAverageTPS1m();

	public double getAverageTPS5m();

	public double getAverageTPS10m();

	interface PostAPI extends Event {

		public CommandPack getAPI();

	}

}
