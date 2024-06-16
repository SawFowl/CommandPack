package sawfowl.commandpack.listeners;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;

import sawfowl.commandpack.CommandPackInstance;

public class PlayerInteractBlockListener {

	private final CommandPackInstance plugin;
	public PlayerInteractBlockListener(CommandPackInstance plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void onInteract(InteractBlockEvent event, @First ServerPlayer player) {
		if(plugin.getMainConfig().getAfkConfig().isEnable()) plugin.getPlayersData().getTempData().updateLastActivity(player);
	}

}
