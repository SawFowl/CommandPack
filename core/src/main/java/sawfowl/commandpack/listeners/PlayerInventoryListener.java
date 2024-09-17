package sawfowl.commandpack.listeners;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;

import sawfowl.commandpack.CommandPack;

public class PlayerInventoryListener {

	private final CommandPack plugin;
	public PlayerInventoryListener(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void onInteract(ChangeInventoryEvent.SwapHand event, @First ServerPlayer player) {
		if(plugin.getMainConfig().getAfkConfig().isEnable()) plugin.getPlayersData().getTempData().updateLastActivity(player);
	}

	@Listener
	public void onInteract(ChangeInventoryEvent.Held event, @First ServerPlayer player) {
		if(plugin.getMainConfig().getAfkConfig().isEnable()) plugin.getPlayersData().getTempData().updateLastActivity(player);
	}

}
