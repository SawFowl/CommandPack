package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Fly;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementFly implements Fly {

	public ImplementFly() {}

	@Setting("Enable")
	private Component enable = TextUtils.deserializeLegacy("&aNow you can fly.");
	@Setting("EnableStaff")
	private Component enableStaff = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + "&a can now fly.");
	@Setting("Disable")
	private Component disable = TextUtils.deserializeLegacy("&aFlight disabled.");
	@Setting("DisableStaff")
	private Component disableStaff = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + "&a can't fly anymore.");

	@Override
	public Component getEnable() {
		return enable;
	}

	@Override
	public Component getEnableStaff(ServerPlayer player) {
		return Text.of(enableStaff).replace(Placeholders.PLAYER, player.name()).get();
	}

	@Override
	public Component getDisabe() {
		return disable;
	}

	@Override
	public Component getDisabeStaff(ServerPlayer player) {
		return Text.of(disableStaff).replace(Placeholders.PLAYER, player.name()).get();
	}

}
