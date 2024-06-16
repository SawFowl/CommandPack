package sawfowl.commandpack.listeners;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;

import sawfowl.commandpack.CommandPackInstance;

public class PlayerInteractItemListener {

	private final CommandPackInstance plugin;
	public PlayerInteractItemListener(CommandPackInstance plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void onInteract(InteractItemEvent event, @First ServerPlayer player) {
		if(plugin.getMainConfig().getAfkConfig().isEnable()) plugin.getPlayersData().getTempData().updateLastActivity(player);
	}

}
