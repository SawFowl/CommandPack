package sawfowl.commandpack.apiclasses.economy.events.transfer;

import java.math.BigDecimal;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;

import sawfowl.commandpack.api.events.ChangeBalanceEvent.Transfer.Pre;
import sawfowl.commandpack.apiclasses.economy.events.TransferEventImp;

public class PreImpl extends TransferEventImp implements Pre {

	private boolean cancel = false;
	public PreImpl(Account account, Account other, Currency currency, BigDecimal amount) {
		super(account, other, currency, amount);
	}

	@Override
	public boolean cancel() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

}
