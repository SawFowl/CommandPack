package sawfowl.commandpack.configure.configs.commands;

import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.localeapi.api.LocalisedComment;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class CommandPrice implements sawfowl.commandpack.api.data.command.Price {

	public CommandPrice() {}

	public static CommandPrice of(String currency, double money) {
		return new CommandPrice().builder().currency(currency).money(money).build();
	}

	public CommandPrice(String currency, double money) {
		this.currency = currency;
		this.money = money;
	}

	public Builder builder() {
		return new Builder();
	}

	@Setting("Currency")
	@LocalisedComment(path = {"Comments", "CommandsConfig", "Price", "Currency"}, plugin = "commandpack")
	private String currency = "$";
	@Setting("Money")
	@LocalisedComment(path = {"Comments", "CommandsConfig", "Price", "Money"}, plugin = "commandpack")
	private double money = 0;

	@Override
	public String getCurrency() {
		return currency;
	}

	@Override
	public double getMoney() {
		return money;
	}

	@Override
	public int contentVersion() {
		return 1;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(DataQuery.of("Currency"), currency)
				.set(DataQuery.of("Money"), money)
				.set(Queries.CONTENT_VERSION, contentVersion());
	}

	public class Builder implements sawfowl.commandpack.api.data.command.Price.Builder {

		@Override
		public CommandPrice build() {
			return CommandPrice.this;
		}

		@Override
		public Builder currency(Currency currency) {
			CommandPrice.this.currency = TextUtils.clearDecorations(currency.symbol());
			return this;
		}

		@Override
		public Builder currency(String currency) {
			CommandPrice.this.currency = currency;
			return this;
		}

		@Override
		public Builder money(double money) {
			CommandPrice.this.money = money;
			return this;
		}
		
	}

}
