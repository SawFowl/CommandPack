package sawfowl.commandpack.listeners;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.ExecuteCommandEvent;
import org.spongepowered.api.event.filter.cause.First;

import sawfowl.commandpack.CommandPackPlugin;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class PlayerCommandListener {

	private final CommandPackPlugin plugin;
	public PlayerCommandListener(CommandPackPlugin plugin) {
		this.plugin = plugin;
	}

	@Listener(order = Order.LAST)
	public void onExecute(ExecuteCommandEvent.Pre event, @First ServerPlayer player) {
		if(!plugin.getPlayersData().getTempData().isTrackingPlayer(player)) return;
		plugin.getPlayersData().getTempData().getTrackingPlayerCommands(player).ifPresent(map -> {
			map.forEach((commandName, config) -> {
				if(!config.getDelay().getCancelRules().isAllowOtherCommand()) {
					plugin.getPlayersData().getTempData().removeCommandTracking(commandName, player);
					player.sendMessage(TextUtils.replace(plugin.getLocales().getText(player.locale(), LocalesPaths.COMMANDS_STOP_TRACKING_COMMAND), Placeholders.COMMAND, "/" + commandName));
				}
			});
		});
	}
}
