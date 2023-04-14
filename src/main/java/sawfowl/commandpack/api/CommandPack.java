package sawfowl.commandpack.api;

import org.spongepowered.api.event.Event;

public interface CommandPack {

	PlayersData playersData();

	TempPlayerData tempPlayerData();

	RandomTeleportService randomTeleportService();

	interface PostAPI extends Event {

		public CommandPack getAPI();

	}

}
