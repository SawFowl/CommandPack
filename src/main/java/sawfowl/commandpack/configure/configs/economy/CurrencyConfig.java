package sawfowl.commandpack.configure.configs.economy;

import java.util.Objects;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class CurrencyConfig {

	public CurrencyConfig(){}

	@Setting("Name")
	private String name = "Dollar";
	@Setting("PluralName")
	private String pluralName = "Dollars";
	@Setting("DBCollumn")
	@Comment("This option is only used when selecting MySql to store the economy data.\nEach currency must have its own column name to record data correctly.\nCan be used to customize compatibility with Bukkit economy plugin databases.\nUse compatibility at your own risk.\nStable work is not guaranteed.")
	private String dbCollumn = "dollars";
	@Setting("ID")
	@Comment("This option is only used when selecting MySql to store the economy data.\nEach currency must have its own id.\nIt is not allowed to change it after creating tables.\nIt is also not allowed to insert new currencies by id between existing ones.")
	private int dbID = 0;
	@Setting("Symbol")
	private char symbol = '$';
	@Setting("StartingBalance")
	private double startingBalance = 100d;
	@Setting("Default")
	private boolean isDefault = true;
	@Setting("TransferPermission")
	@Comment("If true, the player must have a permission to transfer the currency to someone.\nThis rule applies only to the `/pay` command.")
	private boolean transferPerm = false;

	public String getName() {
		return name;
	}

	public Component displayName() {
		return text(name);
	}

	public String getPluralName() {
		return pluralName;
	}

	public Component pluralDisplayName() {
		return text(pluralName);
	}

	public char getSymbol() {
		return symbol;
	}

	public double getStartingBalance() {
		return startingBalance;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public boolean isTransferPerm() {
		return transferPerm;
	}

	private Component text(String string) {
		if(isLegacyDecor(string)) {
			return TextUtils.deserializeLegacy(string);
		} else {
			return TextUtils.deserialize(string);
		}
	}

	private boolean isLegacyDecor(String string) {
		return string.indexOf('&') != -1 && !string.endsWith("&") && isStyleChar(string.charAt(string.indexOf("&") + 1));
	}

	private boolean isStyleChar(char ch) {
		return "0123456789abcdefklmnor".indexOf(ch) != -1;
	}

	@Override
	public String toString() {
		return "CurrencyConfig [name=" + name + ", pluralName=" + pluralName + ", symbol=" + symbol
				+ ", startingBalance=" + startingBalance + ", isDefault=" + isDefault + ", transferPerm=" + transferPerm
				+ "]";
	}

	public String getCollumn() {
		return dbCollumn;
	}

	public int getID() {
		return dbID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dbCollumn, dbID, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		CurrencyConfig other = (CurrencyConfig) obj;
		return Objects.equals(dbCollumn, other.dbCollumn) && dbID == other.dbID && Objects.equals(name, other.name);
	}

}
