package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Item;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementItem implements Item {

	public ImplementItem() {}

	@Setting("SetName")
	private Component setName = TextUtils.deserializeLegacy("&aВы установили новое имя для предмета: &e" + Placeholders.VALUE + "&a.");
	@Setting("ClearName")
	private Component clearName = TextUtils.deserializeLegacy("&aВы восстановили оригинальное название предмета.");
	@Setting("EmptyHand")
	private Component emptyHand = TextUtils.deserializeLegacy("&cВы должны держать предмет в руке.");
	@Setting("SetLore")
	private Component setLore = TextUtils.deserializeLegacy("&aВы изменили описание предмета.");
	@Setting("ClearLore")
	private Component clearLore = TextUtils.deserializeLegacy("&aВы очистили описание предмета.");

	@Override
	public Component getSetName(Component value) {
		return Text.of(setName).replace(Placeholders.VALUE, value).get();
	}

	@Override
	public Component getClearName() {
		return clearName;
	}

	@Override
	public Component getEmptyHand() {
		return emptyHand;
	}

	@Override
	public Component getSetLore() {
		return setLore;
	}

	@Override
	public Component getClearLore() {
		return clearLore;
	}

}
