package sawfowl.commandpack.listeners;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.MovementTypes;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

import sawfowl.commandpack.CommandPackPlugin;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class PlayerMoveListener {

	private final CommandPackPlugin plugin;
	public PlayerMoveListener(CommandPackPlugin plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void onMove(MoveEntityEvent event, @First ServerPlayer player) {
		if(event.originalPosition().distanceSquared(event.destinationPosition()) == 0d) return;
		event.context().get(EventContextKeys.MOVEMENT_TYPE).ifPresent(type -> {
			if(type == MovementTypes.ENTITY_TELEPORT || type == MovementTypes.COMMAND) plugin.getPlayersData().getTempData().setPreviousLocation(player);
		});
		if(!plugin.getPlayersData().getTempData().isTrackingPlayer(player)) return;
		plugin.getPlayersData().getTempData().getTrackingPlayerCommands(player).ifPresent(map -> {
			map.forEach((commandName, config) -> {
				if(!config.getDelay().getCancelRules().isAllowMoving() && !player.hasPermission(Permissions.getIgnoreDelayMoving(commandName))) {
					plugin.getPlayersData().getTempData().removeCommandTracking(commandName, player);
					player.sendMessage(TextUtils.replace(plugin.getLocales().getText(player.locale(), LocalesPaths.COMMANDS_STOP_TRACKING_MOVING), Placeholders.COMMAND, "/" + commandName));
				}
			});
		});
	}

}
