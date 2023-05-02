package sawfowl.commandpack.listeners;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

import sawfowl.commandpack.CommandPack;

public class PlayerInteractEntityListener {

	private final CommandPack plugin;
	public PlayerInteractEntityListener(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void onInteract(InteractEntityEvent event, @First ServerPlayer player) {
		plugin.getPlayersData().getTempData().updateLastActivity(player);
	}

}
