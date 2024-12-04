package sawfowl.commandpack.apiclasses.economy.events.change;

import java.math.BigDecimal;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionType;

import sawfowl.commandpack.api.events.ChangeBalanceEvent.Post;
import sawfowl.commandpack.apiclasses.economy.events.ChangeBalanceEventImpl;

public class PostImpl extends ChangeBalanceEventImpl implements Post {

	private final TransactionResult result;
	public PostImpl(Account account, Currency currency, BigDecimal amount, TransactionResult result) {
		super(account, currency, amount);
		this.result = result;
	}

	@Override
	public TransactionResult getResult() {
		return result;
	}

	@Override
	public TransactionType getTransactionType() {
		return result.type();
	}

}
