package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Feed;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementFeed implements Feed {

	public ImplementFeed() {}

	@Setting("Seccess")
	private Component success = TextUtils.deserializeLegacy("&aYour food level is now maximum.");
	@Setting("SeccessStaff")
	private Component successStaff = TextUtils.deserializeLegacy("&aYou saturated the player &e" + Placeholders.PLAYER + "&a.");

	@Override
	public Component getSuccess() {
		return success;
	}

	@Override
	public Component getSuccessStaff(ServerPlayer player) {
		return Text.of(successStaff).replace(Placeholders.PLAYER, player.name()).get();
	}

}
