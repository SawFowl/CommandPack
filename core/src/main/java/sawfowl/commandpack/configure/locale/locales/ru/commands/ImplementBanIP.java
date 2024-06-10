package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.BanIP;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementBanIP implements BanIP {

	public ImplementBanIP() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aВы забанили &e" + Placeholders.PLAYER + "&a по IP &e" + Placeholders.VALUE + "&a.");
	@Setting("Announcement")
	private Component announcement = TextUtils.deserializeLegacy("&e" + Placeholders.SOURCE + "&a банит игрока &e" + Placeholders.PLAYER + "&a по IP.\n&aОкончание бана &e" + Placeholders.TIME + "&a.\n&aПричина: " + Placeholders.VALUE);
	@Setting("AnnouncementPermanent")
	private Component announcementPermanent = TextUtils.deserializeLegacy("&e" + Placeholders.SOURCE + "&a перманентно банит игрока &e" + Placeholders.PLAYER + "&a по IP.\n&aПричина: " + Placeholders.VALUE);
	@Setting("Disconnect")
	private Component disconnect = TextUtils.deserializeLegacy("&bВаш IP забанен на сервере.\n&bБан выдал &e" + Placeholders.SOURCE + "\n&bПричина: &e" + Placeholders.VALUE + "&a.\n&bОкончание бана &e" + Placeholders.TIME);
	@Setting("DisconnectPermanent")
	private Component disconnectPermanent = TextUtils.deserializeLegacy("&bВаш IP перманентно забанен на сервере.\n&bБан выдал &e" + Placeholders.SOURCE + "\n&bПричина: &e" + Placeholders.VALUE);

	@Override
	public Component getSuccess(ServerPlayer player) {
		return Text.of(success).replace(Placeholders.PLAYER, player.name()).get();
	}

	@Override
	public Component getAnnouncement(Component source, Component expire, IP ban, ServerPlayer player) {
		return Text.of(announcement).replace(Placeholders.SOURCE, source).replace(Placeholders.PLAYER, player.name()).replace(Placeholders.TIME, expire).replace(Placeholders.VALUE, ban.reason().orElse(Component.empty())).get();
	}

	@Override
	public Component getAnnouncementPermanent(Component source, IP ban, ServerPlayer player) {
		return Text.of(announcement).replace(Placeholders.SOURCE, source).replace(Placeholders.PLAYER, player.name()).replace(Placeholders.VALUE, ban.reason().orElse(Component.empty())).get();
	}

	@Override
	public Component getDisconnect(Component source, Component reason) {
		return Text.of(disconnectPermanent).replace(Placeholders.SOURCE, source).replace(Placeholders.VALUE, reason).get();
	}

	@Override
	public Component getDisconnect(Component source, Component reason, Component expire) {
		return Text.of(disconnect).replace(Placeholders.SOURCE, source).replace(Placeholders.VALUE, reason).replace(Placeholders.TIME, expire).get();
	}

}
