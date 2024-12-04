package sawfowl.commandpack.api.events;

import java.math.BigDecimal;
import java.util.Optional;

import org.apache.http.concurrent.Cancellable;

import org.spongepowered.api.event.Event;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionType;
import org.spongepowered.api.service.economy.transaction.TransactionTypes;
import org.spongepowered.api.service.economy.transaction.TransferResult;

/**
 * An event triggered when the account balance changes.
 */
public interface ChangeBalanceEvent extends Event {

	/**
	 * Targeted Account.
	 */
	Account getAccount();

	/**
	 * Targeted Account.<br>
	 * Will only be available if the target account is related to the player.
	 */
	Optional<UniqueAccount> getUniqueAccount();

	/**
	 * The currency for which the balance change is made.
	 */
	Currency getCurrency();

	/**
	 * Amount of change.
	 */
	BigDecimal getAmount();

	/**
	 * See {@link TransactionType}
	 */
	TransactionType getTransactionType();

	/**
	 * This event is triggered when you transfer funds from one account to another.
	 */
	interface Transfer extends ChangeBalanceEvent {

		/**
		 * Second target account.
		 */
		Account getOtherAccount();

		/**
		 * Second target account.<br>
		 * Will only be available if the target account is related to the player.
		 */
		Optional<UniqueAccount> getOtherUniqueAccount();

		@Override
		default TransactionType getTransactionType() {
			return TransactionTypes.TRANSFER.get();
		}

		interface Pre extends Transfer, Cancellable {

			/**
			 * If true, the balance change event will be canceled.
			 */
			public void setCancelled(boolean cancel);

		}

		interface Post extends Transfer {

			/**
			 * See {@link TransferResult}
			 */
			TransferResult getResult();

		}

	}

	interface Pre extends ChangeBalanceEvent, Cancellable {

		/**
		 * If true, the balance change event will be canceled.
		 */
		void setCancelled(boolean cancel);

	}

	interface Post extends ChangeBalanceEvent {

		/**
		 * See {@link TransactionResult}
		 */
		TransactionResult getResult();

	}

}
