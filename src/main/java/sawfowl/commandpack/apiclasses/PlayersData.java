package sawfowl.commandpack.apiclasses;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.iPlayersData;

public class PlayersData implements iPlayersData {

	final CommandPack plugin;
	public PlayersData(CommandPack plugin) {
		this.plugin = plugin;
	}

}
