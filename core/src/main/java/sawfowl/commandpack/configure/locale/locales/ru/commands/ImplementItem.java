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
	private Component setName = TextUtils.deserializeLegacy("&aYou have established a new name for the item: &e" + Placeholders.VALUE + "&a.");
	@Setting("ClearName")
	private Component clearName = TextUtils.deserializeLegacy("&aYou have restored the original name of the item.");
	@Setting("EmptyHand")
	private Component emptyHand = TextUtils.deserializeLegacy("&cYou have to hold the item in your hand.");
	@Setting("SetLore")
	private Component setLore = TextUtils.deserializeLegacy("&aYou changed the lore of the item.");
	@Setting("ClearLore")
	private Component clearLore = TextUtils.deserializeLegacy("&aYou have cleared the item lore.");

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
