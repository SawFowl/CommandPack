package sawfowl.commandpack.apiclasses.economy.events.change;

import java.math.BigDecimal;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.TransactionType;

import sawfowl.commandpack.api.events.ChangeBalanceEvent.Pre;
import sawfowl.commandpack.apiclasses.economy.events.ChangeBalanceEventImpl;

public class PreImpl extends ChangeBalanceEventImpl implements Pre {

	private boolean cancel = false;
	private final TransactionType transactionType;
	public PreImpl(Account account, Currency currency, BigDecimal amount, TransactionType transactionType) {
		super(account, currency, amount);
		this.transactionType = transactionType;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public TransactionType getTransactionType() {
		return transactionType;
	}

}
