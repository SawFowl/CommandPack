package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.HideBalance;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementHideBalance implements HideBalance {

	public ImplementHideBalance() {}

	@Setting("Hide")
	private Component hide = TextUtils.deserializeLegacy("&aВаш баланс скрыт от других игроков.");
	@Setting("Open")
	private Component open = TextUtils.deserializeLegacy("&aДругие игроки снова могут видеть ваш баланс.");
	@Setting("HideStaff")
	private Component hideStaff = TextUtils.deserializeLegacy("&aБаланс игрока &e" + Placeholders.PLAYER + "&a теперь скрыт от других игроков");
	@Setting("OpenStaff")
	private Component openStaff = TextUtils.deserializeLegacy("&aБаланс игрока &e" + Placeholders.PLAYER + "&a снова виден другим игрокам");

	@Override
	public Component getHide() {
		return hide;
	}

	@Override
	public Component getOpen() {
		return open;
	}

	@Override
	public Component getHideStaff(Component player) {
		return Text.of(hideStaff).replace(Placeholders.PLAYER, player).get();
	}

	@Override
	public Component getOpenStaff(Component player) {
		return Text.of(openStaff).replace(Placeholders.PLAYER, player).get();
	}

}
