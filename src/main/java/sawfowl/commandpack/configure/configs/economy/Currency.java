package sawfowl.commandpack.configure.configs.economy;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class Currency {

	public Currency(){}

	@Setting("Name")
	private String name = "Dollar";
	@Setting("PluralName")
	private String pluralName = "Dollars";
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

}
