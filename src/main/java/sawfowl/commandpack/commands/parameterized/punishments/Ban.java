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
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.ban.Ban.Builder;
import org.spongepowered.api.service.ban.BanTypes;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Ban extends AbstractParameterizedCommand {

	public Ban(CommandPack plugin) {
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
			if(user.hasPermission(Permissions.IGNORE_BAN) && isPlayer) {
				src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_BAN_IGNORE), Placeholders.PLAYER, user.name()));
				return;
			}
			plugin.getPunishmentService().find(user.profile()).thenAccept(optBan -> {
				if(optBan.isPresent()) {
					src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_BAN_ALREADY_BANNED), Placeholders.PLAYER, user.name()));
					return;
				}
				Component source = isPlayer ? ((ServerPlayer) src).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) src).name())) : text("&4Server");
				Builder banBuilder = org.spongepowered.api.service.ban.Ban.builder().type(BanTypes.PROFILE).profile(user.profile()).startDate(Instant.now()).source(source);
				Optional<Duration> duration = getArgument(context, Duration.class, "Duration");
				if(duration.isPresent()) banBuilder = banBuilder.expirationDate(Instant.now().plusSeconds(duration.get().getSeconds()));
				Optional<String> reason = getString(context, "Reason");
				if(reason.isPresent()) banBuilder = banBuilder.reason(text(reason.get()));
				org.spongepowered.api.service.ban.Ban ban = banBuilder.build();
				plugin.getPunishmentService().add(ban);
				if(user.player().isPresent() && user.isOnline()) user.player().get().kick(TextUtils.replaceToComponents(getText(user.player().get(), ban.expirationDate().isPresent() ? LocalesPaths.COMMANDS_BAN_DISCONNECT : LocalesPaths.COMMANDS_BAN_DISCONNECT_PERMANENT), new String[] {Placeholders.TIME, Placeholders.SOURCE, Placeholders.VALUE}, new Component[] {(ban.expirationDate().isPresent() ? expire(user.player().get().locale(), ban) : text("")), source, text(reason.orElse("-"))}));
				if(ban.expirationDate().isPresent()) {
					Sponge.systemSubject().sendMessage(TextUtils.replaceToComponents(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_BAN_ANNOUNCEMENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, new Component[] {source, text(user.name()), expire(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), ban), ban.reason().orElse(text("&f-"))}));	
				} else Sponge.systemSubject().sendMessage(TextUtils.replaceToComponents(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_BAN_ANNOUNCEMENT_PERMANENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {source, text(user.name()), ban.reason().orElse(text("&f-"))}));
				if(plugin.getMainConfig().getPunishment().getAnnounce().isBan()) {
					if(ban.expirationDate().isPresent()) {
						Sponge.server().onlinePlayers().forEach(player -> {
							player.sendMessage(TextUtils.replaceToComponents(getText(player, LocalesPaths.COMMANDS_BAN_ANNOUNCEMENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, new Component[] {source, text(user.name()), expire(player.locale(), ban), ban.reason().orElse(text("&f-"))}));
						});
					} else {
						Sponge.server().onlinePlayers().forEach(player -> {
							player.sendMessage(TextUtils.replaceToComponents(getText(player, LocalesPaths.COMMANDS_BAN_ANNOUNCEMENT_PERMANENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {source, text(user.name()), ban.reason().orElse(text("&f-"))}));
						});
					}
				} else src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_BAN_SUCCESS), Placeholders.PLAYER, user.name()));
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
		return Permissions.BAN_STAFF;
	}

	@Override
	public String command() {
		return "ban";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.createUser(false), false, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.createDuration(true), true, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.createStrings("Reason", true), true, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

	private Component expire(Locale locale, org.spongepowered.api.service.ban.Ban ban) {
		if(!ban.expirationDate().isPresent()) return Component.empty();
		SimpleDateFormat format = new SimpleDateFormat(getString(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT));
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(ban.expirationDate().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

}
