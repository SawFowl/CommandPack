package sawfowl.commandpack.api.events;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;

import sawfowl.commandpack.api.data.kits.GiveRule;
import sawfowl.commandpack.api.data.kits.Kit;

public interface KitGiveEvent extends Event {

	ServerPlayer getPlayer();

	Kit kit();

	long getNextAllowedAccess();

	interface Pre extends KitGiveEvent, Cancellable {

		long getCurrentTime();

		long getPreviousGiveTime();

		GiveRule getGiveRule();

	}

	interface Post extends KitGiveEvent {

		InventoryTransactionResult getResult();

		boolean isGived();

	}

}
