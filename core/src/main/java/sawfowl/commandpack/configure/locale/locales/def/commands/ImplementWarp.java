package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractclasses.commands.Warp;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementWarp implements Warp {

	public ImplementWarp() {}

	@Setting("NotFound")
	Component notFound = TextUtils.deserializeLegacy("&cWarp with the specified name was not found.");
	@Setting("Success")
	Component success = TextUtils.deserializeLegacy("&aYou have moved to warp " + Placeholders.WARP + "&a.");
	@Setting("SuccessOther")
	Component successOther = TextUtils.deserializeLegacy("&aYou've been transferred to warp " + Placeholders.WARP + "&a.");
	@Setting("SuccessStaff")
	Component successStaff = TextUtils.deserializeLegacy("&aYou moved &e" + Placeholders.PLAYER + " to warp " + Placeholders.WARP + "&a.");

	@Override
	public Component getNotFound() {
		return notFound;
	}

	@Override
	public Component getSuccess(Component warp) {
		return Text.of(success).replace(Placeholders.WARP, warp).get();
	}

	@Override
	public Component getSuccessOther(Component warp) {
		return Text.of(successOther).replace(Placeholders.WARP, warp).get();
	}

	@Override
	public Component getSuccessStaff(ServerPlayer player, Component warp) {
		return Text.of(successStaff).replace(Placeholders.PLAYER, player.name()).replace(Placeholders.WARP, warp).get();
	}

}
