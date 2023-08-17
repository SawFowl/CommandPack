package sawfowl.commandpack.apiclasses.economy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionType;
import org.spongepowered.api.service.economy.transaction.TransactionTypes;
import org.spongepowered.api.service.economy.transaction.TransferResult;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.apiclasses.economy.storage.AbstractEconomyStorage;
import sawfowl.commandpack.configure.configs.economy.EconomyConfig;
import sawfowl.commandpack.configure.configs.economy.SerializedUniqueAccount;
import sawfowl.localeapi.api.TextUtils;

public class CPUniqueAccount extends CPAccount implements UniqueAccount {

	private UUID userId;
	private String displayName = "n/a";
	private Map<Currency, BigDecimal> balances;
	private EconomyConfig config = CommandPack.getInstance().getMainConfig().getEconomy();
	private AbstractEconomyStorage storage;
	private CPUniqueAccount(){}
	public CPUniqueAccount(UUID userId, Map<Currency, BigDecimal> balances, AbstractEconomyStorage storage) {
		this.userId = userId;
		this.balances = balances;
		this.storage = storage;
		Optional<ServerPlayer> optPlayer = Sponge.server().player(userId);
		if(optPlayer.isPresent()) {
			setName(optPlayer.get().name());
			return;
		}
		Optional<GameProfile> optProfile = Sponge.server().gameProfileManager().cache().findById(userId);
		if(optProfile.isPresent()) {
			setName(optProfile.get().name().orElse(optProfile.get().examinableName()));
			return;
		}
		Sponge.server().userManager().load(userId).thenAccept(optUser -> {
			if(optUser.isPresent()) {
				setName(optUser.get().name());
				return;
			}
		});
		save();
	}

	public static CPUniqueAccount create(UUID userId, String identifier, Map<Currency, BigDecimal> balances, AbstractEconomyStorage storage) {
		CPUniqueAccount uniqueAccount = new CPUniqueAccount();
		uniqueAccount.userId = userId;
		uniqueAccount.displayName = identifier;
		uniqueAccount.balances = balances;
		uniqueAccount.storage = storage;
		return uniqueAccount;
	}

	public static CPUniqueAccount deserealize(SerializedUniqueAccount account, AbstractEconomyStorage storage) {
		CPUniqueAccount uniqueAccount = new CPUniqueAccount();
		uniqueAccount.storage = storage;
		uniqueAccount.userId = account.getUserId();
		uniqueAccount.displayName = account.getName();
		uniqueAccount.balances = account.getBalances(storage.getEconomyService().getCurrenciesMap());
		return uniqueAccount;
	}

	public void setName(String name) {
		this.displayName = name;
		save();
	}

	@Override
	public UUID uniqueId() {
		return userId;
	}

	@Override
	public String identifier() {
		return displayName;
	}

	@Override
	public Component displayName() {
		return text(displayName);
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
			balances.remove(currency);
		} 
		balances.put(currency, amount);
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
				return CPUniqueAccount.this;
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
			balances.remove(currency);
		}
		balances.put(currency, amount);
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
				return CPUniqueAccount.this;
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
				balances.remove(optCurrency.get());
				balances.put(optCurrency.get(), amount);
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
						return CPUniqueAccount.this;
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
				balances.remove(optCurrency.get());
				balances.put(optCurrency.get(), amount);
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
						return CPUniqueAccount.this;
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
			balances.put(currency, BigDecimal.valueOf(optConfig.map(config -> config.getStartingBalance()).orElse(0d)));
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
					return CPUniqueAccount.this;
				}
			};
		}
		sawfowl.commandpack.configure.configs.economy.CurrencyConfig config = optConfig.get();
		TransactionType type = (!contains || balances.get(currency).doubleValue() < config.getStartingBalance() ? TransactionTypes.DEPOSIT : TransactionTypes.WITHDRAW).get();
		BigDecimal amount = BigDecimal.valueOf(config.getStartingBalance());
		if(contains) {
			balances.remove(currency);
		} 
		balances.put(currency, amount);
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
				return CPUniqueAccount.this;
			}
		};
	}

	@Override
	public TransactionResult resetBalance(Currency currency, Cause cause) {
		Optional<sawfowl.commandpack.configure.configs.economy.CurrencyConfig> optConfig = config.getCurrency(currency.displayName());
		boolean contains = balances.containsKey(currency);
		if(!optConfig.isPresent() && contains) {
			balances.remove(currency);
			balances.put(currency, BigDecimal.valueOf(optConfig.map(config -> config.getStartingBalance()).orElse(0d)));
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
					return CPUniqueAccount.this;
				}
			};
		}
		sawfowl.commandpack.configure.configs.economy.CurrencyConfig config = optConfig.get();
		TransactionType type = (!contains || balances.get(currency).doubleValue() < config.getStartingBalance() ? TransactionTypes.DEPOSIT : TransactionTypes.WITHDRAW).get();
		BigDecimal amount = BigDecimal.valueOf(config.getStartingBalance());
		if(contains) {
			balances.remove(currency, amount);
		}
		balances.put(currency, amount);
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
				return CPUniqueAccount.this;
			}
		};
	}

	@Override
	public TransactionResult deposit(Currency currency, BigDecimal amount, Set<Context> contexts) {
		TransactionType type = TransactionTypes.DEPOSIT.get();
		if(balances.containsKey(currency)) {
			if(amount.doubleValue() < 0) type = TransactionTypes.WITHDRAW.get();
			amount = balances.get(currency).add(amount);
			balances.remove(currency);
		}
		balances.put(currency, amount);
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
				return CPUniqueAccount.this;
			}
		};
	}

	@Override
	public TransactionResult deposit(Currency currency, BigDecimal amount, Cause cause) {
		TransactionType type = TransactionTypes.DEPOSIT.get();
		if(balances.containsKey(currency)) {
			if(amount.doubleValue() < 0) type = TransactionTypes.WITHDRAW.get();
			amount = balances.get(currency).add(amount);
			balances.remove(currency);
		}
		balances.put(currency, amount);
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
				return CPUniqueAccount.this;
			}
		};
	}

	@Override
	public TransactionResult withdraw(Currency currency, BigDecimal amount, Set<Context> contexts) {
		TransactionType type = TransactionTypes.WITHDRAW.get();
		if(balances.containsKey(currency)) {
			if(amount.doubleValue() < 0) type = TransactionTypes.DEPOSIT.get();
			amount = balances.get(currency).subtract(amount);
			balances.remove(currency);
		}
		balances.put(currency, amount);
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
				return CPUniqueAccount.this;
			}
		};
	}

	@Override
	public TransactionResult withdraw(Currency currency, BigDecimal amount, Cause cause) {
		TransactionType type = TransactionTypes.WITHDRAW.get();
		if(balances.containsKey(currency)) {
			if(amount.doubleValue() < 0) type = TransactionTypes.DEPOSIT.get();
			amount = balances.get(currency).subtract(amount);
			balances.remove(currency);
		}
		balances.put(currency, amount);
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
				return CPUniqueAccount.this;
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
				return CPUniqueAccount.this;
			}
			
			@Override
			public Account accountTo() {
				return to;
			}
		};
		BigDecimal newValue = balances.get(currency).subtract(amount);
		balances.remove(currency);
		balances.put(currency, newValue);
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
				return CPUniqueAccount.this;
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
				return CPUniqueAccount.this;
			}
			
			@Override
			public Account accountTo() {
				return to;
			}
		};
		BigDecimal newValue = balances.get(currency).subtract(amount);
		balances.remove(currency);
		balances.put(currency, newValue);
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
				return CPUniqueAccount.this;
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

	public CPUniqueAccount setStorage(AbstractEconomyStorage storage) {
		if(this.storage == null) {
			this.storage = storage;
			save();
		}
		return this;
	}

	public void save() {
		if(storage != null) {
			storage.saveUniqueAccount(this);
		}
	}

	@Override
	public String toString() {
		return "CPUniqueAccount [userId=" + userId + ", displayName=" + displayName + ", balances=" + balances + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		return Objects.equals(userId, ((CPUniqueAccount) obj).userId);
	}

}
