package sawfowl.commandpack.apiclasses.economy;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.AccountDeletionResultType;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.account.VirtualAccount;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.services.CPEconomyService;
import sawfowl.commandpack.apiclasses.economy.storage.AbstractEconomyStorage;
import sawfowl.commandpack.apiclasses.economy.storage.FileStorage;
import sawfowl.commandpack.apiclasses.economy.storage.H2Storage;
import sawfowl.commandpack.apiclasses.economy.storage.MySqlStorage;
import sawfowl.commandpack.utils.MariaDB;

public class EconomyServiceImpl implements CPEconomyService {

	CommandPack plugin;
	private AbstractEconomyStorage storage;
	private Currency def;
	private Currency[] currencies;
	private Map<Character, Currency> currenciesMap = new HashMap<Character, Currency>();
	private Map<String, VirtualAccount> virtualAccounts = new HashMap<String, VirtualAccount>();
	public EconomyServiceImpl(CommandPack plugin) {
		this.plugin = plugin;
		def = new CPCurrency(plugin.getMainConfig().getEconomy().getDefaultCurrency().getSymbol());
		for(sawfowl.commandpack.configure.configs.economy.CurrencyConfig currency : plugin.getMainConfig().getEconomy().getCurrencies()) {
			currenciesMap.put(currency.getSymbol(), new CPCurrency(currency.getSymbol()));
		}
		switch (plugin.getMainConfig().getEconomy().getStorageType()) {
			case FILE:
				storage = new FileStorage(plugin, this);
				break;
			case H2:
				try {
					Class.forName("org.h2.Driver");
					storage = new H2Storage(plugin, this);
					plugin.getLogger().info("The h2 database is used to store the economy data.");
				} catch (Exception e) {
					storage = new FileStorage(plugin, this);
					plugin.getLogger().warn("H2 driver not found! Configuration files based data storage will be used.");
				}
				break;
			case MYSQL:
				Optional<MariaDB> optMariaDB = plugin.getMariaDB();
				if(optMariaDB.isPresent()) {
					storage = new MySqlStorage(plugin, this);
					plugin.getLogger().info("The MySql database is used to store the economy data.");
				} else {
					storage = new FileStorage(plugin, this);
					plugin.getLogger().error("MySql or MariaDB Driver not found! Configuration files based data storage will be used.");
				}
				break;
			default:
				storage = new FileStorage(plugin, this);
				break;
		}
		currencies = currenciesMap.values().toArray(new Currency[]{});
		virtualAccounts.put("Server", new CPAccount("Server", new HashMap<Currency, BigDecimal>(), null));
	}

	@Override
	public Currency[] getCurrencies() {
		return currencies;
	}

	@Override
	public Map<Character, Currency> getCurrenciesMap() {
		return new HashMap<Character, Currency>(currenciesMap);
	}

	@Override
	public Currency defaultCurrency() {
		return def;
	}

	@Override
	public boolean hasAccount(UUID uuid) {
		return storage.hasAccount(uuid);
	}

	@Override
	public boolean hasAccount(String identifier) {
		return storage.hasAccount(identifier);
	}

	@Override
	public Optional<UniqueAccount> findOrCreateAccount(UUID uuid) {
		return storage.findOrCreateAccount(uuid);
	}

	@Override
	public Optional<Account> findOrCreateAccount(String identifier) {
		return storage.findOrCreateAccount(identifier);
	}

	@Override
	public Stream<UniqueAccount> streamUniqueAccounts() {
		return storage.streamUniqueAccounts();
	}

	@Override
	public Collection<UniqueAccount> uniqueAccounts() {
		return storage.uniqueAccounts();
	}

	@Override
	public Stream<VirtualAccount> streamVirtualAccounts() {
		return virtualAccounts().stream();
	}

	@Override
	public Collection<VirtualAccount> virtualAccounts() {
		return virtualAccounts.values();
	}

	@Override
	public AccountDeletionResultType deleteAccount(UUID uuid) {
		return storage.deleteAccount(uuid);
	}

	@Override
	public AccountDeletionResultType deleteAccount(String identifier) {
		return storage.deleteAccount(identifier);
	}

}
