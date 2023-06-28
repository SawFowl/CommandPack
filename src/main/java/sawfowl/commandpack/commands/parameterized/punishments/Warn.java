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
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

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
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT));
				return;
			}
			User user = optional.get();
			if(user.hasPermission(Permissions.IGNORE_WARN) && isPlayer) {
				src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WARN_IGNORE), Placeholders.PLAYER, user.name()));
				return;
			}
			Component source = isPlayer ? ((ServerPlayer) src).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) src).name())) : text("&4Server");
			Builder warnBuilder = sawfowl.commandpack.api.data.punishment.Warn.builder().creationDate(Instant.now()).source(source);
			Optional<Duration> duration = getArgument(context, Duration.class, "Duration");
			if(duration.isPresent() && duration.get().getSeconds() > 0) warnBuilder = warnBuilder.expirationDate(Instant.now().plusSeconds(duration.get().getSeconds()));
			Optional<String> reason = getString(context, "Reason");
			if(reason.isPresent()) warnBuilder = warnBuilder.reason(text(reason.get()));
			sawfowl.commandpack.api.data.punishment.Warn warn = warnBuilder.build();
			plugin.getPunishmentService().addWarn(user, warn);
			if(warn.getExpirationDate().isPresent()) {
				Sponge.systemSubject().sendMessage(TextUtils.replaceToComponents(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_ANNOUNCEMENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, new Component[] {source, text(user.name()), expire(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), warn), warn.getReason().orElse(text("&f-"))}));	
			} else Sponge.systemSubject().sendMessage(TextUtils.replaceToComponents(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_ANNOUNCEMENT_PERMANENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {source, text(user.name()), warn.getReason().orElse(text("&f-"))}));
			if(plugin.getMainConfig().getPunishment().getAnnounce().isWarn()) {
				if(duration.isPresent()) {
					Sponge.server().onlinePlayers().forEach(player -> {
						player.sendMessage(TextUtils.replaceToComponents(getText(player, LocalesPaths.COMMANDS_WARN_ANNOUNCEMENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, new Component[] {source, text(user.name()), expire(player.locale(), warn), warn.getReason().orElse(text("&f-"))}));
					});	
				} else Sponge.server().onlinePlayers().forEach(player -> {
					player.sendMessage(TextUtils.replaceToComponents(getText(player, LocalesPaths.COMMANDS_WARN_ANNOUNCEMENT_PERMANENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {source, text(user.name()), warn.getReason().orElse(text("&f-"))}));
				});
			} else {
				src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_WARN_SUCCESS), Placeholders.PLAYER, userName));
				if(user.player().isPresent() && user.isOnline()) user.player().get().sendMessage(TextUtils.replaceToComponents(getText(locale, warn.getExpirationDate().isPresent() ? LocalesPaths.COMMANDS_WARN_SUCCESS_TARGET : LocalesPaths.COMMANDS_WARN_SUCCESS_TARGET_PERMANENT), new String[] {Placeholders.SOURCE, Placeholders.VALUE, Placeholders.TIME}, new Component[] {source, warn.getReason().orElse(text("&e-")), expire(user.player().get().locale(), warn)}));
			}
			Warns warns = plugin.getPunishmentService().getWarns(user.uniqueId()).get();
			if(warns.totalWarns() >= plugin.getMainConfig().getPunishment().getWarnsBefore().getMute()) {
				Optional<sawfowl.commandpack.api.data.punishment.Warn> find = findTempWarn(warns);
				Mute.Builder muteBuilder = Mute.builder().target(user).creationDate(Instant.now()).source(getText(user.player().map(ServerPlayer::locale).orElse(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale()), LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH)).reason(getText(user.player().map(ServerPlayer::locale).orElse(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale()), LocalesPaths.COMMANDS_WARN_BAN_LIMIT));
				Optional<Instant> expire = find.isPresent() ? find.get().getExpirationDate() : (plugin.getMainConfig().getPunishment().getWarnsBefore().getPunishTime().getBanTime() > 0 ? Optional.ofNullable(Instant.now().plusSeconds(plugin.getMainConfig().getPunishment().getWarnsBefore().getPunishTime().getBanTime())) : Optional.empty());
				if(expire.isPresent()) muteBuilder = muteBuilder.expirationDate(expire.get());
				Mute mute = muteBuilder.build();
				plugin.getPunishmentService().addMute(mute);
				if(mute.getExpirationDate().isPresent()) {
					Sponge.systemSubject().sendMessage(TextUtils.replaceToComponents(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_MUTE_ANNOUNCEMENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, new Component[] {getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), text(user.name()), expire(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), mute), getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_MUTE_LIMIT)}));	
				} else Sponge.systemSubject().sendMessage(TextUtils.replaceToComponents(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_MUTE_ANNOUNCEMENT_PERMANENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), text(user.name()), getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_MUTE_LIMIT)}));
				if(plugin.getMainConfig().getPunishment().getAnnounce().isBan()) {
					if(mute.getExpirationDate().isPresent()) {
						Sponge.server().onlinePlayers().forEach(player -> {
							player.sendMessage(TextUtils.replaceToComponents(getText(player, LocalesPaths.COMMANDS_MUTE_ANNOUNCEMENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, new Component[] {getText(player, LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), text(user.name()), expire(player.locale(), mute), getText(player, LocalesPaths.COMMANDS_WARN_MUTE_LIMIT)}));
						});
					} else {
						Sponge.server().onlinePlayers().forEach(player -> {
							player.sendMessage(TextUtils.replaceToComponents(getText(player, LocalesPaths.COMMANDS_MUTE_ANNOUNCEMENT_PERMANENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {getText(player, LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), text(user.name()), getText(player, LocalesPaths.COMMANDS_WARN_MUTE_LIMIT)}));
						});
					}
				} else {
					src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_MUTE_SUCCESS), Placeholders.PLAYER, userName));
					if(user.player().isPresent() && user.isOnline()) user.player().get().sendMessage(TextUtils.replaceToComponents(getText(locale, mute.getExpirationDate().isPresent() ? LocalesPaths.COMMANDS_MUTE_SUCCESS_TARGET : LocalesPaths.COMMANDS_MUTE_SUCCESS_TARGET_PERMANENT), new String[] {Placeholders.SOURCE, Placeholders.TIME}, new Component[] {getText(user.player().get(), LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), expire(user.player().get().locale(), mute)}));
				}
			}
			if(warns.totalWarns() >= plugin.getMainConfig().getPunishment().getWarnsBefore().getKick() && user.player().isPresent() && user.isOnline()) {
				ServerPlayer target = user.player().get();
				target.kick(TextUtils.replaceToComponents(getText(target, LocalesPaths.COMMANDS_KICK_DISCONNECT), new String[] {Placeholders.SOURCE, Placeholders.VALUE}, new Component[] {source, getText(target, LocalesPaths.COMMANDS_WARN_BAN_LIMIT)}));
				Sponge.systemSubject().sendMessage(TextUtils.replaceToComponents(getText(plugin.getLocales().getLocaleService().getDefaultLocale(), LocalesPaths.COMMANDS_KICK_ANNOUNCEMENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {source, text(target.name()), getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_BAN_LIMIT)}));
				if(plugin.getMainConfig().getPunishment().getAnnounce().isKick()) {
					Component targetName = target.get(Keys.DISPLAY_NAME).orElse(text(target.name()));
					Sponge.server().onlinePlayers().forEach(player -> {
						player.sendMessage(TextUtils.replaceToComponents(getText(player, LocalesPaths.COMMANDS_KICK_ANNOUNCEMENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {source, targetName, getText(player, LocalesPaths.COMMANDS_WARN_BAN_LIMIT)}));
					});
				} else src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_KICK_SUCCESS), Placeholders.PLAYER, target.name()));
			}
			if(warns.totalWarns() >= plugin.getMainConfig().getPunishment().getWarnsBefore().getBan()) {
				Optional<sawfowl.commandpack.api.data.punishment.Warn> find = findTempWarn(warns);
				Ban.Builder banBuilder = org.spongepowered.api.service.ban.Ban.builder().type(BanTypes.PROFILE).profile(user.profile()).startDate(Instant.now()).source(getText(user.player().map(ServerPlayer::locale).orElse(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale()), LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH)).reason(getText(user.player().map(ServerPlayer::locale).orElse(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale()), LocalesPaths.COMMANDS_WARN_BAN_LIMIT));
				Optional<Instant> expire = find.isPresent() ? find.get().getExpirationDate() : (plugin.getMainConfig().getPunishment().getWarnsBefore().getPunishTime().getBanTime() > 0 ? Optional.ofNullable(Instant.now().plusSeconds(plugin.getMainConfig().getPunishment().getWarnsBefore().getPunishTime().getBanTime())) : Optional.empty());
				if(expire.isPresent()) banBuilder = banBuilder.expirationDate(expire.get());
				Ban ban = banBuilder.build();
				plugin.getPunishmentService().add(ban);
				if(user.player().isPresent() && user.isOnline()) user.player().get().kick(TextUtils.replaceToComponents(getText(user.player().get(), ban.expirationDate().isPresent() ? LocalesPaths.COMMANDS_BAN_DISCONNECT : LocalesPaths.COMMANDS_BAN_DISCONNECT_PERMANENT), new String[] {Placeholders.TIME, Placeholders.SOURCE, Placeholders.VALUE}, new Component[] {(ban.expirationDate().isPresent() ? expire(user.player().get().locale(), ban) : text("")), getText(user.player().get(), LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), getText(user.player().get(), LocalesPaths.COMMANDS_WARN_BAN_LIMIT)}));
				if(ban.expirationDate().isPresent()) {
					Sponge.systemSubject().sendMessage(TextUtils.replaceToComponents(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_BAN_ANNOUNCEMENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, new Component[] {getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), text(user.name()), expire(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), ban), getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_BAN_LIMIT)}));	
				} else Sponge.systemSubject().sendMessage(TextUtils.replaceToComponents(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_BAN_ANNOUNCEMENT_PERMANENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), text(user.name()), getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_WARN_BAN_LIMIT)}));
				if(plugin.getMainConfig().getPunishment().getAnnounce().isBan()) {
					if(ban.expirationDate().isPresent()) {
						Sponge.server().onlinePlayers().forEach(player -> {
							player.sendMessage(TextUtils.replaceToComponents(getText(player, LocalesPaths.COMMANDS_BAN_ANNOUNCEMENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, new Component[] {getText(player, LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), text(user.name()), expire(player.locale(), ban), getText(player, LocalesPaths.COMMANDS_WARN_BAN_LIMIT)}));
						});
					} else {
						Sponge.server().onlinePlayers().forEach(player -> {
							player.sendMessage(TextUtils.replaceToComponents(getText(player, LocalesPaths.COMMANDS_BAN_ANNOUNCEMENT_PERMANENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {getText(player, LocalesPaths.COMMANDS_WARN_SOURCE_NAME_AUTOPUNISH), text(user.name()), getText(player, LocalesPaths.COMMANDS_WARN_BAN_LIMIT)}));
						});
					}
				} else src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_BAN_SUCCESS), Placeholders.PLAYER, user.name()));
			}
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
		if(!warn.getExpirationDate().isPresent()) return Component.empty();
		SimpleDateFormat format = new SimpleDateFormat(getString(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT));
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(warn.getExpirationDate().get().toEpochMilli());
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
		for(sawfowl.commandpack.api.data.punishment.Warn w : warns.getWarns()) {
			if(!w.isIndefinitely()) {
				if(find == null) {
					find = w;
				} else if(find.getExpirationDate().get().getEpochSecond() > w.getExpirationDate().get().getEpochSecond()) find = w;
			}
		}
		return Optional.ofNullable(find);
	}

	private Component expire(Locale locale, sawfowl.commandpack.api.data.punishment.Mute mute) {
		if(!mute.getExpirationDate().isPresent()) return Component.empty();
		SimpleDateFormat format = new SimpleDateFormat(getString(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT));
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(mute.getExpirationDate().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

}
