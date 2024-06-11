package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Speed;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementSpeed implements Speed {

	public ImplementSpeed() {}

	@Setting("SetSelf")
	private Component setSelf = TextUtils.deserializeLegacy("&aВы установили свой множитель скорости: &e" + Placeholders.VALUE + "&a.");
	@Setting("SetByStaff")
	private Component setByStaff = TextUtils.deserializeLegacy("&aВы установили множитель скорости игрока &e" + Placeholders.PLAYER + "&a: &e" + Placeholders.VALUE + "&a.");
	@Setting("SetOther")
	private Component setOther = TextUtils.deserializeLegacy("&aВам установлен множитель скорости: &e" + Placeholders.VALUE + "&a.");
	@Setting("InFly")
	private Component inFly = TextUtils.deserializeLegacy("\n&aСкорость изменена для режима полета из-за нахождения в воздухе..");
	@Setting("SetDefault")
	private Component setDefault = TextUtils.deserializeLegacy("по умолчанию");

	@Override
	public Component getSetSelf(int value) {
		return Text.of(setSelf).replace(Placeholders.VALUE, value == 1 ? setDefault : Component.text(value)).get();
	}

	@Override
	public Component getSetByStaff(ServerPlayer player, int value) {
		return Text.of(setByStaff).replace(Placeholders.PLAYER, player.name()).replace(Placeholders.VALUE, value == 1 ? setDefault : Component.text(value)).get();
	}

	@Override
	public Component getSetOther(int value) {
		return Text.of(setOther).replace(Placeholders.VALUE, value == 1 ? setDefault : Component.text(value)).get();
	}

	@Override
	public Component getInFly() {
		return inFly;
	}

}
