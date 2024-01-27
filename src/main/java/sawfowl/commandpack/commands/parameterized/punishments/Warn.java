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
import org.spongepowered.api.service.ban.Ban;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.punishment.Warn.Builder;
import sawfowl.commandpack.api.data.punishment.Warns;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

@Register
public class Warn extends AbstractParameterizedCommand {

	public Warn(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		String userName = getUser(context).get();
		if(isPlayer && userName.equals(((ServerPlayer) src).name())) exception(getText(locale, LocalesPaths.COMMANDS_EXCEPTION_TARGET_SELF));
		Sponge.server().userManager().load(userName).thenAccept(optional -> {
			if(!optional.isPresent()) {
				src.sendMessage(getComponent(locale, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT));
				return;
			}
			User user = optional.get();
			if(user.hasPermission(Permissions.IGNORE_WARN) && isPlayer) {
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_WARN_IGNORE).replace(Placeholders.PLAYER, user.name()).get());
				return;
			}
			Optional<Duration> duration = getArgument(context, Duration.class, "Duration");
			if(!duration.isPresent() && !context.cause().hasPermission(Permissions.PERMANENT_WARN_ACCESS)) {
				src.sendMessage(getComponent(locale, LocalesPaths.COMMANDS_EXCEPTION_DURATION_NOT_PRESENT));
				return;
			}
			Component source = isPlayer ? ((ServerPlayer) src).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) src).name())) : text("&4Server");
			Builder warnBuilder = sawfowl.commandpack.api.data.punishment.Warn.builder().created(Instant.now()).source(source);
			if(duration.isPresent() && duration.get().getSeconds() > 0) warnBuilder = warnBuilder.expiration(Instant.now().plusSeconds(duration.get().getSeconds()));
			Optional<String> reason = getString(context, "Reason");
			if(reason.isPresent()) warnBuilder = warnBuilder.reason(text(reason.get()));
			sawfowl.commandpack.api.data.punishment.Warn warn = warnBuilder.build();
			plugin.getPunishmentService().addWarn(user, warn);
			if(warn.getExpiration().isPresent()) {
				Sponge.systemSubject().sendMessage(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_ANNOUNCEMENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, source, text(user.name()), expire(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), warn), warn.getReason().orElse(text("&f-"))).get());	
			} else Sponge.systemSubject().sendMessage(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_ANNOUNCEMENT_PERMANENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, source, text(user.name()), warn.getReason().orElse(text("&f-"))).get());
			if(plugin.getMainConfig().getPunishment().getAnnounce().isWarn()) {
				if(duration.isPresent()) {
					Sponge.server().onlinePlayers().forEach(player -> {
						player.sendMessage(getText(player, LocalesPaths.COMMANDS_WARN_ANNOUNCEMENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, source, text(user.name()), expire(player.locale(), warn), warn.getReason().orElse(text("&f-"))).get());
					});	
				} else Sponge.server().onlinePlayers().forEach(player -> {
					player.sendMessage(getText(player, LocalesPaths.COMMANDS_WARN_ANNOUNCEMENT_PERMANENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, source, text(user.name()), warn.getReason().orElse(text("&f-"))).get());
				});
			} else {
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_WARN_SUCCESS).replace(Placeholders.PLAYER, userName).get());
				if(user.player().isPresent() && user.isOnline()) user.player().get().sendMessage(getText(user.player().get(), warn.getExpiration().isPresent() ? LocalesPaths.COMMANDS_WARN_SUCCESS_TARGET : LocalesPaths.COMMANDS_WARN_SUCCESS_TARGET_PERMANENT).replace(new String[] {Placeholders.SOURCE, Placeholders.VALUE, Placeholders.TIME}, source, warn.getReason().orElse(text("&e-")), expire(user.player().get().locale(), warn)).get());
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
			ParameterSettings.of(CommandParameters.createUser(false), false, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.createDuration(true), true, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.createStrings("Reason", true), true, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

	private Component expire(Locale locale, sawfowl.commandpack.api.data.punishment.Warn warn) {
		if(!warn.getExpiration().isPresent()) return Component.empty();
		SimpleDateFormat format = new SimpleDateFormat(getString(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT));
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(warn.getExpiration().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

	private Component expire(Locale locale, org.spongepowered.api.service.ban.Ban ban) {
		if(!ban.expirationDate().isPresent()) return Component.empty();
		SimpleDateFormat format = new SimpleDateFormat(getString(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT));
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
		SimpleDateFormat format = new SimpleDateFormat(getString(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT));
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(mute.getExpiration().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

	private void tryMute(User user, Warns warns, Audience src, Locale locale) {
		Optional<sawfowl.commandpack.api.data.punishment.Warn> find = findTempWarn(warns);
		Mute.Builder muteBuilder = Mute.builder().target(user).created(Instant.now()).source(getComponent(user.player().map(ServerPlayer::locale).orElse(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale()), LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH)).reason(getComponent(user.player().map(ServerPlayer::locale).orElse(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale()), LocalesPaths.COMMANDS_WARN_BAN_LIMIT));
		Optional<Instant> expire = find.isPresent() ? find.get().getExpiration() : (plugin.getMainConfig().getPunishment().getWarnsBefore().getPunishTime().getBanTime() > 0 ? Optional.ofNullable(Instant.now().plusSeconds(plugin.getMainConfig().getPunishment().getWarnsBefore().getPunishTime().getBanTime())) : Optional.empty());
		if(expire.isPresent()) muteBuilder = muteBuilder.expiration(expire.get());
		muteBuilder = muteBuilder.reason(getText(user.player().map(ServerPlayer::locale).orElse(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale()), LocalesPaths.COMMANDS_WARN_MUTE_LIMIT).replace(Placeholders.VALUE, String.valueOf(plugin.getMainConfig().getPunishment().getWarnsBefore().getMute())).get());
		Mute mute = muteBuilder.build();
		plugin.getPunishmentService().addMute(mute);
		if(mute.getExpiration().isPresent()) {
			Sponge.systemSubject().sendMessage(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_MUTE_ANNOUNCEMENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, getComponent(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), text(user.name()), expire(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), mute), getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_MUTE_LIMIT).replace(Placeholders.VALUE, String.valueOf(plugin.getMainConfig().getPunishment().getWarnsBefore().getMute())).get()).get());	
		} else Sponge.systemSubject().sendMessage(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_MUTE_ANNOUNCEMENT_PERMANENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, getComponent(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), text(user.name()), getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_MUTE_LIMIT).replace(Placeholders.VALUE, plugin.getMainConfig().getPunishment().getWarnsBefore().getMute()).get()).get());
		if(plugin.getMainConfig().getPunishment().getAnnounce().isBan()) {
			if(mute.getExpiration().isPresent()) {
				Sponge.server().onlinePlayers().forEach(player -> {
					player.sendMessage(getText(player, LocalesPaths.COMMANDS_MUTE_ANNOUNCEMENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, getComponent(player, LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), user.get(Keys.DISPLAY_NAME).orElse(text(user.name())), expire(player.locale(), mute), getComponent(player, LocalesPaths.COMMANDS_WARN_MUTE_LIMIT)).get());
				});
			} else {
				Sponge.server().onlinePlayers().forEach(player -> {
					player.sendMessage(getText(player, LocalesPaths.COMMANDS_MUTE_ANNOUNCEMENT_PERMANENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {getComponent(player, LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), user.get(Keys.DISPLAY_NAME).orElse(text(user.name())), getComponent(player, LocalesPaths.COMMANDS_WARN_MUTE_LIMIT)}).get());
				});
			}
		} else {
			src.sendMessage(getText(locale, LocalesPaths.COMMANDS_MUTE_SUCCESS).replace(Placeholders.PLAYER, user.name()).get());
			if(user.player().isPresent() && user.isOnline()) user.player().get().sendMessage(getText(user.player().get(), mute.getExpiration().isPresent() ? LocalesPaths.COMMANDS_MUTE_SUCCESS_TARGET : LocalesPaths.COMMANDS_MUTE_SUCCESS_TARGET_PERMANENT).replace(new String[] {Placeholders.SOURCE, Placeholders.TIME, Placeholders.VALUE}, getComponent(user.player().get(), LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), expire(user.player().get().locale(), mute), getText(user.player().get(), LocalesPaths.COMMANDS_WARN_MUTE_LIMIT).replace(Placeholders.VALUE, plugin.getMainConfig().getPunishment().getWarnsBefore().getMute()).get()).get());
		}
	}

	private void tryKick(ServerPlayer target, Audience src, Locale locale) {
		target.kick(getText(target, LocalesPaths.COMMANDS_KICK_DISCONNECT).replace(new String[] {Placeholders.SOURCE, Placeholders.VALUE}, getComponent(target, LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), getText(target, LocalesPaths.COMMANDS_WARN_KICK_LIMIT).replace(Placeholders.VALUE, plugin.getMainConfig().getPunishment().getWarnsBefore().getKick()).get()).get());
		Sponge.systemSubject().sendMessage(getText(plugin.getLocales().getLocaleService().getDefaultLocale(), LocalesPaths.COMMANDS_KICK_ANNOUNCEMENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, getComponent(target, LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), text(target.name()), getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_KICK_LIMIT).replace(Placeholders.VALUE, plugin.getMainConfig().getPunishment().getWarnsBefore().getKick()).get()).get());
		if(plugin.getMainConfig().getPunishment().getAnnounce().isKick()) {
			Component targetName = target.get(Keys.DISPLAY_NAME).orElse(text(target.name()));
			Sponge.server().onlinePlayers().forEach(player -> {
				player.sendMessage(getText(player, LocalesPaths.COMMANDS_KICK_ANNOUNCEMENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, getComponent(target, LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), targetName, getText(player, LocalesPaths.COMMANDS_WARN_KICK_LIMIT).replace(Placeholders.VALUE, plugin.getMainConfig().getPunishment().getWarnsBefore().getKick()).get()).get());
			});
		} else src.sendMessage(getText(locale, LocalesPaths.COMMANDS_KICK_SUCCESS).replace(Placeholders.PLAYER, target.name()).get());
	}

	private void tryBan(User user, Warns warns, Audience src, Locale locale, Component source) {
		Optional<sawfowl.commandpack.api.data.punishment.Warn> find = findTempWarn(warns);
		Ban.Builder banBuilder = org.spongepowered.api.service.ban.Ban.builder().type(BanTypes.PROFILE).profile(user.profile()).startDate(Instant.now()).source(getComponent(user.player().map(ServerPlayer::locale).orElse(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale()), LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH)).reason(getComponent(user.player().map(ServerPlayer::locale).orElse(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale()), LocalesPaths.COMMANDS_WARN_BAN_LIMIT));
		Optional<Instant> expire = find.isPresent() ? find.get().getExpiration() : (plugin.getMainConfig().getPunishment().getWarnsBefore().getPunishTime().getBanTime() > 0 ? Optional.ofNullable(Instant.now().plusSeconds(plugin.getMainConfig().getPunishment().getWarnsBefore().getPunishTime().getBanTime())) : Optional.empty());
		if(expire.isPresent()) banBuilder = banBuilder.expirationDate(expire.get());
		banBuilder = banBuilder.reason(getText(user.player().map(ServerPlayer::locale).orElse(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale()), LocalesPaths.COMMANDS_WARN_BAN_LIMIT).replace(Placeholders.VALUE, plugin.getMainConfig().getPunishment().getWarnsBefore().getBan()).get());
		Ban ban = banBuilder.build();
		plugin.getPunishmentService().add(ban);
		if(user.player().isPresent() && user.isOnline()) user.player().get().kick(getText(user.player().get(), ban.expirationDate().isPresent() ? LocalesPaths.COMMANDS_BAN_DISCONNECT : LocalesPaths.COMMANDS_BAN_DISCONNECT_PERMANENT).replace(new String[] {Placeholders.TIME, Placeholders.SOURCE, Placeholders.VALUE}, (ban.expirationDate().isPresent() ? expire(user.player().get().locale(), ban) : text("")), getComponent(user.player().get(), LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), getText(user.player().get(), LocalesPaths.COMMANDS_WARN_BAN_LIMIT).replace(Placeholders.VALUE, plugin.getMainConfig().getPunishment().getWarnsBefore().getBan()).get()).get());
		if(ban.expirationDate().isPresent()) {
			Sponge.systemSubject().sendMessage(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_BAN_ANNOUNCEMENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, getComponent(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), text(user.name()), expire(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), ban), getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_BAN_LIMIT).replace(Placeholders.VALUE, plugin.getMainConfig().getPunishment().getWarnsBefore().getBan()).get()).get());	
		} else Sponge.systemSubject().sendMessage(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_BAN_ANNOUNCEMENT_PERMANENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, getComponent(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), text(user.name()), getComponent(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_BAN_LIMIT)).get());
		if(plugin.getMainConfig().getPunishment().getAnnounce().isBan()) {
			if(ban.expirationDate().isPresent()) {
				Sponge.server().onlinePlayers().forEach(player -> {
					player.sendMessage(getText(player, LocalesPaths.COMMANDS_BAN_ANNOUNCEMENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, getComponent(player, LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), text(user.name()), expire(player.locale(), ban), getText(player, LocalesPaths.COMMANDS_WARN_BAN_LIMIT).replace(Placeholders.VALUE, plugin.getMainConfig().getPunishment().getWarnsBefore().getBan()).get()).get());
				});
			} else {
				Sponge.server().onlinePlayers().forEach(player -> {
					player.sendMessage(getText(player, LocalesPaths.COMMANDS_BAN_ANNOUNCEMENT_PERMANENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, getComponent(player, LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), text(user.name()), getText(player, LocalesPaths.COMMANDS_WARN_BAN_LIMIT).replace(Placeholders.VALUE, plugin.getMainConfig().getPunishment().getWarnsBefore().getBan()).get()).get());
				});
			}
		} else src.sendMessage(getText(locale, LocalesPaths.COMMANDS_BAN_SUCCESS).replace(Placeholders.PLAYER, user.name()).get());
	}

}
