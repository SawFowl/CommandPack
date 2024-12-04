package sawfowl.commandpack.apiclasses.economy.events.transfer;

import java.math.BigDecimal;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.TransferResult;

import sawfowl.commandpack.api.events.ChangeBalanceEvent.Transfer.Post;
import sawfowl.commandpack.apiclasses.economy.events.TransferEventImp;

public class PostImpl extends TransferEventImp implements Post {

	private final TransferResult result;
	public PostImpl(Account account, Account other, Currency currency, BigDecimal amount, TransferResult result) {
		super(account, other, currency, amount);
		this.result = result;
	}

	@Override
	public TransferResult getResult() {
		return result;
	}

}
