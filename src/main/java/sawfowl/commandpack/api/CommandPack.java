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

	interface PostAPI extends Event {

		public CommandPack getAPI();

	}

}
