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
	private Component inventory = TextUtils.deserializeLegacy("&aВаш инвентарь будет сохранен. Шанс сохранить каждый предмет составляет &e" + Placeholders.VALUE + "%&a. Предметы будут возвращены вам после респавна.");
	@Setting("Exp")
	private Component exp = TextUtils.deserializeLegacy("&aВаш опыт будет сохранен в объеме &e" + Placeholders.VALUE + "%&a. Опыт будет возвращен вам после респавна.");

	@Override
	public Component getInventory(double chance) {
		return Text.of(inventory).replace(Placeholders.VALUE, chance).get();
	}

	@Override
	public Component getExp(double size) {
		return Text.of(exp).replace(Placeholders.VALUE, size).get();
	}

}
