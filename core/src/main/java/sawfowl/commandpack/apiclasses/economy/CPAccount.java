package sawfowl.commandpack.apiclasses.economy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.VirtualAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionType;
import org.spongepowered.api.service.economy.transaction.TransactionTypes;
import org.spongepowered.api.service.economy.transaction.TransferResult;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.events.ChangeBalanceEvent;
import sawfowl.commandpack.api.events.ChangeBalanceEvent.Pre;
import sawfowl.commandpack.api.events.ChangeBalanceEvent.Post;
import sawfowl.commandpack.api.events.ChangeBalanceEvent.Transfer;
import sawfowl.commandpack.api.storages.EconomyStorage;
import sawfowl.commandpack.apiclasses.economy.events.change.PostImpl;
import sawfowl.commandpack.apiclasses.economy.events.change.PreImpl;
import sawfowl.commandpack.apiclasses.economy.storage.AbstractEconomyStorage;
import sawfowl.commandpack.configure.configs.economy.EconomyConfig;
import sawfowl.commandpack.configure.configs.economy.SerializedAccount;
import sawfowl.localeapi.api.TextUtils;

public class CPAccount implements Account, VirtualAccount {

	protected String identifier = "n/a";
	protected Map<Currency, BigDecimal> balances;
	private EconomyConfig config = CommandPackInstance.getInstance().getMainConfig().getEconomy();
	protected EconomyStorage storage;
	public CPAccount(){}
	public CPAccount(String identifier, Map<Currency, BigDecimal> balances, EconomyStorage storage) {
		this.identifier = identifier;
		this.balances = balances;
		this.storage = storage;
		save();
	}

	public static CPAccount create(String identifier, Map<Currency, BigDecimal> balances, AbstractEconomyStorage storage) {
		CPAccount account = new CPAccount();
		account.identifier = identifier;
		account.balances = balances;
		account.storage = storage;
		return account;
	}

	public static CPAccount deserealize(SerializedAccount serializedAccount, AbstractEconomyStorage storage) {
		CPAccount account = new CPAccount();
		account.identifier = serializedAccount.getName();
		account.balances = serializedAccount.getBalances(storage.getCurrenciesMap());
		account.storage = storage;
		return account;
	}

	@Override
	public String identifier() {
		return identifier;
	}

	@Override
	public Component displayName() {
		return text(identifier);
	}

	@Override
	public BigDecimal defaultBalance(Currency currency) {
		return config.getCurrency(currency.displayName()).map(v -> BigDecimal.valueOf(v.getStartingBalance())).orElse(BigDecimal.ZERO);
	}

	@Override
	public boolean hasBalance(Currency currency, Set<Context> contexts) {
		return balances.containsKey(currency);
	}

	@Override
	public boolean hasBalance(Currency currency, Cause cause) {
		return balances.containsKey(currency);
	}

	@Override
	public BigDecimal balance(Currency currency, Set<Context> contexts) {
		return balances.getOrDefault(currency, BigDecimal.ZERO);
	}

	@Override
	public BigDecimal balance(Currency currency, Cause cause) {
		return balances.getOrDefault(currency, BigDecimal.ZERO);
	}

	@Override
	public Map<Currency, BigDecimal> balances(Set<Context> contexts) {
		return new HashMap<Currency, BigDecimal>(balances);
	}

	@Override
	public Map<Currency, BigDecimal> balances(Cause cause) {
		return new HashMap<Currency, BigDecimal>(balances);
	}

	@Override
	public TransactionResult setBalance(Currency currency, BigDecimal amount, Set<Context> contexts) {
		TransactionType type = TransactionTypes.DEPOSIT.get();
		if(amount.doubleValue() < 0) amount = BigDecimal.ZERO;
		if(balances.containsKey(currency)) {
			if(balances.get(currency).doubleValue() > amount.doubleValue()) type = TransactionTypes.WITHDRAW.get();
			if(preChange(currency, amount, type).cancel()) return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.FAILED, type)).getResult();
			balances.remove(currency);
			balances.put(currency, amount);
		} else {
			if(preChange(currency, amount, type).cancel()) return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.FAILED, type)).getResult();
			balances.put(currency, amount);
		}
		save();
		return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.SUCCESS, type)).getResult();
	}

	@Override
	public TransactionResult setBalance(Currency currency, BigDecimal amount, Cause cause) {
		TransactionType type = TransactionTypes.DEPOSIT.get();
		if(amount.doubleValue() < 0) amount = BigDecimal.ZERO;
		if(balances.containsKey(currency)) {
			if(balances.get(currency).doubleValue() > amount.doubleValue()) type = TransactionTypes.WITHDRAW.get();
			if(preChange(currency, amount, type).cancel()) return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.FAILED, type)).getResult();
			balances.remove(currency);
			balances.put(currency, amount);
		} else {
			if(preChange(currency, amount, type).cancel()) return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.FAILED, type)).getResult();
			balances.put(currency, amount);
		}
		save();
		return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.SUCCESS, type)).getResult();
	}

	@Override
	public Map<Currency, TransactionResult> resetBalances(Set<Context> contexts) {
		return balances.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> resetBalance(entry.getKey(), contexts)));
	}

	@Override
	public Map<Currency, TransactionResult> resetBalances(Cause cause) {
		return balances.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> resetBalance(entry.getKey(), cause)));
	}

	@Override
	public TransactionResult resetBalance(Currency currency, Set<Context> contexts) {
		Optional<sawfowl.commandpack.configure.configs.economy.CurrencyConfig> optConfig = config.getCurrency(currency.displayName());
		boolean contains = balances.containsKey(currency);
		if(!optConfig.isPresent() && contains) {
			double old = balances.remove(currency).doubleValue();
			double newValue = balances.put(currency, BigDecimal.valueOf(optConfig.map(config -> config.getStartingBalance()).orElse(0d))).doubleValue();
			TransactionType type = old > newValue ? TransactionTypes.WITHDRAW.get() : TransactionTypes.DEPOSIT.get();
			BigDecimal amount = BigDecimal.valueOf(newValue);
			if(preChange(currency, amount, type).cancel()) return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.FAILED, type)).getResult();
			save();
			return postChange(currency, balances.get(currency), new CPTransactionResult(this, currency, balances.get(currency), ResultType.SUCCESS, type)).getResult();
		}
		sawfowl.commandpack.configure.configs.economy.CurrencyConfig config = optConfig.get();
		TransactionType type = (!contains || balances.get(currency).doubleValue() < config.getStartingBalance() ? TransactionTypes.DEPOSIT : TransactionTypes.WITHDRAW).get();
		BigDecimal amount = BigDecimal.valueOf(config.getStartingBalance());
		if(preChange(currency, amount, type).cancel()) return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.FAILED, type)).getResult();
		if(contains) balances.remove(currency);
		balances.put(currency, amount);
		save();
		return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.SUCCESS, type)).getResult();
	}

	@Override
	public TransactionResult resetBalance(Currency currency, Cause cause) {
		Optional<sawfowl.commandpack.configure.configs.economy.CurrencyConfig> optConfig = config.getCurrency(currency.displayName());
		boolean contains = balances.containsKey(currency);
		if(!optConfig.isPresent() && contains) {
			double old = balances.remove(currency).doubleValue();
			double newValue = balances.put(currency, BigDecimal.valueOf(optConfig.map(config -> config.getStartingBalance()).orElse(0d))).doubleValue();
			TransactionType type = old > newValue ? TransactionTypes.WITHDRAW.get() : TransactionTypes.DEPOSIT.get();
			BigDecimal amount = BigDecimal.valueOf(newValue);
			if(preChange(currency, amount, type).cancel()) return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.FAILED, type)).getResult();
			save();
			return postChange(currency, balances.get(currency), new CPTransactionResult(this, currency, balances.get(currency), ResultType.SUCCESS, type)).getResult();
		}
		sawfowl.commandpack.configure.configs.economy.CurrencyConfig config = optConfig.get();
		TransactionType type = (!contains || balances.get(currency).doubleValue() < config.getStartingBalance() ? TransactionTypes.DEPOSIT : TransactionTypes.WITHDRAW).get();
		BigDecimal amount = BigDecimal.valueOf(config.getStartingBalance());
		if(preChange(currency, amount, type).cancel()) return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.FAILED, type)).getResult();
		if(contains) balances.remove(currency);
		balances.put(currency, amount);
		save();
		return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.SUCCESS, type)).getResult();
	}

	@Override
	public TransactionResult deposit(Currency currency, BigDecimal amount, Set<Context> contexts) {
		TransactionType type = TransactionTypes.DEPOSIT.get();
		if(balances.containsKey(currency)) {
			if(amount.doubleValue() < 0) type = TransactionTypes.WITHDRAW.get();
			if(preChange(currency, amount, type).cancel()) return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.FAILED, type)).getResult();
			amount = balances.get(currency).add(amount);
			balances.remove(currency);
			balances.put(currency, amount);
		} else {
			if(preChange(currency, amount, type).cancel()) return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.FAILED, type)).getResult();
			balances.put(currency, amount);
		}
		if(balances.get(currency).doubleValue() < 0) balances.replace(currency, BigDecimal.ZERO);
		save();
		return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.SUCCESS, type)).getResult();
	}

	@Override
	public TransactionResult deposit(Currency currency, BigDecimal amount, Cause cause) {
		TransactionType type = TransactionTypes.DEPOSIT.get();
		if(balances.containsKey(currency)) {
			if(amount.doubleValue() < 0) type = TransactionTypes.WITHDRAW.get();
			if(preChange(currency, amount, type).cancel()) return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.FAILED, type)).getResult();
			amount = balances.get(currency).add(amount);
			balances.remove(currency);
			balances.put(currency, amount);
		} else {
			if(preChange(currency, amount, type).cancel()) return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.FAILED, type)).getResult();
			balances.put(currency, amount);
		}
		if(balances.get(currency).doubleValue() < 0) balances.replace(currency, BigDecimal.ZERO);
		save();
		return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.SUCCESS, type)).getResult();
	}

	@Override
	public TransactionResult withdraw(Currency currency, BigDecimal amount, Set<Context> contexts) {
		TransactionType type = TransactionTypes.WITHDRAW.get();
		if(balances.containsKey(currency)) {
			double check = checkDB(currency, balances.get(currency).doubleValue());
			if(check != balances.get(currency).doubleValue()) {
				balances.replace(currency, BigDecimal.valueOf(check));
				return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.ACCOUNT_NO_FUNDS, type)).getResult();
			}
			if(amount.doubleValue() < 0) type = TransactionTypes.DEPOSIT.get();
			amount = balances.get(currency).subtract(amount);
			if(preChange(currency, amount, type).cancel()) return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.FAILED, type)).getResult();
			balances.remove(currency);
			balances.put(currency, amount);
			if(balances.get(currency).doubleValue() < 0) balances.replace(currency, BigDecimal.ZERO);
			save();
			return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.SUCCESS, type)).getResult();
		} else return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.ACCOUNT_NO_FUNDS, type)).getResult();
	}

	@Override
	public TransactionResult withdraw(Currency currency, BigDecimal amount, Cause cause) {
		TransactionType type = TransactionTypes.WITHDRAW.get();
		if(balances.containsKey(currency)) {
			double check = checkDB(currency, balances.get(currency).doubleValue());
			if(check != balances.get(currency).doubleValue()) {
				balances.replace(currency, BigDecimal.valueOf(check));
				return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.ACCOUNT_NO_FUNDS, type)).getResult();
			}
			if(amount.doubleValue() < 0) type = TransactionTypes.DEPOSIT.get();
			amount = balances.get(currency).subtract(amount);
			if(preChange(currency, amount, type).cancel()) return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.FAILED, type)).getResult();
			balances.remove(currency);
			balances.put(currency, amount);
			if(balances.get(currency).doubleValue() < 0) balances.replace(currency, BigDecimal.ZERO);
			save();
			return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.SUCCESS, type)).getResult();
		} else return postChange(currency, amount, new CPTransactionResult(this, currency, amount, ResultType.ACCOUNT_NO_FUNDS, type)).getResult();
	}

	@Override
	public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Set<Context> contexts) {
		if(!balances.containsKey(currency) || balances.get(currency).doubleValue() < amount.doubleValue()) return postTransfer(to, currency, amount, new CPTransferResult(this, to, currency, amount, ResultType.ACCOUNT_NO_FUNDS, TransactionTypes.TRANSFER.get())).getResult();
		double check = checkDB(currency, balances.get(currency).doubleValue());
		if(check != balances.get(currency).doubleValue()) {
			balances.replace(currency, BigDecimal.valueOf(check));
			return postTransfer(to, currency, amount, new CPTransferResult(this, to, currency, amount, ResultType.ACCOUNT_NO_FUNDS, TransactionTypes.TRANSFER.get())).getResult();
		}
		BigDecimal newValue = balances.get(currency).subtract(amount);
		if(preTransfer(to, currency, amount).cancel() || preChange(currency, newValue, TransactionTypes.WITHDRAW.get()).cancel()) return postTransfer(to, currency, amount, new CPTransferResult(this, to, currency, amount, ResultType.FAILED, TransactionTypes.TRANSFER.get())).getResult();;
		balances.remove(currency);
		balances.put(currency, newValue);
		to.deposit(currency, amount);
		save();
		return postTransfer(to, currency, amount, new CPTransferResult(this, to, currency, amount, ResultType.SUCCESS, TransactionTypes.TRANSFER.get())).getResult();
	}

	@Override
	public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Cause cause) {
		if(!balances.containsKey(currency) || balances.get(currency).doubleValue() < amount.doubleValue()) return postTransfer(to, currency, amount, new CPTransferResult(this, to, currency, amount, ResultType.ACCOUNT_NO_FUNDS, TransactionTypes.TRANSFER.get())).getResult();
		double check = checkDB(currency, balances.get(currency).doubleValue());
		if(check != balances.get(currency).doubleValue()) {
			balances.replace(currency, BigDecimal.valueOf(check));
			return postTransfer(to, currency, amount, new CPTransferResult(this, to, currency, amount, ResultType.ACCOUNT_NO_FUNDS, TransactionTypes.TRANSFER.get())).getResult();
		}
		BigDecimal newValue = balances.get(currency).subtract(amount);
		if(preTransfer(to, currency, amount).cancel() || preChange(currency, newValue, TransactionTypes.WITHDRAW.get()).cancel()) return postTransfer(to, currency, amount, new CPTransferResult(this, to, currency, amount, ResultType.FAILED, TransactionTypes.TRANSFER.get())).getResult();;
		balances.remove(currency);
		balances.put(currency, newValue);
		to.deposit(currency, amount);
		save();
		return postTransfer(to, currency, amount, new CPTransferResult(this, to, currency, amount, ResultType.SUCCESS, TransactionTypes.TRANSFER.get())).getResult();
	}

	public CPAccount setStorage(AbstractEconomyStorage storage) {
		if(this.storage == null) {
			this.storage = storage;
			save();
		}
		return this;
	}

	public void save() {
		if(storage != null) storage.saveAccount(this);
	}

	protected Component text(String string) {
		if(isLegacyDecor(string)) {
			return TextUtils.deserializeLegacy(string);
		} else {
			return TextUtils.deserialize(string);
		}
	}

	protected double checkDB(Currency currency, double cached) {
		return cached;
	}

	private boolean isLegacyDecor(String string) {
		return string.indexOf('&') != -1 && !string.endsWith("&") && isStyleChar(string.charAt(string.indexOf("&") + 1));
	}

	private boolean isStyleChar(char ch) {
		return "0123456789abcdefklmnor".indexOf(ch) != -1;
	}

	@Override
	public String toString() {
		return "CPAccount [identifier=" + identifier + ", balances=" + balances + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(identifier);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		return Objects.equals(identifier, ((CPAccount) obj).identifier);
	}

	private <T extends ChangeBalanceEvent> T postEvent(T event) {
		Sponge.eventManager().post(event);
		return event;
	}

	private Pre preChange(Currency currency, BigDecimal amount, TransactionType type) {
		return postEvent(new PreImpl(this, currency, amount, type));
	}

	private Post postChange(Currency currency, BigDecimal amount, TransactionResult result) {
		return postEvent(new PostImpl(this, currency, amount, result));
	}

	private Transfer.Pre preTransfer(Account other, Currency currency, BigDecimal amount) {
		return postEvent(new sawfowl.commandpack.apiclasses.economy.events.transfer.PreImpl(this, other, currency, amount));
	}

	private Transfer.Post postTransfer(Account other, Currency currency, BigDecimal amount, TransferResult result) {
		return postEvent(new sawfowl.commandpack.apiclasses.economy.events.transfer.PostImpl(this, other, currency, amount, result));
	}

}
