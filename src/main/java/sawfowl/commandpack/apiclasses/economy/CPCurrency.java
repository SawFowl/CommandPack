package sawfowl.commandpack.apiclasses.economy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.service.economy.Currency;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.configure.configs.economy.EconomyConfig;

public class CPCurrency implements Currency {

	private final EconomyConfig config = CommandPack.getInstance().getMainConfig().getEconomy();
	private ResourceKey key;
	private Component name;
	private Component pluralName;
	private char symbol;
	public CPCurrency() {}
	public CPCurrency(ResourceKey key) {
		this.key = key;
		setSymbol();
		setName();
		setPluralName();
	}

	public ResourceKey getKey() {
		return key;
	}

	@Override
	public Component displayName() {
		return name == null ? setName() : name;
	}

	@Override
	public Component pluralDisplayName() {
		return pluralName == null ? setPluralName() : pluralName;
	}

	@Override
	public Component symbol() {
		return Component.text(symbol);
	}

	@Override
	public Component format(BigDecimal amount, int numFractionDigits) {
		return symbol().append(Component.text(amount.setScale(numFractionDigits < 0 ? defaultFractionDigits() : numFractionDigits, RoundingMode.HALF_UP).doubleValue()));
	}

	@Override
	public int defaultFractionDigits() {
		return 2;
	}

	@Override
	public boolean isDefault() {
		return config.getCurrency(symbol).map(c -> c.isDefault()).orElse(false);
	}

	private char setSymbol() {
		return symbol = config.getCurrency(key).map(c -> c.getSymbol()).orElse('?');
	}

	private Component setName() {
		return name = config.getCurrency(symbol).map(c -> c.displayName()).orElse(symbol());
	}

	private Component setPluralName() {
		return pluralName = config.getCurrency(symbol).map(c -> c.pluralDisplayName()).orElse(symbol());
	}

	@Override
	public int hashCode() {
		return Objects.hash(symbol);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		return symbol == ((CPCurrency) obj).symbol;
	}

}
