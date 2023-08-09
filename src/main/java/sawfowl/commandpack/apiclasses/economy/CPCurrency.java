package sawfowl.commandpack.apiclasses.economy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.configure.configs.economy.EconomyConfig;

@ConfigSerializable
public class CPCurrency implements Currency {

	private final EconomyConfig config = CommandPack.getInstance().getMainConfig().getEconomy();
	public CPCurrency() {}
	public CPCurrency(char symbol) {
		this.symbol = symbol;
	}

	@Setting("Symbol")
	private char symbol;

	@Override
	public Component displayName() {
		return config.getCurrency(symbol).map(c -> c.displayName()).orElse(symbol());
	}

	@Override
	public Component pluralDisplayName() {
		return config.getCurrency(symbol).map(c -> c.pluralDisplayName()).orElse(symbol());
	}

	@Override
	public Component symbol() {
		return Component.text(symbol);
	}

	@Override
	public Component format(BigDecimal amount, int numFractionDigits) {
		return symbol().append(Component.text(amount.setScale(numFractionDigits, RoundingMode.HALF_UP).doubleValue()));
	}

	@Override
	public int defaultFractionDigits() {
		return 2;
	}

	@Override
	public boolean isDefault() {
		return config.getCurrency(symbol).map(c -> c.isDefault()).orElse(false);
	}

}
