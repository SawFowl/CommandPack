package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Unmute;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementUnmute implements Unmute {

	public ImplementUnmute() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aВы сняли мут с &e" + Placeholders.PLAYER + "&a.");
	@Setting("SuccessTarget")
	private Component successTarget = TextUtils.deserializeLegacy("&aВы снова можете писать в чат.");
	@Setting("Announcement")
	private Component announcement = TextUtils.deserializeLegacy("&e" + Placeholders.SOURCE + "&a снимает мут с &e" + Placeholders.PLAYER + "&a.");

	@Override
	public Component getSuccess(String player) {
		return Text.of(success).replace(Placeholders.PLAYER, player).get();
	}

	@Override
	public Component getSuccessTarget() {
		return Text.of(successTarget).get();
	}

	@Override
	public Component getAnnouncement(Component source, String player) {
		return Text.of(announcement).replace(Placeholders.SOURCE, source).replace(Placeholders.PLAYER, player).get();
	}

}
