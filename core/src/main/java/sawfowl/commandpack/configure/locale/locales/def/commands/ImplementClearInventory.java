package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractclasses.commands.ClearInventory;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementClearInventory implements ClearInventory {

	public ImplementClearInventory() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aYou have cleared your inventory.");
	@Setting("SuccessOther")
	private Component successOther = TextUtils.deserializeLegacy("&eYour inventory has been cleared.");
	@Setting("SuccessStaff")
	private Component successStaff = TextUtils.deserializeLegacy("&aYou cleared &e" + Placeholders.PLAYER + "&a inventory.");

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
