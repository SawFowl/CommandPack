package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Kick;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementKick implements Kick {

	public ImplementKick() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aВы кикнули &e" + Placeholders.PLAYER + "&a.");
	@Setting("Disconnect")
	private Component disconnect = TextUtils.deserializeLegacy("&e" + Placeholders.SOURCE + "&b выгоняет вас с сервера.\n&bПричина: &e" + Placeholders.VALUE + ".");
	@Setting("Announcement")
	private Component announcement = TextUtils.deserializeLegacy("&e" + Placeholders.SOURCE + "&a выгоняет с сервера  " + Placeholders.PLAYER + ".\n&aПричина: &e" + Placeholders.VALUE + "&a.");
	@Setting("Ignore")
	private Component ignore = TextUtils.deserializeLegacy("&cВы не можете кикнуть &e" + Placeholders.PLAYER + "&c.");

	@Override
	public Component getSuccess(String player) {
		return Text.of(success).replace(Placeholders.PLAYER, player).get();
	}

	@Override
	public Component getDisconnect(Component source, Component reason) {
		return Text.of(disconnect).replace(Placeholders.SOURCE, source).replace(Placeholders.VALUE, reason).get();
	}

	@Override
	public Component getAnnouncement(Component source, String player, Component reason) {
		return Text.of(announcement).replace(Placeholders.SOURCE, source).replace(Placeholders.PLAYER, player).replace(Placeholders.VALUE, reason).get();
	}

	@Override
	public Component getIgnore(String player) {
		return Text.of(ignore).replace(Placeholders.PLAYER, player).get();
	}

}
