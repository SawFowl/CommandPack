package sawfowl.commandpack.configure.configs.economy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.configs.economy.dbsettings.DBSettings;
import sawfowl.commandpack.utils.StorageType;
import sawfowl.localeapi.api.LocalisedComment;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class EconomyConfig {

	public EconomyConfig(){}

	@Setting("Enable")
	private boolean enable = false;
	@Setting("Currencies")
	private List<CurrencyConfig> currencies = new ArrayList<CurrencyConfig>(Arrays.asList(new CurrencyConfig()));
	@Setting("StorageType")
	@LocalisedComment(path = {"Comments", "MainConfig", "Economy", "StorageType"}, plugin = "commandpack")
	private String storageType = StorageType.FILE.typeName();
	@Setting("UpdateInterval")
	@LocalisedComment(path = {"Comments", "MainConfig", "Economy", "UpdateInterval"}, plugin = "commandpack")
	private long updateInterval = 500;
	@Setting("DBSettings")
	@LocalisedComment(path = {"Comments", "MainConfig", "Economy", "DBSettings"}, plugin = "commandpack")
	private DBSettings dbSettings = new DBSettings();
	@Setting("AdditionalChecks")
	@LocalisedComment(path = {"Comments", "MainConfig", "Economy", "AdditionalChecks"}, plugin = "commandpack")
	private boolean additionalChecks = true;

	public boolean isEnable() {
		return enable;
	}

	public List<CurrencyConfig> getCurrencies() {
		return currencies;
	}

	public Optional<CurrencyConfig> getCurrency(char symbol) {
		return currencies.stream().filter(c -> c.getSymbol() == symbol).findFirst();
	}

	public Optional<CurrencyConfig> getCurrency(ResourceKey key) {
		return currencies.stream().filter(c -> key.asString().equals(c.getKey())).findFirst();
	}

	public Optional<CurrencyConfig> getCurrency(String name) {
		return currencies.stream().filter(c -> c.getName().equals(name) || (name.length() == 1 && name.charAt(0) == c.getSymbol())).findFirst();
	}

	public Optional<CurrencyConfig> getCurrency(Component displayName) {
		return getCurrency(TextUtils.serializeLegacy(displayName));
	}

	public CurrencyConfig getDefaultCurrency() {
		if(currencies.isEmpty()) currencies.add(new CurrencyConfig());
		return currencies.stream().filter(c -> c.isDefault()).findFirst().orElse(currencies.get(0));
	}

	public StorageType getStorageType() {
		return StorageType.getType(storageType);
	}

	public long getUpdateInterval() {
		return updateInterval;
	}

	public DBSettings getDBSettings() {
		return dbSettings;
	}

	public boolean isAdditionalChecks() {
		return additionalChecks;
	}

}
