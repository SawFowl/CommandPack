package sawfowl.commandpack.commands.parameterized.punishments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Banlist;
import sawfowl.localeapi.api.Text;

@Register
public class BanList extends AbstractParameterizedCommand {

	Value<String> arg;
	public BanList(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		boolean ip = context.one(arg).isPresent();
		if(ip) {
			sendIpBans(context, src, locale, isPlayer);
		} else sendProfileBans(context, src, locale, isPlayer);
	}

	private void sendIpBans(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(plugin.getPunishmentService().getAllIPBans().isEmpty()) exception(getBanlist(locale).getEmpty());
		boolean unban = isPlayer && context.hasPermission(Permissions.UNBANIP_STAFF);
		List<Component> bans = new ArrayList<Component>();
		plugin.getPunishmentService().getAllIPBans().forEach(ban -> {
			Component banInfo = getBanlist(locale).getElement(ban.address().getHostAddress());
			if(isPlayer && context.hasPermission(Permissions.BANINFO)) banInfo = banInfo.hoverEvent(HoverEvent.showText(getBanlist(locale).getInfo().getIP(ban.address().getHostAddress(), ban.banSource().orElse(text("&4Server")), created(locale, ban), expire(locale, ban), ban.reason().orElse(text("-")))));
			bans.add(unban ? Text.of(plugin.getLocales().getLocale(locale).getButtons().getRemove()).createCallBack(cause -> {
				plugin.getPunishmentService().remove(ban);
				src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getUnbanIP().getSuccess(ban.address().getHostAddress()));
			}).append(banInfo).get() : banInfo);
		});
		if(!isPlayer) {
			int i = 1;
			for(Component ban : bans) {
				if(i >= 50) return;
				src.sendMessage(text(i + " ➤ ").append(ban));
				i++;
			}
		} else {
			Component title = getBanlist(locale).getTitle().replace(new String[] {"%profile%", "%ip%"}, new Component[] {Text.of(getBanlist(locale).getProfile()).createCallBack(cause -> {
				try {
					sendProfileBans(context, src, locale, isPlayer);
				} catch (CommandException e) {
					src.sendMessage(e.componentMessage());
				}
			}).get(), getBanlist(locale).getIP()}).get();
			delay((ServerPlayer) src, locale, consumer -> {
				sendPaginationList(src, title, text("=").color(title.color()), 10, bans);
			});
		}
	}

	private void sendProfileBans(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(plugin.getPunishmentService().getAllProfileBans().isEmpty()) exception(getBanlist(locale).getEmpty());
		boolean unban = isPlayer && context.hasPermission(Permissions.UNBAN_STAFF);
		List<Component> bans = new ArrayList<Component>();
		plugin.getPunishmentService().getAllProfileBans().forEach(ban -> {
			Component banInfo = getBanlist(locale).getElement(ban.profile().name().orElse(ban.profile().examinableName()));
			if(isPlayer) banInfo = banInfo.hoverEvent(
					HoverEvent.showText(getBanlist(locale).getInfo().getPlayer(ban.profile().name().orElse(ban.profile().examinableName()), ban.banSource().orElse(text("&4Server")), created(locale, ban), expire(locale, ban), ban.reason().orElse(text("-"))))
					);
			bans.add(unban ? Text.of(plugin.getLocales().getLocale(locale).getButtons().getRemove()).createCallBack(cause -> {
				plugin.getPunishmentService().remove(ban);
				src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getUnban().getSuccess(ban.profile()));
			}).append(banInfo).get() : banInfo);
		});
		if(!isPlayer) {
			int i = 1;
			for(Component ban : bans) {
				if(i >= 50) return;
				src.sendMessage(text(i + "➤").append(ban));
				i++;
			}
		} else {
			Component title = getBanlist(locale).getTitle().replace(new String[] {"%profile%", "%ip%"}, getBanlist(locale).getProfile(), Text.of(getBanlist(locale).getIP()).createCallBack(cause -> {
				try {
					sendIpBans(context, src, locale, isPlayer);
				} catch (CommandException e) {
					src.sendMessage(e.componentMessage());
				}
			}).get()).get();
			sendPaginationList(src, title, text("=").color(title.color()), 10, bans);
		}
		
	}

	@Override
	public Parameterized build() {
		if(!plugin.getMainConfig().getPunishment().isEnable()) return null;
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.BANLIST;
	}

	@Override
	public String command() {
		return "banlist";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		arg = Parameter.choices("ip").key("IP").optional().build();
		return Arrays.asList(ParameterSettings.of(arg, true, locale -> getExceptions(locale).getValueNotPresent()));
	}

	private Component expire(Locale locale, org.spongepowered.api.service.ban.Ban ban) {
		if(!ban.expirationDate().isPresent()) return plugin.getLocales().getLocale(locale).getCommands().getBanInfo().getPermanent();
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(ban.expirationDate().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

	private String created(Locale locale, org.spongepowered.api.service.ban.Ban ban) {
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(ban.creationDate().toEpochMilli());
		return format.format(calendar.getTime());
	}

	private Banlist getBanlist(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getBanlist();
	}

}
