package sawfowl.commandpack.apiclasses.economy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.apiclasses.economy.storage.AbstractEconomyStorage;
import sawfowl.commandpack.configure.configs.economy.EconomyConfig;
import sawfowl.commandpack.configure.configs.economy.SerializedAccount;
import sawfowl.localeapi.api.TextUtils;

public class CPAccount implements Account, VirtualAccount {

	private String identifier;
	private Map<Currency, BigDecimal> balances;
	private EconomyConfig config = CommandPack.getInstance().getMainConfig().getEconomy();
	private AbstractEconomyStorage storage;
	public CPAccount(){}
	public CPAccount(String identifier, Map<Currency, BigDecimal> balances, AbstractEconomyStorage storage) {
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
		account.balances = serializedAccount.getBalances(storage.getEconomyService().getCurrenciesMap());
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
		return balances;
	}

	@Override
	public Map<Currency, BigDecimal> balances(Cause cause) {
		return balances;
	}

	@Override
	public TransactionResult setBalance(Currency currency, BigDecimal amount, Set<Context> contexts) {
		TransactionType type = TransactionTypes.DEPOSIT.get();
		if(amount.doubleValue() < 0) amount = BigDecimal.ZERO;
		BigDecimal finalAmount = amount;
		if(balances.containsKey(currency)) {
			if(balances.get(currency).doubleValue() > amount.doubleValue()) type = TransactionTypes.WITHDRAW.get();
			balances.replace(currency, amount);
		} balances.put(currency, amount);
		TransactionType finalType = type;
		save();
		return new TransactionResult() {
			
			@Override
			public TransactionType type() {
				return finalType;
			}
			
			@Override
			public ResultType result() {
				return ResultType.SUCCESS;
			}
			
			@Override
			public Currency currency() {
				return currency;
			}
			
			@Override
			public Set<Context> contexts() {
				return new HashSet<Context>();
			}
			
			@Override
			public BigDecimal amount() {
				return finalAmount;
			}
			
			@Override
			public Account account() {
				return CPAccount.this;
			}
		};
	}

	@Override
	public TransactionResult setBalance(Currency currency, BigDecimal amount, Cause cause) {
		TransactionType type = TransactionTypes.DEPOSIT.get();
		if(amount.doubleValue() < 0) amount = BigDecimal.ZERO;
		BigDecimal finalAmount = amount;
		if(balances.containsKey(currency)) {
			if(balances.get(currency).doubleValue() > amount.doubleValue()) type = TransactionTypes.WITHDRAW.get();
			balances.replace(currency, amount);
		} balances.put(currency, amount);
		TransactionType finalType = type;
		save();
		return new TransactionResult() {
			
			@Override
			public TransactionType type() {
				return finalType;
			}
			
			@Override
			public ResultType result() {
				return ResultType.SUCCESS;
			}
			
			@Override
			public Currency currency() {
				return currency;
			}
			
			@Override
			public Set<Context> contexts() {
				return new HashSet<Context>();
			}
			
			@Override
			public BigDecimal amount() {
				return finalAmount;
			}
			
			@Override
			public Account account() {
				return CPAccount.this;
			}
		};
	}

	@Override
	public Map<Currency, TransactionResult> resetBalances(Set<Context> contexts) {
		Map<Currency, TransactionResult> transacrions = new HashMap<Currency, TransactionResult>();
		balances.entrySet().removeIf(b -> !config.getCurrency(b.getKey().displayName()).isPresent());
		config.getCurrencies().forEach(cur -> {
			Optional<Currency> optCurrency = balances.keySet().stream().filter(c -> TextUtils.serializeLegacy(c.displayName()).equals(cur.getName())).findFirst();
			if(optCurrency.isPresent()) {
				TransactionType type = balances.get(optCurrency.get()).doubleValue() < cur.getStartingBalance() ? TransactionTypes.DEPOSIT.get() : TransactionTypes.WITHDRAW.get();
				BigDecimal amount = BigDecimal.valueOf(cur.getStartingBalance());
				balances.replace(optCurrency.get(), amount);
				transacrions.put(optCurrency.get(), new TransactionResult() {
					
					@Override
					public TransactionType type() {
						return type;
					}
					
					@Override
					public ResultType result() {
						return ResultType.SUCCESS;
					}
					
					@Override
					public Currency currency() {
						return optCurrency.get();
					}
					
					@Override
					public Set<Context> contexts() {
						return new HashSet<Context>();
					}
					
					@Override
					public BigDecimal amount() {
						return amount;
					}
					
					@Override
					public Account account() {
						return CPAccount.this;
					}
				}
				);
			}
		});
		save();
		return transacrions;
	}

	@Override
	public Map<Currency, TransactionResult> resetBalances(Cause cause) {
		Map<Currency, TransactionResult> transacrions = new HashMap<Currency, TransactionResult>();
		balances.entrySet().removeIf(b -> !config.getCurrency(b.getKey().displayName()).isPresent());
		config.getCurrencies().forEach(cur -> {
			Optional<Currency> optCurrency = balances.keySet().stream().filter(c -> TextUtils.serializeLegacy(c.displayName()).equals(cur.getName())).findFirst();
			if(optCurrency.isPresent()) {
				TransactionType type = balances.get(optCurrency.get()).doubleValue() < cur.getStartingBalance() ? TransactionTypes.DEPOSIT.get() : TransactionTypes.WITHDRAW.get();
				BigDecimal amount = BigDecimal.valueOf(cur.getStartingBalance());
				balances.replace(optCurrency.get(), amount);
				transacrions.put(optCurrency.get(), new TransactionResult() {
					
					@Override
					public TransactionType type() {
						return type;
					}
					
					@Override
					public ResultType result() {
						return ResultType.SUCCESS;
					}
					
					@Override
					public Currency currency() {
						return optCurrency.get();
					}
					
					@Override
					public Set<Context> contexts() {
						return new HashSet<Context>();
					}
					
					@Override
					public BigDecimal amount() {
						return amount;
					}
					
					@Override
					public Account account() {
						return CPAccount.this;
					}
				}
				);
			}
		});
		save();
		return transacrions;
	}

	@Override
	public TransactionResult resetBalance(Currency currency, Set<Context> contexts) {
		Optional<sawfowl.commandpack.configure.configs.economy.CurrencyConfig> optConfig = config.getCurrency(currency.displayName());
		boolean contains = balances.containsKey(currency);
		if(!optConfig.isPresent() && contains) {
			balances.remove(currency);
			save();
			return new TransactionResult() {
				
				@Override
				public TransactionType type() {
					return TransactionTypes.WITHDRAW.get();
				}
				
				@Override
				public ResultType result() {
					return ResultType.FAILED;
				}
				
				@Override
				public Currency currency() {
					return currency;
				}
				
				@Override
				public Set<Context> contexts() {
					return new HashSet<Context>();
				}
				
				@Override
				public BigDecimal amount() {
					return BigDecimal.ZERO;
				}
				
				@Override
				public Account account() {
					return CPAccount.this;
				}
			};
		}
		sawfowl.commandpack.configure.configs.economy.CurrencyConfig config = optConfig.get();
		TransactionType type = (!contains || balances.get(currency).doubleValue() < config.getStartingBalance() ? TransactionTypes.DEPOSIT : TransactionTypes.WITHDRAW).get();
		BigDecimal amount = BigDecimal.valueOf(config.getStartingBalance());
		if(contains) {
			balances.replace(currency, amount);
		} else balances.put(currency, amount);
		save();
		return new TransactionResult() {
			
			@Override
			public TransactionType type() {
				return type;
			}
			
			@Override
			public ResultType result() {
				return ResultType.SUCCESS;
			}
			
			@Override
			public Currency currency() {
				return currency;
			}
			
			@Override
			public Set<Context> contexts() {
				return new HashSet<Context>();
			}
			
			@Override
			public BigDecimal amount() {
				return amount;
			}
			
			@Override
			public Account account() {
				return CPAccount.this;
			}
		};
	}

	@Override
	public TransactionResult resetBalance(Currency currency, Cause cause) {
		Optional<sawfowl.commandpack.configure.configs.economy.CurrencyConfig> optConfig = config.getCurrency(currency.displayName());
		boolean contains = balances.containsKey(currency);
		if(!optConfig.isPresent() && contains) {
			balances.remove(currency);
			save();
			return new TransactionResult() {
				
				@Override
				public TransactionType type() {
					return TransactionTypes.WITHDRAW.get();
				}
				
				@Override
				public ResultType result() {
					return ResultType.FAILED;
				}
				
				@Override
				public Currency currency() {
					return currency;
				}
				
				@Override
				public Set<Context> contexts() {
					return new HashSet<Context>();
				}
				
				@Override
				public BigDecimal amount() {
					return BigDecimal.ZERO;
				}
				
				@Override
				public Account account() {
					return CPAccount.this;
				}
			};
		}
		sawfowl.commandpack.configure.configs.economy.CurrencyConfig config = optConfig.get();
		TransactionType type = (!contains || balances.get(currency).doubleValue() < config.getStartingBalance() ? TransactionTypes.DEPOSIT : TransactionTypes.WITHDRAW).get();
		BigDecimal amount = BigDecimal.valueOf(config.getStartingBalance());
		if(contains) {
			balances.replace(currency, amount);
		} else balances.put(currency, amount);
		save();
		return new TransactionResult() {
			
			@Override
			public TransactionType type() {
				return type;
			}
			
			@Override
			public ResultType result() {
				return ResultType.SUCCESS;
			}
			
			@Override
			public Currency currency() {
				return currency;
			}
			
			@Override
			public Set<Context> contexts() {
				return new HashSet<Context>();
			}
			
			@Override
			public BigDecimal amount() {
				return amount;
			}
			
			@Override
			public Account account() {
				return CPAccount.this;
			}
		};
	}

	@Override
	public TransactionResult deposit(Currency currency, BigDecimal amount, Set<Context> contexts) {
		TransactionType type = TransactionTypes.DEPOSIT.get();
		if(balances.containsKey(currency)) {
			if(amount.doubleValue() < 0) type = TransactionTypes.WITHDRAW.get();
			amount = balances.get(currency).add(amount);
			balances.replace(currency, amount);
		} balances.put(currency, amount);
		if(balances.get(currency).doubleValue() < 0) balances.replace(currency, BigDecimal.ZERO);
		TransactionType finalType = type;
		BigDecimal finalAmount = amount;
		save();
		return new TransactionResult() {
			
			@Override
			public TransactionType type() {
				return finalType;
			}
			
			@Override
			public ResultType result() {
				return ResultType.SUCCESS;
			}
			
			@Override
			public Currency currency() {
				return currency;
			}
			
			@Override
			public Set<Context> contexts() {
				return new HashSet<Context>();
			}
			
			@Override
			public BigDecimal amount() {
				return finalAmount;
			}
			
			@Override
			public Account account() {
				return CPAccount.this;
			}
		};
	}

	@Override
	public TransactionResult deposit(Currency currency, BigDecimal amount, Cause cause) {
		TransactionType type = TransactionTypes.DEPOSIT.get();
		if(balances.containsKey(currency)) {
			if(amount.doubleValue() < 0) type = TransactionTypes.WITHDRAW.get();
			amount = balances.get(currency).add(amount);
			balances.replace(currency, amount);
		} balances.put(currency, amount);
		if(balances.get(currency).doubleValue() < 0) balances.replace(currency, BigDecimal.ZERO);
		TransactionType finalType = type;
		BigDecimal finalAmount = amount;
		save();
		return new TransactionResult() {
			
			@Override
			public TransactionType type() {
				return finalType;
			}
			
			@Override
			public ResultType result() {
				return ResultType.SUCCESS;
			}
			
			@Override
			public Currency currency() {
				return currency;
			}
			
			@Override
			public Set<Context> contexts() {
				return new HashSet<Context>();
			}
			
			@Override
			public BigDecimal amount() {
				return finalAmount;
			}
			
			@Override
			public Account account() {
				return CPAccount.this;
			}
		};
	}

	@Override
	public TransactionResult withdraw(Currency currency, BigDecimal amount, Set<Context> contexts) {
		TransactionType type = TransactionTypes.WITHDRAW.get();
		if(balances.containsKey(currency)) {
			if(amount.doubleValue() < 0) type = TransactionTypes.DEPOSIT.get();
			amount = balances.get(currency).subtract(amount);
			balances.replace(currency, amount);
		} balances.put(currency, amount);
		if(balances.get(currency).doubleValue() < 0) balances.replace(currency, BigDecimal.ZERO);
		TransactionType finalType = type;
		BigDecimal finalAmount = amount;
		save();
		return new TransactionResult() {
			
			@Override
			public TransactionType type() {
				return finalType;
			}
			
			@Override
			public ResultType result() {
				return ResultType.SUCCESS;
			}
			
			@Override
			public Currency currency() {
				return currency;
			}
			
			@Override
			public Set<Context> contexts() {
				return new HashSet<Context>();
			}
			
			@Override
			public BigDecimal amount() {
				return finalAmount;
			}
			
			@Override
			public Account account() {
				return CPAccount.this;
			}
		};
	}

	@Override
	public TransactionResult withdraw(Currency currency, BigDecimal amount, Cause cause) {
		TransactionType type = TransactionTypes.WITHDRAW.get();
		if(balances.containsKey(currency)) {
			if(amount.doubleValue() < 0) type = TransactionTypes.DEPOSIT.get();
			amount = balances.get(currency).subtract(amount);
			balances.replace(currency, amount);
		} balances.put(currency, amount);
		if(balances.get(currency).doubleValue() < 0) balances.replace(currency, BigDecimal.ZERO);
		TransactionType finalType = type;
		BigDecimal finalAmount = amount;
		save();
		return new TransactionResult() {
			
			@Override
			public TransactionType type() {
				return finalType;
			}
			
			@Override
			public ResultType result() {
				return ResultType.SUCCESS;
			}
			
			@Override
			public Currency currency() {
				return currency;
			}
			
			@Override
			public Set<Context> contexts() {
				return new HashSet<Context>();
			}
			
			@Override
			public BigDecimal amount() {
				return finalAmount;
			}
			
			@Override
			public Account account() {
				return CPAccount.this;
			}
		};
	}

	@Override
	public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Set<Context> contexts) {
		if(!balances.containsKey(currency) || balances.get(currency).doubleValue() < amount.doubleValue()) return new TransferResult() {
			
			@Override
			public TransactionType type() {
				return TransactionTypes.TRANSFER.get();
			}
			
			@Override
			public ResultType result() {
				return ResultType.ACCOUNT_NO_FUNDS;
			}
			
			@Override
			public Currency currency() {
				return currency;
			}
			
			@Override
			public Set<Context> contexts() {
				return contexts;
			}
			
			@Override
			public BigDecimal amount() {
				return amount;
			}
			
			@Override
			public Account account() {
				return CPAccount.this;
			}
			
			@Override
			public Account accountTo() {
				return to;
			}
		};
		balances.replace(currency, balances.get(currency).subtract(amount));
		to.deposit(currency, amount);
		save();
		return new TransferResult() {
			
			@Override
			public TransactionType type() {
				return TransactionTypes.TRANSFER.get();
			}
			
			@Override
			public ResultType result() {
				return ResultType.SUCCESS;
			}
			
			@Override
			public Currency currency() {
				return currency;
			}
			
			@Override
			public Set<Context> contexts() {
				return contexts;
			}
			
			@Override
			public BigDecimal amount() {
				return amount;
			}
			
			@Override
			public Account account() {
				return CPAccount.this;
			}
			
			@Override
			public Account accountTo() {
				return to;
			}
		};
	}

	@Override
	public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Cause cause) {
		if(!balances.containsKey(currency) || balances.get(currency).doubleValue() < amount.doubleValue()) return new TransferResult() {
			
			@Override
			public TransactionType type() {
				return TransactionTypes.TRANSFER.get();
			}
			
			@Override
			public ResultType result() {
				return ResultType.ACCOUNT_NO_FUNDS;
			}
			
			@Override
			public Currency currency() {
				return currency;
			}
			
			@Override
			public Set<Context> contexts() {
				return new HashSet<Context>();
			}
			
			@Override
			public BigDecimal amount() {
				return amount;
			}
			
			@Override
			public Account account() {
				return CPAccount.this;
			}
			
			@Override
			public Account accountTo() {
				return to;
			}
		};
		balances.replace(currency, balances.get(currency).subtract(amount));
		to.deposit(currency, amount);
		save();
		return new TransferResult() {
			
			@Override
			public TransactionType type() {
				return TransactionTypes.TRANSFER.get();
			}
			
			@Override
			public ResultType result() {
				return ResultType.SUCCESS;
			}
			
			@Override
			public Currency currency() {
				return currency;
			}
			
			@Override
			public Set<Context> contexts() {
				return new HashSet<Context>();
			}
			
			@Override
			public BigDecimal amount() {
				return amount;
			}
			
			@Override
			public Account account() {
				return CPAccount.this;
			}
			
			@Override
			public Account accountTo() {
				return to;
			}
		};
	}

	private Component text(String string) {
		if(isLegacyDecor(string)) {
			return TextUtils.deserializeLegacy(string);
		} else {
			return TextUtils.deserialize(string);
		}
	}

	private boolean isLegacyDecor(String string) {
		return string.indexOf('&') != -1 && !string.endsWith("&") && isStyleChar(string.charAt(string.indexOf("&") + 1));
	}

	private boolean isStyleChar(char ch) {
		return "0123456789abcdefklmnor".indexOf(ch) != -1;
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

}
