package sawfowl.commandpack.listeners;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;

import sawfowl.commandpack.CommandPack;

public class PlayerInteractBlockListener {

	private final CommandPack plugin;
	public PlayerInteractBlockListener(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void onInteract(InteractBlockEvent event, @First ServerPlayer player) {
		plugin.getPlayersData().getTempData().updateLastActivity(player);
	}

}
