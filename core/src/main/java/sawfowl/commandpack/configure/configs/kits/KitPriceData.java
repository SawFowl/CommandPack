package sawfowl.commandpack.configure.configs.kits;

import java.math.BigDecimal;

import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.data.kits.KitPrice;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class KitPriceData implements KitPrice {

	public KitPriceData(){}

	@Setting("Currency")
	private String currency;
	@Setting("Money")
	private double money;

	public Builder builder() {
		return new Builder();
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

	@Override
	public Currency getCurrency() {
		return CommandPackInstance.getInstance().getEconomy().checkCurrency(currency);
	}

	@Override
	public BigDecimal getMoney() {
		return BigDecimal.valueOf(money);
	}

	@Override
	public Component asComponent() {
		return getCurrency().symbol().append(Component.text(money));
	}

	class Builder implements KitPrice.Builder {

		@Override
		public KitPrice build() {
			return KitPriceData.this;
		}

		@Override
		public KitPrice of(Currency currency, BigDecimal money) {
			KitPriceData.this.currency = TextUtils.clearDecorations(currency.symbol());
			KitPriceData.this.money = money.doubleValue();
			return KitPriceData.this;
		}
		
	}

}
