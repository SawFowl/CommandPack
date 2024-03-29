package sawfowl.commandpack.commands.parameterized.punishments;

import java.util.ArrayList;
import java.util.Arrays;
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
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.commandpack.utils.TimeConverter;

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
		if(plugin.getPunishmentService().getAllIPBans().isEmpty()) exception(locale, LocalesPaths.COMMANDS_BANLIST_EMPTY);
		boolean unban = isPlayer && context.hasPermission(Permissions.UNBANIP_STAFF);
		List<Component> bans = new ArrayList<Component>();
		plugin.getPunishmentService().getAllIPBans().forEach(ban -> {
			Component banInfo = getText(locale, LocalesPaths.COMMANDS_BANLIST_ELEMENT).replace(Placeholders.VALUE, ban.address().getHostAddress()).get();
			if(isPlayer && context.hasPermission(Permissions.BANINFO)) banInfo = banInfo.hoverEvent(HoverEvent.showText(getText(locale, LocalesPaths.COMMANDS_BANLIST_BANINFO_IP).replace(new String[] {Placeholders.VALUE, Placeholders.SOURCE, Placeholders.CREATED, Placeholders.EXPIRE, Placeholders.REASON}, new Component[] {text(ban.address().getHostAddress()), ban.banSource().orElse(text("n/a")), text(TimeConverter.toString(ban.creationDate())), ban.expirationDate().map(time -> text(TimeConverter.toString(time))).orElse(getComponent(locale, LocalesPaths.COMMANDS_BANINFO_PERMANENT)), ban.reason().orElse(text("-"))}).get()));
			bans.add(unban ? getText(locale, LocalesPaths.REMOVE).createCallBack(cause -> {
				plugin.getPunishmentService().remove(ban);
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_UNBANIP_SUCCESS).replace(Placeholders.VALUE, ban.address().getHostAddress()).get());
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
			Component title = getText(locale, LocalesPaths.COMMANDS_BANLIST_TITLE).replace(new String[] {"%profile%", "%ip%"}, new Component[] {getText(locale, LocalesPaths.COMMANDS_BANLIST_PROFILE).createCallBack(cause -> {
				try {
					sendProfileBans(context, src, locale, isPlayer);
				} catch (CommandException e) {
					src.sendMessage(e.componentMessage());
				}
			}).get(), getComponent(locale, LocalesPaths.COMMANDS_BANLIST_IP)}).get();
			delay((ServerPlayer) src, locale, consumer -> {
				sendPaginationList(src, title, text("=").color(title.color()), 10, bans);
			});
		}
	}

	private void sendProfileBans(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(plugin.getPunishmentService().getAllProfileBans().isEmpty()) exception(locale, LocalesPaths.COMMANDS_BANLIST_EMPTY);
		boolean unban = isPlayer && context.hasPermission(Permissions.UNBAN_STAFF);
		List<Component> bans = new ArrayList<Component>();
		plugin.getPunishmentService().getAllProfileBans().forEach(ban -> {
			Component banInfo = getText(locale, LocalesPaths.COMMANDS_BANLIST_ELEMENT).replace(Placeholders.VALUE, ban.profile().name().orElse(ban.profile().examinableName())).get();
			if(isPlayer) banInfo = banInfo.hoverEvent(
					HoverEvent.showText(getText(locale, LocalesPaths.COMMANDS_BANLIST_BANINFO_PLAYER).replace(new String[] {Placeholders.PLAYER, Placeholders.SOURCE, Placeholders.CREATED, Placeholders.EXPIRE, Placeholders.REASON}, new Component[] {text(ban.profile().name().orElse(ban.profile().examinableName())), ban.banSource().orElse(text("n/a")), text(TimeConverter.toString(ban.creationDate())), ban.expirationDate().map(time -> text(TimeConverter.toString(time))).orElse(text(getText(locale, LocalesPaths.COMMANDS_BANINFO_PERMANENT))), ban.reason().orElse(text("-"))}).get())
					);
			bans.add(unban ? getText(locale, LocalesPaths.REMOVE).createCallBack(cause -> {
				plugin.getPunishmentService().remove(ban);
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_UNBAN_SUCCESS).replace(Placeholders.PLAYER, ban.profile().name().orElse(ban.profile().examinableName())).get());
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
			Component title = getText(locale, LocalesPaths.COMMANDS_BANLIST_TITLE).replace(new String[] {"%profile%", "%ip%"}, getComponent(locale, LocalesPaths.COMMANDS_BANLIST_PROFILE), getText(locale, LocalesPaths.COMMANDS_BANLIST_IP).createCallBack(cause -> {
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
		return Arrays.asList(ParameterSettings.of(arg, true, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT));
	}

}
