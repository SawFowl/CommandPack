package sawfowl.commandpack.listeners;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.configure.LocalesPaths;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.localeapi.api.TextUtils;

public class PlayerMoveListener {

	private final CommandPack plugin;
	public PlayerMoveListener(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void onMove(MoveEntityEvent event, @First ServerPlayer player) {
		if(event.originalPosition().distanceSquared(event.destinationPosition()) == 0d || !plugin.getTempPlayerData().isTrackingPlayer(player)) return;
		plugin.getTempPlayerData().getTrackingPlayerCommands(player).ifPresent(map -> {
			map.forEach((commandName, config) -> {
				if(!config.getDelay().getCancelRules().isAllowMoving() && !player.hasPermission(Permissions.getIgnoreDelayMoving(commandName))) {
					plugin.getTempPlayerData().removeCommandTracking(commandName, player);
					player.sendMessage(TextUtils.replace(plugin.getLocales().getText(player.locale(), LocalesPaths.COMMANDS_STOP_TRACKING_MOVING), Placeholders.COMMAND, "/" + commandName));
				}
			});
		});
	}

}
