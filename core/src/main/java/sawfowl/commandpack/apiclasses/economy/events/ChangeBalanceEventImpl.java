package sawfowl.commandpack.apiclasses.economy.events;

import java.math.BigDecimal;
import java.util.Optional;

import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.events.ChangeBalanceEvent;

public abstract class ChangeBalanceEventImpl implements ChangeBalanceEvent {

	private static final Cause cause = Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, CommandPackInstance.getInstance().getPluginContainer()).build(), CommandPackInstance.getInstance().getPluginContainer());
	private final Account account;
	private final Optional<UniqueAccount> uniqueAccount;
	private final Currency currency;
	private final BigDecimal amount;
	public ChangeBalanceEventImpl(Account account, Currency currency, BigDecimal amount) {
		this.account = account;
		this.uniqueAccount = Optional.ofNullable(account instanceof UniqueAccount uniqueAccount ? uniqueAccount : null);
		this.currency = currency;
		this.amount = amount;
	}

	@Override
	public Cause cause() {
		return cause;
	}

	@Override
	public Account getAccount() {
		return account;
	}

	@Override
	public Optional<UniqueAccount> getUniqueAccount() {
		return uniqueAccount;
	}

	@Override
	public Currency getCurrency() {
		return currency;
	}

	@Override
	public BigDecimal getAmount() {
		return amount;
	}

}
