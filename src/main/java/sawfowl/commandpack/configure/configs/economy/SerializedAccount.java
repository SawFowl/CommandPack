package sawfowl.commandpack.configure.configs.economy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.apiclasses.economy.CPAccount;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class SerializedAccount {

	public SerializedAccount(){}
	public SerializedAccount(CPAccount account) {
		name = account.identifier();
		account.balances().forEach((k, v) -> {
			balances.put(TextUtils.clearDecorations(k.symbol()).charAt(0), v.doubleValue());
		});		
	}

	@Setting("Name")
	private String name = "n/a";
	@Setting("Balances")
	private Map<Character, Double> balances = new HashMap<>();

	public String getName() {
		return name;
	}

	public Map<Currency, BigDecimal> getBalances(Map<Character, Currency> currenciesMap) {
		Map<Currency, BigDecimal> accountBalances = new HashMap<Currency, BigDecimal>();
		if(!balances.isEmpty()) balances.forEach((k, v) -> {
			if(currenciesMap.containsKey(k)) accountBalances.put(currenciesMap.get(k), BigDecimal.valueOf(v));
		});
		return accountBalances;
	}

}
