package sawfowl.commandpack.listeners;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

import sawfowl.commandpack.CommandPackInstance;

public class PlayerInteractEntityListener {

	private final CommandPackInstance plugin;
	public PlayerInteractEntityListener(CommandPackInstance plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void onInteract(InteractEntityEvent event, @First ServerPlayer player) {
		if(plugin.getMainConfig().getAfkConfig().isEnable()) plugin.getPlayersData().getTempData().updateLastActivity(player);
	}

}
