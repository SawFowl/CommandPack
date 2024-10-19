package sawfowl.commandpack.api.events;

import org.spongepowered.api.event.Event;

import sawfowl.commandpack.api.storages.EconomyStorage;
import sawfowl.commandpack.api.storages.PunishmentStorage;

public interface SetDataStorageEvent extends Event {

	interface Economy extends SetDataStorageEvent {

		void setStorage(EconomyStorage storage);

	}

	interface Punishment extends SetDataStorageEvent {

		void setStorage(PunishmentStorage storage);

	}

}
