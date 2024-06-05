package sawfowl.commandpack.configure.locale.locales.ru.other;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Other.Keep;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementKeep implements Keep {

	public ImplementKeep() {}

	@Setting("Inventory")
	private Component inventory = TextUtils.deserializeLegacy("&aYour inventory will be saved. The chance of saving each item is &e" + Placeholders.VALUE + "%&a. Items will be returned to you after respawn.");
	@Setting("Exp")
	private Component exp = TextUtils.deserializeLegacy("&aYour experience will be saved to the extent of &e" + Placeholders.VALUE + "%&a. Experience will be returned to you after respawn.");

	@Override
	public Component getInventory(double chance) {
		return Text.of(inventory).replace(Placeholders.VALUE, chance).get();
	}

	@Override
	public Component getExp(double size) {
		return Text.of(exp).replace(Placeholders.VALUE, size).get();
	}

}
