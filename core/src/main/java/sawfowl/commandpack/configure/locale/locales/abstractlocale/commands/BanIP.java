package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.ban.Ban.IP;

import net.kyori.adventure.text.Component;

public interface BanIP {

	Component getSuccess(ServerPlayer player);

	Component getAnnouncement(Component source, Component expire, IP ban, ServerPlayer player);

	Component getAnnouncementPermanent(Component source, IP ban, ServerPlayer player);

	Component getDisconnect(Component source, Component reason);

	Component getDisconnect(Component source, Component reason, Component expire);

	default Component getDisconnect(boolean permanent, Component source, Component reason, Component expire) {
		return permanent ? getDisconnect(source, reason) : getDisconnect(source, reason, expire);
	}

	default Component getAnnouncement(boolean permanent, Component source, Component expire, IP ban, ServerPlayer player) {
		return permanent ? getAnnouncementPermanent(source, ban, player) : getAnnouncement(source, expire, ban, player);
	}

}
