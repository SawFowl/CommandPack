package sawfowl.commandpack.listeners;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.configure.configs.player.PlayerData;

public class PlayerConnectionListener {

	private final CommandPack plugin;
	public PlayerConnectionListener(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void onConnect(ServerSideConnectionEvent.Join event) {
		if(!plugin.getPlayersData().getPlayerData(event.player().uniqueId()).isPresent()) plugin.getPlayersData().addPlayerData(new PlayerData(event.player()).save());
		if(plugin.getMainConfig().getSpawnData().isPresent() && plugin.getMainConfig().getSpawnData().get().isMoveAfterSpawn() && plugin.getMainConfig().getSpawnData().get().getLocationData().getServerLocation().isPresent()) {
			event.player().setLocation(plugin.getMainConfig().getSpawnData().get().getLocationData().getServerLocation().get());
			plugin.getMainConfig().getSpawnData().get().getLocationData().getPosition().getRotation().ifPresent(rotation -> {
				event.player().setRotation(rotation.asVector3d());
			});
		}
	}

}
