package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractclasses.commands.GodMode;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementGodMode implements GodMode {

	public ImplementGodMode() {}

	@Setting("Enable")
	private Component enable = TextUtils.deserializeLegacy("&aNow you are invulnerable.");
	@Setting("EnableStaff")
	private Component enableStaff = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + "&a now has invulnerability.");
	@Setting("Disable")
	private Component disable = TextUtils.deserializeLegacy("&aInvulnerability is disabled.");
	@Setting("DisableStaff")
	private Component disableStaff = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + "&a no longer has invulnerability.");

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
