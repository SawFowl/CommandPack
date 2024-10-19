package sawfowl.commandpack.configure.configs.economy;

import java.util.Objects;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.localeapi.api.LocalisedComment;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class CurrencyConfig {

	public CurrencyConfig(){}

	@Setting("Name")
	private String name = "&2Dollar";
	@Setting("PluralName")
	private String pluralName = "&2Dollars";
	@Setting("Key")
	@LocalisedComment(path = {"Comments", "MainConfig", "Economy", "Currency", "Key"}, plugin = "commandpack")
	private String key = "dollar";
	@Setting("DBCollumn")
	@LocalisedComment(path = {"Comments", "MainConfig", "Economy", "Currency", "DBCollumn"}, plugin = "commandpack")
	private String dbCollumn = "dollars";
	@Setting("ID")
	@LocalisedComment(path = {"Comments", "MainConfig", "Economy", "Currency", "ID"}, plugin = "commandpack")
	private int dbID = 0;
	@Setting("Symbol")
	private char symbol = '$';
	@Setting("StartingBalance")
	private double startingBalance = 100d;
	@Setting("Default")
	private boolean isDefault = true;
	@Setting("TransferPermission")
	@LocalisedComment(path = {"Comments", "MainConfig", "Economy", "Currency", "TransferPermission"}, plugin = "commandpack")
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

	public String getKey() {
		return "cpcurrency:" + key.toLowerCase();
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
