package sawfowl.commandpack.api.events;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.world.server.ServerWorld;

import sawfowl.commandpack.api.data.miscellaneous.RandomTeleportOptions;

public interface RandomTeleportEvent extends MoveEntityEvent {

	/**
	 * Gets the {@link ServerPlayer}<br>
	 * @return The player
	 */
	ServerPlayer player();

	/**
	 * Gets the {@link ServerWorld}<br>
	 * @return The world
	 */
	ServerWorld getDestinationWorld();

	/**
	 * Change the {@link ServerWorld} to which the player will be moved.
	 */
	void setDestinationWorld(ServerWorld world);

	/**
	 * Used {@link RandomTeleportOptions}
	 */
	RandomTeleportOptions getOptions();

}
