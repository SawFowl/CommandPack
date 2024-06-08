package sawfowl.commandpack.listeners;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.PlayerChatEvent;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.localeapi.api.TextUtils;

public class PlayerChatListener {

	private final CommandPack plugin;
	public PlayerChatListener(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Listener(order = Order.LAST)
	public void onSendMessage(PlayerChatEvent.Submit event, @First ServerPlayer player) {
		if(plugin.getMainConfig().getAfkConfig().isEnable()) plugin.getPlayersData().getTempData().updateLastActivity(player);
		if(!plugin.getMainConfig().getPunishment().isEnable()) return;
		Optional<Mute> optMute = plugin.getPunishmentService().getMute(player);
		if(!optMute.isPresent()) return;
		Mute mute = optMute.get();
		event.setCancelled(true);
		player.sendMessage(plugin.getLocales().getLocale(player.locale()).getCommands().getMute().getSuccessTarget(mute.getExpiration().isPresent(), mute.getSource().orElse(text("&e-")), expire(player.locale(), mute), mute.getReason().orElse(text("&e-"))));
	}

	private Component expire(Locale locale, sawfowl.commandpack.api.data.punishment.Mute mute) {
		if(!mute.getExpiration().isPresent()) return Component.empty();
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(mute.getExpiration().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

	private Component text(String string) {
		if(isLegacyDecor(string)) {
			return TextUtils.deserializeLegacy(string);
		} else {
			return TextUtils.deserialize(string);
		}
	}

	private boolean isLegacyDecor(String string) {
		return string.indexOf('&') != -1 && !string.endsWith("&") && isStyleChar(string.charAt(string.indexOf("&") + 1));
	}

	private boolean isStyleChar(char ch) {
		return "0123456789abcdefklmnor".indexOf(ch) != -1;
	}

}
