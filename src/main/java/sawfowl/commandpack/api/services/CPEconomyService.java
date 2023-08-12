package sawfowl.commandpack.api.services;

import java.util.Map;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;

public interface CPEconomyService extends EconomyService {

	/**
	 * Getting a set of registered currencies.
	 */
	Currency[] getCurrencies();

	/**
	 * Getting a copy of the map of registered currencies.
	 */
	Map<Character, Currency> getCurrenciesMap();

}
