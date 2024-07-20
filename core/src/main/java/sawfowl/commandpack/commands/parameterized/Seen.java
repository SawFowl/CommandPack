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

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warns;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.api.services.PunishmentService;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@Register
public class Seen extends AbstractParameterizedCommand {

	public Seen(CommandPackInstance plugin) {
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
					} else src.sendMessage(getExceptions(locale).getUserNotPresent());
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
		return Arrays.asList(ParameterSettings.of(CommandParameters.createUser(true), false, locale -> getExceptions(locale).getUserNotPresent()));
	}

	private void sendInfo(CommandContext context, Audience audience, Locale locale, User target, boolean full, int size, boolean isPlayer) {
		Optional<ServerPlayer> player = target.player();
		Component displayName = target.get(Keys.DISPLAY_NAME).orElse(text(target.name()));
		Component title = getSeen(locale).getTitle().replace(Placeholders.PLAYER, displayName).get();
		Component yes = getSeen(locale).getYes();
		Component no = getSeen(locale).getNo();
		List<Component> messages = new ArrayList<>();
		boolean online = target.isOnline() && player.isPresent();
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		if(online) {
			if(full) {
				messages.add(getSeen(locale).getOnline().replace(Placeholders.PLAYER, target.name()).get());
				messages.add(getSeen(locale).getOnlineTime().replace(Placeholders.VALUE, timeFormat(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - (plugin.getPlayersData().getPlayerData(target.uniqueId()).isPresent() ? plugin.getPlayersData().getPlayerData(target.uniqueId()).get().getLastJoinTime() : TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())), locale)).get());
				if(context.hasPermission(Permissions.SEEN_STAFF)) messages.add(getSeen(locale).getClientName(MixinServerPlayer.cast(player.get()).getClientName()));
			} else {
				calendar.setTimeInMillis(plugin.getPlayersData().getTempData().getVanishEnabledTime(player.get()).isPresent() ? System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(plugin.getPlayersData().getTempData().getVanishEnabledTime(player.get()).get()) : System.currentTimeMillis());
				messages.add(getSeen(locale).getOffline().replace(Placeholders.PLAYER, target.name()).get());
				messages.add(getSeen(locale).getLastOnline().replace(Placeholders.VALUE, format.format(calendar.getTime())).get());
			}
		} else {
			messages.add(getSeen(locale).getOffline().replace(Placeholders.PLAYER, target.name()).get());
			messages.add(getSeen(locale).getLastOnline().replace(Placeholders.VALUE, timeFormat(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - (plugin.getPlayersData().getPlayerData(target.uniqueId()).isPresent() ? plugin.getPlayersData().getPlayerData(target.uniqueId()).get().getLastExitTime() : TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())), locale)).get());
		}
		messages.add(getSeen(locale).getDisplayName().replace(Placeholders.VALUE, target.get(Keys.DISPLAY_NAME).orElse(text(target.name()))).get());
		if(full) {
			messages.add(getSeen(locale).getUUID().replace(Placeholders.VALUE, target.uniqueId()).get());
			if(online) messages.add(getSeen(locale).getIP().replace(Placeholders.VALUE, player.get().connection().address().getHostString()).get());
		}
		if(target.get(Keys.FIRST_DATE_JOINED).isPresent()) {
			calendar.setTimeInMillis(target.get(Keys.FIRST_DATE_JOINED).get().toEpochMilli());
			messages.add(getSeen(locale).getFirstPlayed().replace(Placeholders.VALUE, format.format(calendar.getTime())).get());
		}
		messages.add(getSeen(locale).getWalkingSpeed().replace(Placeholders.VALUE, BigDecimal.valueOf(target.get(Keys.WALKING_SPEED).orElse(0d)).setScale(2, RoundingMode.HALF_UP).doubleValue()).get());
		messages.add(getSeen(locale).getFlyingSpeed().replace(Placeholders.VALUE, BigDecimal.valueOf(target.get(Keys.FLYING_SPEED).orElse(0d)).setScale(2, RoundingMode.HALF_UP).doubleValue()).get());
		if(online && full) messages.add(getSeen(locale).getCurrentLocation().replace(Placeholders.VALUE, player.get().world().key().asString() + " " + player.get().blockPosition()).get());
		messages.add(getSeen(locale).getCanFly().replace(Placeholders.VALUE, target.get(Keys.CAN_FLY).isPresent() && target.get(Keys.CAN_FLY).get() ? yes : no).get());
		messages.add(getSeen(locale).getIsFlying().replace(Placeholders.VALUE, (target.get(Keys.IS_FLYING).isPresent() && target.get(Keys.IS_FLYING).get()) || (target.get(Keys.IS_ELYTRA_FLYING).isPresent() && target.get(Keys.IS_ELYTRA_FLYING).get()) ? yes : no).get());
		messages.add(getSeen(locale).getGameMode().replace(Placeholders.VALUE, target.get(Keys.GAME_MODE).orElse(Sponge.server().gameMode()).asComponent()).get());
		messages.add(getSeen(locale).getVanished().replace(Placeholders.VALUE, full && online && target.get(Keys.VANISH_STATE).isPresent() && target.get(Keys.VANISH_STATE).get().invisible() ? yes : no).get());
		messages.add(getSeen(locale).getInvulnerable().replace(Placeholders.VALUE, target.get(Keys.INVULNERABLE).isPresent() && target.get(Keys.INVULNERABLE).get() ? yes : no).get());
		messages.add(getSeen(locale).getAFK().replace(Placeholders.VALUE, full && online && plugin.getPlayersData().getTempData().isAfk(player.get()) ? yes : no).get());
		if(plugin.getMainConfig().getPunishment().isEnable()) {
			PunishmentService service = plugin.getPunishmentService();
			Optional<Profile> optBan = service.findBan(target.uniqueId());
			Component banned = getSeen(locale).getBan().replace(Placeholders.VALUE, optBan.isPresent() ? yes : no).get();
			if(optBan.isPresent() && context.hasPermission(Permissions.BANINFO)) {
				Profile ban = optBan.get();
				banned = banned.hoverEvent(HoverEvent.showText(plugin.getLocales().getLocale(locale).getCommands().getBanInfo().getSuccess(ban.banSource().orElse(text("&4Server")), created(locale, ban), expire(locale, ban), ban.reason().orElse(text("-")))));
			}
			messages.add(banned);
			Optional<Mute> optMute = service.getMute(target.uniqueId());
			Component muted = getSeen(locale).getMute().replace(Placeholders.VALUE, optMute.isPresent() ? yes : no).get();
			if(optMute.isPresent() && context.hasPermission(Permissions.MUTEINFO)) {
				Mute mute = optMute.get();
				muted = muted.hoverEvent(plugin.getLocales().getLocale(locale).getCommands().getMuteInfo().getSuccess(mute.getSource().orElse(text("&4Server")), created(locale, mute), expire(locale, mute), mute.getReason().orElse(text("-"))));
			}
			messages.add(muted);
			Optional<Warns> optWarns = service.getWarns(target.uniqueId());
			Text warnsText = getSeen(locale).getWarns().replace(Placeholders.VALUE, optWarns.map(w -> w.totalWarns() + "/" + w.inAllTime()).orElse("0/0"));
			if(optWarns.isPresent() && context.hasPermission(Permissions.WARNS_OTHER)) {
				Warns warns = optWarns.get();
				warnsText = warnsText.createCallBack(cause -> {
					sendWarnsList(audience, locale, warns, context.hasPermission(Permissions.WARNS_STAFF), isPlayer);
				});
			}
			messages.add(warnsText.get());
			sendPaginationList(audience, title, getSeen(locale).getPadding(), size, messages);
		} else if(Sponge.serviceProvider().provide(BanService.class).isPresent()) {
			BanService service = Sponge.serviceProvider().provide(BanService.class).get();
			service.find(target.profile()).thenAccept(optBan -> {
				Component banned = getSeen(locale).getBan().replace(Placeholders.VALUE, optBan.isPresent() ? yes : no).get();
				if(optBan.isPresent() && context.hasPermission(Permissions.BANINFO)) {
					Profile ban = optBan.get();
					banned = banned.hoverEvent(HoverEvent.showText(plugin.getLocales().getLocale(locale).getCommands().getBanInfo().getSuccess(ban.banSource().orElse(text("&4Server")), created(locale, ban), expire(locale, ban), ban.reason().orElse(text("-")))));
				}
				messages.add(banned);
				sendPaginationList(audience, title, getSeen(locale).getPadding(), size, messages);
			});
		} else sendPaginationList(audience, title, getSeen(locale).getPadding(), size, messages);
	}

	private void sendWarnsList(Audience audience, Locale locale, Warns warns, boolean remove, boolean isPlayer) {
		if(warns.getWarns().isEmpty()) return;
		List<Component> list = new ArrayList<>();
		warns.getWarns().forEach(warn -> {
			Component removeText = TextUtils.createCallBack(plugin.getLocales().getLocale(locale).getButtons().getRemove(), cause -> {
				Optional<Warns> find = plugin.getPunishmentService().getWarns(warns.getUniqueId());
				plugin.getPunishmentService().removeWarn(warns.getUniqueId(), warn);
				sendWarnsList(audience, locale, find.get(), remove, isPlayer);
			});
			Component w = isPlayer ? plugin.getLocales().getLocale(locale).getCommands().getWarns().getTimes(created(locale, warn), expire(locale, warn)) : plugin.getLocales().getLocale(locale).getCommands().getWarns().getTimes(created(locale, warn), expire(locale, warn)).append(plugin.getLocales().getLocale(locale).getCommands().getWarns().getReason(warn.getReason().orElse(text("&e-"))));
			list.add(remove ? removeText.append(text("    ")).append(w) : w);
		});
		Component title = plugin.getLocales().getLocale(locale).getCommands().getWarns().getTitle(warns.getName());
		sendPaginationList(audience, title, text("=").color(title.color()), 10, list);
	}

	private Component expire(Locale locale, sawfowl.commandpack.api.data.punishment.Warn warn) {
		if(!warn.getExpiration().isPresent()) return Component.empty();
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(warn.getExpiration().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

	private Component expire(Locale locale, sawfowl.commandpack.api.data.punishment.Mute mute) {
		if(!mute.getExpiration().isPresent()) return plugin.getLocales().getLocale(locale).getCommands().getMuteInfo().getPermanent();
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(mute.getExpiration().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
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

	private String created(Locale locale, sawfowl.commandpack.api.data.punishment.Mute mute) {
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(mute.getCreated().toEpochMilli());
		return format.format(calendar.getTime());
	}

	private Component created(Locale locale, sawfowl.commandpack.api.data.punishment.Warn warn) {
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(warn.getCreated().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Seen getSeen(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getSeen();
	}

}
