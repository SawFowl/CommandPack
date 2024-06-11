package sawfowl.commandpack.configure.locale.locales.def.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Economy.Tables;

@ConfigSerializable
public class ImplementTables implements Tables {

	public ImplementTables() {}

	@Setting("UniqueAccounts")
	private String uniqueAccounts = "This table is used to record player balances.";
	@Setting("Accounts")
	private String accounts = "This table is for recording account data without UUIDs.\nIt is suitable for those cases when there is an attempt to change the balance of a player who has not yet logged in to the server.\nAfter the player login in, the data will be resaved with the UUID added.";

	@Override
	public String getUniqueAccounts() {
		return uniqueAccounts;
	}

	@Override
	public String getAccounts() {
		return accounts;
	}

}
