package sawfowl.commandpack.apiclasses.economy.storage;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.AccountDeletionResultType;
import org.spongepowered.api.service.economy.account.AccountDeletionResultTypes;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationOptions;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.apiclasses.economy.CPAccount;
import sawfowl.commandpack.apiclasses.economy.CPUniqueAccount;
import sawfowl.commandpack.apiclasses.economy.EconomyServiceImpl;

public abstract class AbstractEconomyStorage extends Thread {

	final CommandPack plugin;
	final EconomyServiceImpl economyService;

	Map<UUID, UniqueAccount> uniqueAccounts = new HashMap<UUID, UniqueAccount>();
	Map<String, Account> accounts = new HashMap<String, Account>();
	ConfigurationOptions options;
	Map<Currency, BigDecimal> defaultBalances;
	public AbstractEconomyStorage(CommandPack plugin, EconomyServiceImpl economyService) {
		this.plugin = plugin;
		this.economyService = economyService;
		options = plugin.getLocales().getLocaleService().getConfigurationOptions();
		defaultBalances = createDefaultBalances();
		try {
			load();
		} catch (ConfigurateException e) {
			plugin.getLogger().error(e.getLocalizedMessage());
		}
	}

	public abstract void load() throws ConfigurateException;

	public abstract void removeUniqueAccount(UUID uuid);

	public abstract void removeAccount(String identifier);

	public abstract void saveUniqueAccount(CPUniqueAccount account);

	public abstract void saveAccount(CPAccount account);

	public  UniqueAccount createUniqueAccount(UUID uuid) {
		return uniqueAccounts.put(uuid, new CPUniqueAccount(uuid, defaultBalances, this));
	}

	public Account createAccount(String identifier) {
		return accounts.put(identifier, new CPAccount(identifier, defaultBalances, this));
	}

	public EconomyServiceImpl getEconomyService() {
		return economyService;
	}

	public boolean hasAccount(UUID uuid) {
		return uniqueAccounts.containsKey(uuid);
	}

	public boolean hasAccount(String identifier) {
		return accounts.containsKey(identifier);
	}

	public Optional<UniqueAccount> findOrCreateAccount(UUID uuid) {
		return Optional.ofNullable(hasAccount(uuid) ? uniqueAccounts.get(uuid) : createUniqueAccount(uuid));
	}

	public Optional<Account> findOrCreateAccount(String identifier) {
		return Optional.ofNullable(hasAccount(identifier) ? accounts.get(identifier) : createAccount(identifier));
	}

	public Stream<UniqueAccount> streamUniqueAccounts() {
		return uniqueAccounts.values().stream();
	}

	public Collection<UniqueAccount> uniqueAccounts() {
		return uniqueAccounts.values();
	}

	public AccountDeletionResultType deleteAccount(UUID uuid) {
		if(!uniqueAccounts.containsKey(uuid)) return AccountDeletionResultTypes.ABSENT.get();
		uniqueAccounts.remove(uuid);
		removeUniqueAccount(uuid);
		return AccountDeletionResultTypes.SUCCESS.get();
	}

	public AccountDeletionResultType deleteAccount(String identifier) {
		if(!accounts.containsKey(identifier)) return AccountDeletionResultTypes.ABSENT.get();
		accounts.remove(identifier);
		removeAccount(identifier);
		return AccountDeletionResultTypes.SUCCESS.get();
	}

	public Map<Currency, BigDecimal> createDefaultBalances() {
		Map<Currency, BigDecimal> balances = new HashMap<Currency, BigDecimal>();
		for(Entry<Character, Currency> currency : economyService.getCurrenciesMap().entrySet()) {
			plugin.getMainConfig().getEconomy().getCurrency(currency.getKey()).ifPresent(config -> {
				balances.put(currency.getValue(), BigDecimal.valueOf(config.getStartingBalance()));
			});
		}
		return balances;
	}

	public void checkAccounts(ServerPlayer player) {
		if(hasAccount(player.uniqueId()) || !hasAccount(player.name())) return;
		CPUniqueAccount account = CPUniqueAccount.create(player.uniqueId(), player.name(), accounts.get(player.name()).balances(), this);
		uniqueAccounts.put(player.uniqueId(), account);
		removeAccount(player.name());
		account.save();
	}

}
