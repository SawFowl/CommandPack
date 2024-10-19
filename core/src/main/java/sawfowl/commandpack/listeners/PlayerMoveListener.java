package sawfowl.commandpack.listeners;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.MovementTypes;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;

public class PlayerMoveListener {

	private final CommandPackInstance plugin;
	public PlayerMoveListener(CommandPackInstance plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void onMove(MoveEntityEvent event, @First ServerPlayer player) {
		if(event.originalPosition().distanceSquared(event.destinationPosition()) == 0d) return;
		if(plugin.getMainConfig().getAfkConfig().isEnable()) plugin.getPlayersData().getTempData().updateLastActivity(player);
		event.context().get(EventContextKeys.MOVEMENT_TYPE).ifPresent(type -> {
			if(type == MovementTypes.ENTITY_TELEPORT || type == MovementTypes.COMMAND) plugin.getPlayersData().getTempData().setPreviousLocation(player);
		});
		if(!plugin.getPlayersData().getTempData().isTrackingPlayer(player)) return;
		plugin.getPlayersData().getTempData().getTrackingPlayerCommands(player).ifPresent(map -> {
			map.forEach((commandName, config) -> {
				if(!config.getDelay().getCancelRules().isAllowMoving() && !player.hasPermission(Permissions.getIgnoreDelayMoving(commandName))) {
					plugin.getPlayersData().getTempData().removeCommandTracking(commandName, player);
					player.sendMessage(plugin.getLocales().getLocale(player).getOther().getExecuteCommand().getMoving("/" + commandName));
				}
			});
		});
	}

}
