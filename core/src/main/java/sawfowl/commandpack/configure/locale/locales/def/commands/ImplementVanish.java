package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Vanish;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementVanish implements Vanish {

	public ImplementVanish() {}

	@Setting("Enable")
	private Component enable = TextUtils.deserializeLegacy("&aYou are now invisible.");
	@Setting("EnableStaff")
	private Component enableStaff = TextUtils.deserializeLegacy("&aThe player &e" + Placeholders.PLAYER + "&a is now invisible.");
	@Setting("Disable")
	private Component disable = TextUtils.deserializeLegacy("&aYou are visible again.");
	@Setting("DisableStaff")
	private Component disableStaff = TextUtils.deserializeLegacy("&aThe player &e" + Placeholders.PLAYER + "&a is visible again.");

	@Override
	public Component getEnable() {
		return enable;
	}

	@Override
	public Component getEnableStaff(ServerPlayer player) {
		return Text.of(enableStaff).replace(Placeholders.PLAYER, player.name()).get();
	}

	@Override
	public Component getDisable() {
		return disable;
	}

	@Override
	public Component getDisableStaff(ServerPlayer player) {
		return Text.of(disableStaff).replace(Placeholders.PLAYER, player.name()).get();
	}

}
