package sawfowl.commandpack.api.storages;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.AccountDeletionResultType;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import sawfowl.commandpack.apiclasses.economy.CPAccount;
import sawfowl.commandpack.apiclasses.economy.CPUniqueAccount;

public interface EconomyStorage {

	void load();

	void removeUniqueAccount(UUID uuid);

	void removeAccount(String identifier);

	void saveUniqueAccount(CPUniqueAccount account);

	void saveAccount(CPAccount account);

	boolean hasAccount(UUID uuid);

	boolean hasAccount(String identifier);

	Optional<UniqueAccount> findOrCreateAccount(UUID uuid);

	public Optional<Account> findOrCreateAccount(String identifier);

	Stream<UniqueAccount> streamUniqueAccounts();

	Collection<UniqueAccount> uniqueAccounts();

	Collection<Account> allAccounts();

	AccountDeletionResultType deleteAccount(UUID uuid);

	AccountDeletionResultType deleteAccount(String identifier);

	void checkAccounts(ServerPlayer player);

	Map<Character, Currency> getCurrenciesMap();

	double checkBalance(UUID uuid, Currency currency, double cached);

}
