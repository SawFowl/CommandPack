package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Extinguish;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementExtinguish implements Extinguish {

	public ImplementExtinguish() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aОгонь потушен.");
	@Setting("SuccessStaff")
	private Component successStaff = TextUtils.deserializeLegacy("&e" + Placeholders.PLAYER + "&a потушен.");

	@Override
	public Component getSuccess() {
		return success;
	}

	@Override
	public Component getSuccessStaff(Entity target, boolean isPlayer) {
		return Text.of(successStaff).replace(Placeholders.PLAYER, isPlayer ? ((ServerPlayer) target).name() : EntityTypes.registry().valueKey(target.type()).asString()).get();
	}

}
