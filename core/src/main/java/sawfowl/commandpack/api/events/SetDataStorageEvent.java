package sawfowl.commandpack.api.events;

import java.math.BigDecimal;
import java.util.Map;

import org.spongepowered.api.event.Event;
import org.spongepowered.api.service.economy.Currency;

import sawfowl.commandpack.api.storages.EconomyStorage;
import sawfowl.commandpack.api.storages.PunishmentStorage;

public interface SetDataStorageEvent extends Event {

	interface Economy extends SetDataStorageEvent {

		void setStorage(EconomyStorage storage);

		Map<Character, Currency> getCurrenciesMap();

		Map<Currency, BigDecimal> getDefaultBalances();

	}

	interface Punishment extends SetDataStorageEvent {

		void setStorage(PunishmentStorage storage);

	}

}
