package sawfowl.commandpack.api.events;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3i;

/**
 * This event is triggered constantly when a player breaks a block until the block is broken or the player stops breaking it.
 * 
 * @author SawFowl
 */
public interface BlockDestroyProgressEvent extends Event, Cancellable {

	/**
	 * The player who breaks the block.
	 */
	ServerPlayer getPlayer();

	/**
	 * The world in which the event takes place.
	 */
	ServerWorld getWorld();

	/**
	 * The block the player is trying to break.
	 */
	BlockState getBlock();

	/**
	 * The position of the block the player is trying to break.
	 */
	Vector3i getBlockPosition();

	/**
	 * Block breaking rate.
	 */
	float getSpeed();

	/**
	 * Current progress of block destruction.
	 */
	float getProgress();

	/**
	 * The previous level of block destruction.
	 */
	float getPreviousProgress();

}
