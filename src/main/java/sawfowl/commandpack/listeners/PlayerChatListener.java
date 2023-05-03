package sawfowl.commandpack.listeners;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.PlayerChatEvent;

import sawfowl.commandpack.CommandPack;

public class PlayerChatListener {

	private final CommandPack plugin;
	public PlayerChatListener(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Listener(order = Order.LAST)
	public void onExecute(PlayerChatEvent event, @First ServerPlayer player) {
		plugin.getPlayersData().getTempData().updateLastActivity(player);
	}
}
