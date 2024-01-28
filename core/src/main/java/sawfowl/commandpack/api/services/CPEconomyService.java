package sawfowl.commandpack.api.services;

import java.util.Map;
import java.util.UUID;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;

public interface CPEconomyService extends EconomyService {

	/**
	 * Getting a set of registered currencies.
	 */
	Currency[] getCurrencies();

	/**
	 * Getting a copy of the map of registered currencies.
	 */
	Map<Character, Currency> getCurrenciesMap();

	/**
	 * Adding a player's UUID to the balance hiding collection.<br>
	 * If the UUID is already in the collection, it will be removed from it.
	 */
	void hide(UUID uuid);

	/**
	 * If true, the balance should be hidden from view by other players.<br>
	 * Hiding is implemented independently and does not depend on the economy API.
	 */
	boolean isHiden(UniqueAccount account);

	/**
	 * If true, the balance should be hidden from view by other players.<br>
	 * Hiding is implemented independently and does not depend on the economy API.
	 */
	boolean isHiden(UUID uuid);

}
