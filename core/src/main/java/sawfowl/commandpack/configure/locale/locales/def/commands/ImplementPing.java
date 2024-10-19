package sawfowl.commandpack.configure.locale.locales.def.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Ping;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementPing implements Ping {

	public ImplementPing() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aPing&f: &e" + Placeholders.VALUE);
	@Setting("SuccessStaff")
	private Component successStaff = TextUtils.deserializeLegacy("&aPing of player &e" + Placeholders.PLAYER + "&f: &e" + Placeholders.VALUE);

	@Override
	public Component getSuccess(long ping) {
		return Text.of(success).replace(Placeholders.VALUE, ping).get();
	}

	@Override
	public Component getSuccessStaff(ServerPlayer player, long ping) {
		return Text.of(successStaff).replace(Placeholders.PLAYER, player.name()).replace(Placeholders.VALUE, ping).get();
	}

}
