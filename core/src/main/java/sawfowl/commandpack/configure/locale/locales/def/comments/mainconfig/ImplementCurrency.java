package sawfowl.commandpack.configure.locale.locales.def.comments.mainconfig;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.mainconfig.Economy.Currency;

@ConfigSerializable
public class ImplementCurrency implements Currency {

	public ImplementCurrency() {}

	@Setting("Key")
	private String key = "Currency Registration Key.\nDo not use text decoration or special characters!";
	@Setting("DBCollumn")
	private String dBCollumn = "This option is only used when selecting MySql to store the economy data.\nEach currency must have its own column name to record data correctly.\nCan be used to customize compatibility with Bukkit economy plugin databases.\nUse compatibility at your own risk.\nStable work is not guaranteed.\nDo not use text decoration or special characters other than underscores!";
	@Setting("ID")
	private String iD = "This option is only used when selecting MySql to store the economy data.\nEach currency must have its own id.\nIt is not allowed to change it after creating tables.\nIt is also not allowed to insert new currencies by id between existing ones.";
	@Setting("Default")
	private String defaultCurrency = "The currency used by default.";
	@Setting("TransferPermission")
	private String transferPermission = "If true, the player must have a permission to transfer the currency to someone.\nThis rule applies only to the `/pay` command.";

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getDBCollumn() {
		return dBCollumn;
	}

	@Override
	public String getID() {
		return iD;
	}

	@Override
	public String getDefault() {
		return defaultCurrency;
	}

	@Override
	public String getTransferPermission() {
		return transferPermission;
	}

}
