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
	private Component success = TextUtils.deserializeLegacy("&aВы горите.");
	@Setting("Damage")
	private Component damage = TextUtils.deserializeLegacy("&4Вы горите!");
	@Setting("SuccessStaff")
	private Component successStaff = TextUtils.deserializeLegacy("&aВы подожгли &e" + Placeholders.PLAYER + "&a.");

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
