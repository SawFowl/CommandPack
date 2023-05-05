package sawfowl.commandpack.api.events;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;

import sawfowl.commandpack.api.RandomTeleportService.RandomTeleportOptions;

/**
 * The event is triggered when using the command /randomteleport, if the position was found.
 * 
 * @author SawFowl
 */
public interface RandomTeleportEvent extends ChangeEntityWorldEvent.Reposition {

	/**
	 * Gets the {@link ServerPlayer}<br>
	 * @return The player
	 */
	ServerPlayer player();

	/**
	 * Used {@link RandomTeleportOptions}
	 */
	RandomTeleportOptions getOptions();

}
