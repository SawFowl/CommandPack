package sawfowl.commandpack.listeners;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;

import sawfowl.commandpack.CommandPack;

public class PlayerInteractItemListener {

	private final CommandPack plugin;
	public PlayerInteractItemListener(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void onInteract(InteractItemEvent event, @First ServerPlayer player) {
		plugin.getPlayersData().getTempData().updateLastActivity(player);
	}

}
