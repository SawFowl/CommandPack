package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Glow;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementGlow implements Glow {

	public ImplementGlow() {}

	@Setting("Enable")
	private Component enable = TextUtils.deserializeLegacy("&aВы сияете.");
	@Setting("EnableStaff")
	private Component enableStaff = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + "&a сияет.");
	@Setting("Disable")
	private Component disable = TextUtils.deserializeLegacy("&aВы больше не сияете.");
	@Setting("DisableStaff")
	private Component disableStaff = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + "&a больше не сияет.");

	@Override
	public Component getEnable() {
		return enable;
	}

	@Override
	public Component getEnableStaff(Entity target, boolean isPlayer) {
		return Text.of(enableStaff).replace(Placeholders.PLAYER, isPlayer ? ((ServerPlayer) target).name() : EntityTypes.registry().valueKey(target.type()).asString()).get();
	}

	@Override
	public Component getDisable() {
		return disable;
	}

	@Override
	public Component getDisableStaff(Entity target, boolean isPlayer) {
		return Text.of(disableStaff).replace(Placeholders.PLAYER, isPlayer ? ((ServerPlayer) target).name() : EntityTypes.registry().valueKey(target.type()).asString()).get();
	}

}
