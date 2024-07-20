package sawfowl.commandpack.listeners;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.ExecuteCommandEvent;
import org.spongepowered.api.event.filter.cause.First;

import sawfowl.commandpack.CommandPackInstance;

public class PlayerCommandListener {

	private final CommandPackInstance plugin;
	public PlayerCommandListener(CommandPackInstance plugin) {
		this.plugin = plugin;
	}

	@Listener(order = Order.LAST)
	public void onExecute(ExecuteCommandEvent.Post event, @First ServerPlayer player) {
		if(Sponge.server().onlinePlayers().size() > 40) {
			Sponge.asyncScheduler().executor(plugin.getPluginContainer()).execute(() -> {
				spyCommand(event, player, true);
			});
		} else spyCommand(event, player, false);
		if(plugin.getPlayersData().getTempData().isAfk(player) && event.command().equalsIgnoreCase("afk")) return;
		if(plugin.getMainConfig().getAfkConfig().isEnable()) plugin.getPlayersData().getTempData().updateLastActivity(player);
		if(!plugin.getPlayersData().getTempData().isTrackingPlayer(player)) return;
		plugin.getPlayersData().getTempData().getTrackingPlayerCommands(player).ifPresent(map -> {
			map.forEach((commandName, config) -> {
				if(!config.getDelay().getCancelRules().isAllowOtherCommand()) {
					plugin.getPlayersData().getTempData().removeCommandTracking(commandName, player);
					player.sendMessage(plugin.getLocales().getLocale(player.locale()).getOther().getExecuteCommand().getOtherCommand("/" + commandName));
				}
			});
		});
	}

	private void spyCommand(ExecuteCommandEvent.Post event, ServerPlayer player, boolean parallel) {
		(parallel ? Sponge.server().onlinePlayers().parallelStream() : Sponge.server().onlinePlayers().stream()).filter(p -> !p.uniqueId().equals(player.uniqueId()) && plugin.getPlayersData().getTempData().isSpyCommand(p)).forEach(p -> {
			p.sendMessage(plugin.getLocales().getLocale(p).getCommands().getCommandSpy().getSpy(player, "/" + event.command() + " " + event.arguments()));
		});
	}
}
