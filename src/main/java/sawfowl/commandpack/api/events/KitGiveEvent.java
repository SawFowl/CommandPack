package sawfowl.commandpack.api.events;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;

import sawfowl.commandpack.api.data.kits.GiveRule;
import sawfowl.commandpack.api.data.kits.Kit;

/**
 * The event of giving a kit.<br>
 * The event is not called when giving a kit with administrative permission.
 * 
 * @author SawFowl
 */
public interface KitGiveEvent extends Event {

	/**
	 * The player to whom the kit is given.
	 */
	ServerPlayer getPlayer();

	/**
	 * A kit that is given to the player.
	 */
	Kit kit();

	/**
	 * The time when the kit will be available to the player again.
	 */
	long getNextAllowedAccess();

	/**
	 * The event will be canceled by default if the player cannot currently get a kit.
	 */
	interface Pre extends KitGiveEvent, Cancellable {

		/**
		 * Kit giving time.
		 */
		long getCurrentTime();

		/**
		 * The time when the current kit was given to the player last time.
		 */
		long getPreviousGiveTime();

		/**
		 * Kit giving rule.
		 */
		GiveRule getGiveRule();

	}

	/**
	 * Event is not called if no kit has been given.
	 */
	interface Post extends KitGiveEvent {

		/**
		 * The result of placing items from the set into the player's inventory.
		 */
		InventoryTransactionResult getResult();

		/**
		 * Whether or not a kit has been given out.
		 */
		boolean isGived();

	}

}
