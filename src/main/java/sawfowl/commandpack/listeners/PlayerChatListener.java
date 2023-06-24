package sawfowl.commandpack.listeners;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.PlayerChatEvent;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class PlayerChatListener {

	private final CommandPack plugin;
	public PlayerChatListener(CommandPack plugin) {
		this.plugin = plugin;
	}

	@Listener(order = Order.LAST)
	public void onExecute(PlayerChatEvent event, @First ServerPlayer player) {
		plugin.getPlayersData().getTempData().updateLastActivity(player);
		plugin.getPunishmentService().getMute(player).ifPresent(mute -> {
			player.sendMessage(TextUtils.replaceToComponents(plugin.getLocales().getText(player.locale(), mute.getExpirationDate().isPresent() ? LocalesPaths.COMMANDS_MUTE_SUCCESS_TARGET : LocalesPaths.COMMANDS_MUTE_SUCCESS_TARGET_PERMANENT), new String[] {Placeholders.SOURCE, Placeholders.TIME, Placeholders.VALUE}, new Component[] {mute.getSource().orElse(text("&e-")), expire(player.locale(), mute), mute.getReason().orElse(text("&e-"))}));
			event.setCancelled(true);
		});
	}

	private Component expire(Locale locale, sawfowl.commandpack.api.data.punishment.Mute mute) {
		if(!mute.getExpirationDate().isPresent()) return Component.empty();
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getString(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT));
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(mute.getExpirationDate().get().toEpochMilli());
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
