package sawfowl.commandpack.apiclasses.economy;

import java.math.BigDecimal;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionType;
import org.spongepowered.api.service.economy.transaction.TransferResult;

public class CPTransferResult extends CPTransactionResult implements TransferResult {

	private Account to;
	public CPTransferResult(Account account, Account to, Currency currency, BigDecimal amount, ResultType result, TransactionType type) {
		super(account, currency, amount, result, type);
		this.to = to;
	}

	@Override
	public Account accountTo() {
		return to;
	}

}
