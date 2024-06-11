package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Repair;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementRepair implements Repair {

	public ImplementRepair() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aРемонт выполнен.");
	@Setting("SuccessOther")
	private Component successOther = TextUtils.deserializeLegacy("&aВаш предмет(ы) был отремонтирован(ы).");
	@Setting("SuccessStaff")
	private Component successStaff = TextUtils.deserializeLegacy("&aВы отремонтировали предмет(ы) игроку &e" + Placeholders.PLAYER + "&a.");

	@Override
	public Component getSuccess() {
		return success;
	}

	@Override
	public Component getSuccessOther() {
		return successOther;
	}

	@Override
	public Component getSuccessStaff(ServerPlayer player) {
		return Text.of(successStaff).replace(Placeholders.PLAYER, player.name()).get();
	}

}
