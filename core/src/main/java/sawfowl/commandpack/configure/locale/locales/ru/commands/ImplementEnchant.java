package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Enchant;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementEnchant implements Enchant {

	public ImplementEnchant() {}

	@Setting("ItemNotPresent")
	Component itemNotPresent = TextUtils.deserializeLegacy("&cВы должны держать в руке предмет, который хотите зачаровать.");
	@Setting("Success")
	Component success = TextUtils.deserializeLegacy("&aВы добавили зачарование к предмету в вашей руке.");

	@Override
	public Component getItemNotPresent() {
		return itemNotPresent;
	}

	@Override
	public Component getSuccess() {
		return success;
	}

}
