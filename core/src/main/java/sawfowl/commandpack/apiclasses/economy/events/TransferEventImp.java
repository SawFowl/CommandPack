package sawfowl.commandpack.apiclasses.economy.events;

import java.math.BigDecimal;
import java.util.Optional;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import sawfowl.commandpack.api.events.ChangeBalanceEvent.Transfer;

public class TransferEventImp extends ChangeBalanceEventImpl implements Transfer {

	private final Account other;
	private final Optional<UniqueAccount> otherUniqueAccount;
	public TransferEventImp(Account account, Account other, Currency currency, BigDecimal amount) {
		super(account, currency, amount);
		this.other = other;
		this.otherUniqueAccount = Optional.ofNullable(other instanceof UniqueAccount uniqueAccount ? uniqueAccount : null);
	}

	@Override
	public Account getOtherAccount() {
		return other;
	}

	@Override
	public Optional<UniqueAccount> getOtherUniqueAccount() {
		return otherUniqueAccount;
	}

}
