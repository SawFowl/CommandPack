package sawfowl.commandpack.commands.parameterized;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.ban.Ban.Profile;
import org.spongepowered.api.service.ban.BanService;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.PunishmentService;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warns;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.commandpack.utils.TimeConverter;
import sawfowl.localeapi.api.TextUtils;

public class Seen extends AbstractParameterizedCommand {

	public Seen(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			ServerPlayer player = (ServerPlayer) src;
			delay(player, locale, consumer -> {
				Sponge.server().userManager().load(getUser(context).orElse(player.name())).thenAccept(user -> {
					Sponge.server().scheduler().executor(getContainer()).execute(() -> {
						sendInfo(context, src, locale, user.orElse(player.user()), player.hasPermission(Permissions.SEEN_STAFF) || (user.isPresent() && user.get().uniqueId().equals(player.uniqueId())), 10, isPlayer);
					});
				});
			});
		} else {
			Sponge.server().userManager().load(getUser(context).get()).thenAccept(user -> {
				Sponge.server().scheduler().executor(getContainer()).execute(() -> {
					if(user.isPresent()) {
						sendInfo(context, src, locale, user.get(), true, 30, isPlayer);
					} else src.sendMessage(getText(locale, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT));
				});
			});
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.SEEN;
	}

	@Override
	public String command() {
		return "seen";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(ParameterSettings.of(CommandParameters.createUser(true), false, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT));
	}

	private void sendInfo(CommandContext context, Audience audience, Locale locale, User target, boolean full, int size, boolean isPlayer) {
		Optional<ServerPlayer> player = target.player();
		Component displayName = target.get(Keys.DISPLAY_NAME).orElse(text(target.name()));
		Component title = TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_TITLE), Placeholders.PLAYER, displayName);
		Component yes = getText(locale, LocalesPaths.COMMANDS_SEEN_YES);
		Component no = getText(locale, LocalesPaths.COMMANDS_SEEN_NO);
		List<Component> messages = new ArrayList<>();
		boolean online = target.isOnline() && player.isPresent();
		SimpleDateFormat format = new SimpleDateFormat(getString(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT));
		Calendar calendar = Calendar.getInstance(locale);
		if(online) {
			if(full) {
				messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_ONLINE), Placeholders.PLAYER, target.name()));
				messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_ONLINE_TIME), Placeholders.VALUE, timeFormat(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - (plugin.getPlayersData().getPlayerData(target.uniqueId()).isPresent() ? plugin.getPlayersData().getPlayerData(target.uniqueId()).get().getLastJoinTime() : TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())), locale)));
			} else {
				calendar.setTimeInMillis(plugin.getPlayersData().getTempData().getVanishEnabledTime(player.get()).isPresent() ? System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(plugin.getPlayersData().getTempData().getVanishEnabledTime(player.get()).get()) : System.currentTimeMillis());
				messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_OFFLINE), Placeholders.PLAYER, target.name()));
				messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_LAST_ONLINE), Placeholders.VALUE, format.format(calendar.getTime())));
			}
		} else {
			messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_OFFLINE), Placeholders.PLAYER, target.name()));
			messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_LAST_ONLINE), Placeholders.VALUE, timeFormat(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - (plugin.getPlayersData().getPlayerData(target.uniqueId()).isPresent() ? plugin.getPlayersData().getPlayerData(target.uniqueId()).get().getLastExitTime() : TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())), locale)));
		}
		messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_DISPLAY_NAME), Placeholders.VALUE, target.get(Keys.DISPLAY_NAME).orElse(text(target.name()))));
		if(full) {
			messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_UUID), Placeholders.VALUE, target.uniqueId().toString()));
			if(online) messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_IP), Placeholders.VALUE, player.get().connection().address().getHostString()));
		}
		if(target.get(Keys.FIRST_DATE_JOINED).isPresent()) {
			calendar.setTimeInMillis(target.get(Keys.FIRST_DATE_JOINED).get().toEpochMilli());
			messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_FIRST_PLAYED), Placeholders.VALUE, format.format(calendar.getTime())));
		}
		messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_WALKING_SPEED), Placeholders.VALUE, String.valueOf(BigDecimal.valueOf(target.get(Keys.WALKING_SPEED).orElse(0d)).setScale(2, RoundingMode.HALF_UP).doubleValue())));
		messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_FLYING_SPEED), Placeholders.VALUE, String.valueOf(BigDecimal.valueOf(target.get(Keys.FLYING_SPEED).orElse(0d)).setScale(2, RoundingMode.HALF_UP).doubleValue())));
		if(online && full) messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_CURRENT_LOCATION), Placeholders.VALUE, player.get().world().key().asString() + " " + player.get().blockPosition()));
		messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_CAN_FLY), Placeholders.VALUE, target.get(Keys.CAN_FLY).isPresent() && target.get(Keys.CAN_FLY).get() ? yes : no));
		messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_FLYING), Placeholders.VALUE, (target.get(Keys.IS_FLYING).isPresent() && target.get(Keys.IS_FLYING).get()) || (target.get(Keys.IS_ELYTRA_FLYING).isPresent() && target.get(Keys.IS_ELYTRA_FLYING).get()) ? yes : no));
		messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_GAMEMODE), Placeholders.VALUE, target.get(Keys.GAME_MODE).orElse(Sponge.server().gameMode()).asComponent()));
		messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_VANISHED), Placeholders.VALUE, full && online && target.get(Keys.VANISH_STATE).isPresent() && target.get(Keys.VANISH_STATE).get().invisible() ? yes : no));
		messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_GODMODE), Placeholders.VALUE, target.get(Keys.INVULNERABLE).isPresent() && target.get(Keys.INVULNERABLE).get() ? yes : no));
		messages.add(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_AFK), Placeholders.VALUE, full && online && plugin.getPlayersData().getTempData().isAfk(player.get()) ? yes : no));
		if(plugin.getMainConfig().getPunishment().isEnable()) {
			PunishmentService service = plugin.getPunishmentService();
			Optional<Profile> optBan = service.findBan(target.uniqueId());
			Component banned = TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_BAN), Placeholders.VALUE, optBan.isPresent() ? yes : no);
			if(optBan.isPresent() && context.hasPermission(Permissions.BANINFO)) {
				Profile ban = optBan.get();
				banned = banned.hoverEvent(HoverEvent.showText(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_BANINFO_SUCCESS), new String[] {Placeholders.SOURCE, Placeholders.CREATED, Placeholders.EXPIRE, Placeholders.REASON}, new Component[] {ban.banSource().orElse(text("n/a")), text(TimeConverter.toString(ban.creationDate())), ban.expirationDate().map(time -> text(TimeConverter.toString(time))).orElse(text(getText(locale, LocalesPaths.COMMANDS_BANINFO_PERMANENT))), ban.reason().orElse(text("-"))})));
			}
			messages.add(banned);
			Optional<Mute> optMute = service.getMute(target.uniqueId());
			Component muted = TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_MUTE), Placeholders.VALUE, optMute.isPresent() ? yes : no);
			if(optMute.isPresent() && context.hasPermission(Permissions.MUTEINFO)) {
				Mute mute = optMute.get();
				muted = muted.hoverEvent(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_MUTEINFO_SUCCESS), new String[] {Placeholders.SOURCE, Placeholders.CREATED, Placeholders.EXPIRE, Placeholders.REASON}, new Component[] {mute.getSource().orElse(text("n/a")), text(TimeConverter.toString(mute.getCreated())), mute.getExpiration().map(time -> text(TimeConverter.toString(time))).orElse(text(getText(locale, LocalesPaths.COMMANDS_MUTEINFO_PERMANENT))), mute.getReason().orElse(text("-"))}));
			}
			messages.add(muted);
			Optional<Warns> optWarns = service.getWarns(target.uniqueId());
			Component warnsText = TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_WARNS), Placeholders.VALUE, optWarns.map(w -> w.totalWarns() + "/" + w.inAllTime()).orElse("0/0"));
			if(optWarns.isPresent() && context.hasPermission(Permissions.WARNS_OTHER)) {
				Warns warns = optWarns.get();
				warnsText = TextUtils.createCallBack(warnsText, cause -> {
					sendWarnsList(audience, locale, warns, context.hasPermission(Permissions.WARNS_STAFF), isPlayer);
				});
			}
			messages.add(warnsText);
			sendPaginationList(audience, title, getText(locale, LocalesPaths.COMMANDS_SEEN_PADDING), size, messages);
		} else if(Sponge.serviceProvider().provide(BanService.class).isPresent()) {
			BanService service = Sponge.serviceProvider().provide(BanService.class).get();
			service.find(target.profile()).thenAccept(optBan -> {
				Component banned = TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_SEEN_BAN), Placeholders.VALUE, optBan.isPresent() ? yes : no);
				if(optBan.isPresent() && context.hasPermission(Permissions.BANINFO)) {
					Profile ban = optBan.get();
					banned = banned.hoverEvent(HoverEvent.showText(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_BANINFO_SUCCESS), new String[] {Placeholders.SOURCE, Placeholders.CREATED, Placeholders.EXPIRE, Placeholders.REASON}, new Component[] {ban.banSource().orElse(text("n/a")), text(TimeConverter.toString(ban.creationDate())), ban.expirationDate().map(time -> text(TimeConverter.toString(time))).orElse(text(getText(locale, LocalesPaths.COMMANDS_BANINFO_PERMANENT))), ban.reason().orElse(text("-"))})));
				}
				messages.add(banned);
				sendPaginationList(audience, title, getText(locale, LocalesPaths.COMMANDS_SEEN_PADDING), size, messages);
			});
		} else sendPaginationList(audience, title, getText(locale, LocalesPaths.COMMANDS_SEEN_PADDING), size, messages);
	}

	private Component timeFormat(Locale locale, long time) {
		SimpleDateFormat format = new SimpleDateFormat(getString(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT));
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(time);
		return text(format.format(calendar.getTime()));
	}

	private void sendWarnsList(Audience audience, Locale locale, Warns warns, boolean remove, boolean isPlayer) {
		if(warns.getWarns().isEmpty()) return;
		List<Component> list = new ArrayList<>();
		warns.getWarns().forEach(warn -> {
			Component removeText = TextUtils.createCallBack(getText(locale, LocalesPaths.REMOVE), consumer -> {
				Optional<Warns> find = plugin.getPunishmentService().getWarns(warns.getUniqueId());
				plugin.getPunishmentService().removeWarn(warns.getUniqueId(), warn);
				sendWarnsList(audience, locale, find.get(), remove, isPlayer);
			});
			Component w = isPlayer ? TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_WARNS_TIMES), new String[] {Placeholders.TIME, Placeholders.LIMIT}, new Component[] {timeFormat(locale, warn.getCreated().toEpochMilli()), warn.getExpiration().map(i -> timeFormat(locale, i.toEpochMilli())).orElse(text("&c∞"))}).hoverEvent(HoverEvent.showText(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WARNS_REASON), Placeholders.VALUE, warn.getReason().orElse(text("&e-"))))) : TextUtils.replaceToComponents(getText(locale, LocalesPaths.COMMANDS_WARNS_TIMES), new String[] {Placeholders.TIME, Placeholders.LIMIT}, new Component[] {timeFormat(locale, warn.getCreated().toEpochMilli()), warn.getExpiration().map(i -> timeFormat(locale, i.toEpochMilli())).orElse(text("&c∞"))}).append(text("  ")).append(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WARNS_REASON), Placeholders.VALUE, warn.getReason().orElse(text("&e-"))));
			list.add(remove ? removeText.append(text("    ")).append(w) : w);
		});
		Component title = TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WARNS_TITLE), Placeholders.PLAYER, warns.getName());
		sendPaginationList(audience, title, text("=").color(title.color()), 10, list);
	}

}
