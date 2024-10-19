package sawfowl.commandpack.commands.parameterized.punishments;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.ban.BanTypes;
import org.spongepowered.api.service.ban.Ban.Profile;
import org.spongepowered.api.service.ban.Ban;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.punishment.Warn.Builder;
import sawfowl.commandpack.api.data.punishment.Warns;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Warn extends AbstractParameterizedCommand {

	public Warn(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		String userName = getUser(context).get();
		if(isPlayer && userName.equals(((ServerPlayer) src).name())) exception(getExceptions(locale).getTargetSelf());
		Sponge.server().userManager().load(userName).thenAccept(optional -> {
			if(!optional.isPresent()) {
				src.sendMessage(getExceptions(locale).getUserNotPresent());
				return;
			}
			User user = optional.get();
			if(user.hasPermission(Permissions.IGNORE_WARN) && isPlayer) {
				src.sendMessage(getWarn(locale).getIgnore(userName));
				return;
			}
			Optional<Duration> duration = getArgument(context, Duration.class, "Duration");
			if(!duration.isPresent() && !context.cause().hasPermission(Permissions.PERMANENT_WARN_ACCESS)) {
				src.sendMessage(getExceptions(locale).getDurationNotPresent());
				return;
			}
			Component source = isPlayer ? ((ServerPlayer) src).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) src).name())) : text("&4Server");
			Builder warnBuilder = sawfowl.commandpack.api.data.punishment.Warn.builder().created(Instant.now()).source(source);
			if(duration.isPresent() && duration.get().getSeconds() > 0) warnBuilder = warnBuilder.expiration(Instant.now().plusSeconds(duration.get().getSeconds()));
			Optional<String> reason = getString(context, "Reason");
			if(reason.isPresent()) warnBuilder = warnBuilder.reason(text(reason.get()));
			sawfowl.commandpack.api.data.punishment.Warn warn = warnBuilder.build();
			plugin.getPunishmentService().addWarn(user, warn);
			Sponge.systemSubject().sendMessage(getWarn().getAnnouncement(warn.isIndefinitely(), source, userName, warn.getReason().orElse(text("-")), expire(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), warn)));
			if(plugin.getMainConfig().getPunishment().getAnnounce().isWarn()) {
				if(duration.isPresent()) {
					Sponge.server().onlinePlayers().forEach(player -> {
						player.sendMessage(getWarn(player).getAnnouncement(source, userName, warn.getReason().orElse(text("-")), expire(player.locale(), warn)));
					});	
				} else Sponge.server().onlinePlayers().forEach(player -> {
					player.sendMessage(getWarn(player).getAnnouncementPermanent(source, userName, warn.getReason().orElse(text("-"))));
				});
			} else {
				src.sendMessage(getWarn(locale).getSuccess(userName));
				if(user.player().isPresent() && user.isOnline()) user.player().get().sendMessage(getWarn(user.player().get()).getSuccessTarget(warn.isIndefinitely(), source, warn.getReason().orElse(text("-")), expire(user.player().get().locale(), warn)));
			}
			Warns warns = plugin.getPunishmentService().getWarns(user.uniqueId()).get();
			if(!plugin.getPunishmentService().getMute(user.uniqueId()).isPresent() && warns.totalWarns() >= plugin.getMainConfig().getPunishment().getWarnsBefore().getMute()) tryMute(user, warns, src, locale);
			plugin.getPunishmentService().find(user.profile()).thenAccept(optBan -> {
				if(!optBan.isPresent() && warns.totalWarns() >= plugin.getMainConfig().getPunishment().getWarnsBefore().getBan()) {
					tryBan(user, warns, src, locale, source);
				} else if(warns.totalWarns() >= plugin.getMainConfig().getPunishment().getWarnsBefore().getKick() && user.player().isPresent() && user.isOnline()) tryKick(user.player().get(), src, locale);
			});
		});
	}

	@Override
	public Parameterized build() {
		if(!plugin.getMainConfig().getPunishment().isEnable()) return null;
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.WARN_STAFF;
	}

	@Override
	public String command() {
		return "warn";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.createUser(false), false, locale -> getExceptions(locale).getUserNotPresent()),
			ParameterSettings.of(CommandParameters.createDuration(true), true, locale -> getExceptions(locale).getDurationNotPresent()),
			ParameterSettings.of(CommandParameters.createStrings("Reason", true), true, locale -> getExceptions(locale).getReasonNotPresent())
		);
	}

	private Component expire(Locale locale, sawfowl.commandpack.api.data.punishment.Warn warn) {
		if(!warn.getExpiration().isPresent()) return Component.empty();
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(warn.getExpiration().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

	private Component expire(Locale locale, org.spongepowered.api.service.ban.Ban ban) {
		if(!ban.expirationDate().isPresent()) return Component.empty();
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(ban.expirationDate().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

	private Optional<sawfowl.commandpack.api.data.punishment.Warn> findTempWarn(Warns warns) {
		sawfowl.commandpack.api.data.punishment.Warn find = null;
		for(sawfowl.commandpack.api.data.punishment.Warn w : warns.getWarns()) if(!w.isIndefinitely() && (find == null || find.getExpirationTime() > w.getExpirationTime())) find = w;
		return Optional.ofNullable(find);
	}

	private Component expire(Locale locale, sawfowl.commandpack.api.data.punishment.Mute mute) {
		if(!mute.getExpiration().isPresent()) return Component.empty();
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(mute.getExpiration().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

	private void tryMute(User user, Warns warns, Audience src, Locale locale) {
		Optional<sawfowl.commandpack.api.data.punishment.Warn> find = findTempWarn(warns);
		Mute.Builder muteBuilder = Mute.builder().target(user).created(Instant.now()).source(getWarn(user.player().map(ServerPlayer::locale).orElse(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale())).getAutopunish()).reason(getWarn(user.player().map(ServerPlayer::locale).orElse(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale())).getBanLimit(plugin.getMainConfig().getPunishment().getWarnsBefore().getMute()));
		Optional<Instant> expire = find.isPresent() ? find.get().getExpiration() : (plugin.getMainConfig().getPunishment().getWarnsBefore().getPunishTime().getBanTime() > 0 ? Optional.ofNullable(Instant.now().plusSeconds(plugin.getMainConfig().getPunishment().getWarnsBefore().getPunishTime().getBanTime())) : Optional.empty());
		if(expire.isPresent()) muteBuilder = muteBuilder.expiration(expire.get());
		muteBuilder = muteBuilder.reason(getWarn(user.player().map(ServerPlayer::locale).orElse(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale())).getBanLimit(plugin.getMainConfig().getPunishment().getWarnsBefore().getMute()));
		Mute mute = muteBuilder.build();
		plugin.getPunishmentService().addMute(mute);
		Sponge.systemSubject().sendMessage(getMute().getAnnouncement(mute.isIndefinitely(), mute.getSource().orElse(text("&4Server")), mute.getName(), expire(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), mute), mute.getReason().orElse(text("-"))));	
		if(plugin.getMainConfig().getPunishment().getAnnounce().isBan()) {
			if(mute.getExpiration().isPresent()) {
				Sponge.server().onlinePlayers().forEach(player -> {
					player.sendMessage(getMute(player).getAnnouncement(mute.getSource().orElse(text("&4Server")), mute.getName(), expire(player.locale(), mute), mute.getReason().orElse(text("-"))));
				});
			} else {
				Sponge.server().onlinePlayers().forEach(player -> {
					player.sendMessage(getMute(player).getAnnouncementPermanent(mute.getSource().orElse(text("&4Server")), mute.getName(), mute.getReason().orElse(text("-"))));
				});
			}
		} else {
			src.sendMessage(getMute(locale).getSuccess(mute.getName()));
			if(user.player().isPresent() && user.isOnline()) user.player().get().sendMessage(getMute(user.player().get()).getSuccessTarget(mute.isIndefinitely(), mute.getSource().orElse(text("&4Server")), expire(user.player().get().locale(), mute), mute.getReason().orElse(text("-"))));
		}
	}

	private void tryKick(ServerPlayer target, Audience src, Locale locale) {
		target.kick(getKick(target).getDisconnect(getWarn(target.locale()).getAutopunish(), getWarn(target.locale()).getKickLimit(plugin.getMainConfig().getPunishment().getWarnsBefore().getKick())));
		Sponge.systemSubject().sendMessage(getKick().getAnnouncement(getWarn().getAutopunish(), target.name(), getWarn().getKickLimit(plugin.getMainConfig().getPunishment().getWarnsBefore().getKick())));
		if(plugin.getMainConfig().getPunishment().getAnnounce().isKick()) {
			Sponge.server().onlinePlayers().forEach(player -> {
				player.sendMessage(getKick(player).getAnnouncement(getWarn(player).getAutopunish(), target.name(), getWarn(player).getKickLimit(plugin.getMainConfig().getPunishment().getWarnsBefore().getKick())));
			});
		} else src.sendMessage(getKick(locale).getSuccess(target.name()));
	}

	private void tryBan(User user, Warns warns, Audience src, Locale locale, Component source) {
		Optional<sawfowl.commandpack.api.data.punishment.Warn> find = findTempWarn(warns);
		Ban.Builder banBuilder = org.spongepowered.api.service.ban.Ban.builder().type(BanTypes.PROFILE).profile(user.profile()).startDate(Instant.now()).source(getWarn(user.player().map(ServerPlayer::locale).orElse(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale())).getAutopunish()).reason(getWarn(user.player().map(ServerPlayer::locale).orElse(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale())).getBanLimit(plugin.getMainConfig().getPunishment().getWarnsBefore().getBan()));
		Optional<Instant> expire = find.isPresent() ? find.get().getExpiration() : (plugin.getMainConfig().getPunishment().getWarnsBefore().getPunishTime().getBanTime() > 0 ? Optional.ofNullable(Instant.now().plusSeconds(plugin.getMainConfig().getPunishment().getWarnsBefore().getPunishTime().getBanTime())) : Optional.empty());
		if(expire.isPresent()) banBuilder = banBuilder.expirationDate(expire.get());
		banBuilder = banBuilder.reason(getWarn(user.player().map(ServerPlayer::locale).orElse(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale())).getBanLimit(plugin.getMainConfig().getPunishment().getWarnsBefore().getBan()));
		Ban ban = banBuilder.build();
		plugin.getPunishmentService().add(ban);
		if(user.player().isPresent() && user.isOnline()) user.player().get().kick(getBan(user.player().get()).getDisconnect(!ban.expirationDate().isPresent(), source, ban.reason().orElse(text("-")), expire(user.player().get().locale(), ban)));
		Sponge.systemSubject().sendMessage(getBan().getAnnouncement(!ban.expirationDate().isPresent(), source, expire(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), ban), (Profile) ban));	
		if(plugin.getMainConfig().getPunishment().getAnnounce().isBan()) {
			if(ban.expirationDate().isPresent()) {
				Sponge.server().onlinePlayers().forEach(player -> {
					player.sendMessage(getBan(player).getAnnouncement(source, expire(player.locale(), ban), (Profile) ban));
				});
			} else {
				Sponge.server().onlinePlayers().forEach(player -> {
					player.sendMessage(getBan(player).getAnnouncementPermanent(source, (Profile) ban));
				});
			}
		} else src.sendMessage(getBan(locale).getSuccess(user));
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Warn getWarn(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getWarn();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Warn getWarn(ServerPlayer player) {
		return getWarn(player.locale());
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Warn getWarn() {
		return plugin.getLocales().getSystemLocale().getCommands().getWarn();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Mute getMute(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getMute();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Mute getMute(ServerPlayer player) {
		return getMute(player.locale());
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Mute getMute() {
		return plugin.getLocales().getSystemLocale().getCommands().getMute();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Kick getKick(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getKick();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Kick getKick(ServerPlayer player) {
		return getKick(player.locale());
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Kick getKick() {
		return plugin.getLocales().getSystemLocale().getCommands().getKick();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Ban getBan(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getBan();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Ban getBan(ServerPlayer player) {
		return getBan(player.locale());
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Ban getBan() {
		return plugin.getLocales().getSystemLocale().getCommands().getBan();
	}


}
