package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Flame;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementFlame implements Flame {

	public ImplementFlame() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aYou are burning.");
	@Setting("Damage")
	private Component damage = TextUtils.deserializeLegacy("&4You are burning!");
	@Setting("SuccessStaff")
	private Component successStaff = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + "&a is now burning.");

	@Override
	public Component getSuccess() {
		return success;
	}

	@Override
	public Component getDamage() {
		return damage;
	}

	@Override
	public Component getSuccessStaff(Entity target, boolean isPlayer) {
		return Text.of(successStaff).replace(Placeholders.PLAYER, isPlayer ? Component.text(((ServerPlayer) target).name()) : target.asHoverEvent().value().name()).get();
	}

}
