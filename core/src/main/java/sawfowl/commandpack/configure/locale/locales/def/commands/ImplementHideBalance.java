package sawfowl.commandpack.configure.locale.locales.def.commands;

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
	private Component hide = TextUtils.deserializeLegacy("&aYour balance is hidden from other players.");
	@Setting("Open")
	private Component open = TextUtils.deserializeLegacy("&aOther players can once again view your balance.");
	@Setting("HideStaff")
	private Component hideStaff = TextUtils.deserializeLegacy("&aThe balance of the player &e" + Placeholders.PLAYER + "&a is hidden from other players.");
	@Setting("OpenStaff")
	private Component openStaff = TextUtils.deserializeLegacy("&aPlayer balance &e" + Placeholders.PLAYER + "&a is once again available for other players to view.");

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
