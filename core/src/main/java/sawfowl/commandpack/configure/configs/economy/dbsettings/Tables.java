package sawfowl.commandpack.configure.configs.economy.dbsettings;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.localeapi.api.LocalisedComment;

@ConfigSerializable
public class Tables {

	public Tables(){}

	@Setting("UniqueAccounts")
	@LocalisedComment(path = {"Comments", "MainConfig", "Economy", "Tables", "UniqueAccounts"}, plugin = "commandpack")
	private String uniqueAccounts = "cp_economy_unique_accounts";
	@Setting("Accounts")
	@LocalisedComment(path = {"Comments", "MainConfig", "Economy", "Tables", "Accounts"}, plugin = "commandpack")
	private String accounts = "cp_economy_accounts";

	public String getUniqueAccounts() {
		return uniqueAccounts;
	}

	public String getAccounts() {
		return accounts;
	}

}
