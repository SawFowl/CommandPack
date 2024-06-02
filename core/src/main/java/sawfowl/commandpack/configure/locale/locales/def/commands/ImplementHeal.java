package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Heal;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementHeal implements Heal {

	public ImplementHeal() {}

	@Setting("Seccess")
	private Component success = TextUtils.deserializeLegacy("&aYour heals level is now maximum.");
	@Setting("SeccessStaff")
	private Component successStaff = TextUtils.deserializeLegacy("&aYou healed player &e" + Placeholders.PLAYER + "&a.");

	@Override
	public Component getSuccess() {
		return success;
	}

	@Override
	public Component getSuccessStaff(ServerPlayer player) {
		return Text.of(successStaff).replace(Placeholders.PLAYER, player.name()).get();
	}

}
