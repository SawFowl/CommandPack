package sawfowl.commandpack.configure.configs.economy.dbsettings;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Tables {

	public Tables(){}

	@Setting("UniqueAccounts")
	@Comment("This table is used to record player balances.")
	private String uniqueAccounts = "cp_economy_unique_accounts";
	@Setting("Accounts")
	@Comment("This table is for recording account data without UUIDs. It is suitable for those cases when there is an attempt to change the balance of a player who has not yet logged in to the server. After the player login in, the data will be resaved with the UUID added.")
	private String accounts = "cp_economy_accounts";

	public String getUniqueAccounts() {
		return uniqueAccounts;
	}

	public String getAccounts() {
		return accounts;
	}

}
