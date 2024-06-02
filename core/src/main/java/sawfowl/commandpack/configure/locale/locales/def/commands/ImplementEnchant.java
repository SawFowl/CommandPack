package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Enchant;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementEnchant implements Enchant {

	public ImplementEnchant() {}

	@Setting("ItemNotPresent")
	Component itemNotPresent = TextUtils.deserializeLegacy("&cYou have to hold the item you want to enchant in your hand.");
	@Setting("Success")
	Component success = TextUtils.deserializeLegacy("&aYou have added an enchantment to the item in your hand.");

	@Override
	public Component getItemNotPresent() {
		return itemNotPresent;
	}

	@Override
	public Component getSuccess() {
		return success;
	}

}
