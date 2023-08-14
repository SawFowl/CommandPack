package sawfowl.commandpack.listeners;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;

public class PlayerInteractBlockListener {

	private final CommandPack plugin;
	public PlayerInteractBlockListener(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void onInteract(InteractBlockEvent event, @First ServerPlayer player) {
		if(plugin.getMainConfig().getAfkConfig().isEnable()) plugin.getPlayersData().getTempData().updateLastActivity(player);
		player.sendMessage(Component.text(plugin.getEconomy().getCurrencies().size()));
		plugin.getEconomy().getCurrencies().forEach(currency -> {
			player.sendMessage(currency.displayName().append(Component.text(plugin.getEconomy().getPlayerBalance(player.uniqueId(), currency).doubleValue())));
		});
	}

}
