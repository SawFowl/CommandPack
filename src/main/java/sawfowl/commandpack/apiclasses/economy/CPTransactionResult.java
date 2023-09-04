package sawfowl.commandpack.apiclasses.economy;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionType;

public class CPTransactionResult implements TransactionResult {

	private final Account account;
	private final Currency currency;
	private final BigDecimal amount;
	private final Set<Context> contexts;
	private final ResultType result;
	private final TransactionType type;
	public CPTransactionResult(Account account, Currency currency, BigDecimal amount, ResultType result, TransactionType type) {
		this.account = account;
		this.currency = currency;
		this.amount = amount;
		this.contexts = new HashSet<Context>();
		this.result = result;
		this.type = type;
		
	}

	@Override
	public Account account() {
		return account;
	}

	@Override
	public Currency currency() {
		return currency;
	}

	@Override
	public BigDecimal amount() {
		return amount;
	}

	@Override
	public Set<Context> contexts() {
		return contexts;
	}

	@Override
	public ResultType result() {
		return result;
	}

	@Override
	public TransactionType type() {
		return type;
	}

}
