package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Unban;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementUnban implements Unban {

	public ImplementUnban() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aВы разбанили &e" + Placeholders.PLAYER + "&a.");
	@Setting("Announcement")
	private Component announcement = TextUtils.deserializeLegacy("&e" + Placeholders.SOURCE + "&a разбанивает &e" + Placeholders.PLAYER + "&a.");

	@Override
	public Component getSuccess(GameProfile player) {
		return Text.of(success).replace(Placeholders.PLAYER, player.name().orElse(player.examinableName())).get();
	}

	@Override
	public Component getAnnouncement(Component source, GameProfile player) {
		return Text.of(announcement).replace(Placeholders.SOURCE, source).replace(Placeholders.PLAYER, player.name().orElse(player.examinableName())).get();
	}

}
