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

	ServerPlayer getPlayer();

	Kit kit();

	long getNextAllowedAccess();

	/**
	 * The event will be canceled by default if the player cannot currently get a kit.
	 */
	interface Pre extends KitGiveEvent, Cancellable {

		long getCurrentTime();

		long getPreviousGiveTime();

		GiveRule getGiveRule();

	}

	/**
	 * Event is not called if no kit has been given.
	 */
	interface Post extends KitGiveEvent {

		InventoryTransactionResult getResult();

		boolean isGived();

	}

}
