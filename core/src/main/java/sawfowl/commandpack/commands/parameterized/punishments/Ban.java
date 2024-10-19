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
import org.spongepowered.api.service.ban.Ban.Builder;
import org.spongepowered.api.service.ban.Ban.Profile;
import org.spongepowered.api.service.ban.BanTypes;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Ban extends AbstractParameterizedCommand {

	public Ban(CommandPackInstance plugin) {
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
			if(user.hasPermission(Permissions.IGNORE_BAN) && isPlayer) {
				src.sendMessage(getBan(locale).getIgnore(user));
				return;
			}
			plugin.getPunishmentService().find(user.profile()).thenAccept(optBan -> {
				if(optBan.isPresent()) {
					src.sendMessage(getBan(locale).getAlreadyBanned(user));
					return;
				}
				Optional<Duration> duration = getArgument(context, Duration.class, "Duration");
				if(!duration.isPresent() && !context.cause().hasPermission(Permissions.PERMANENT_BAN_ACCESS)) {
					src.sendMessage(getExceptions(locale).getDurationNotPresent());
					return;
				}
				Component source = isPlayer ? ((ServerPlayer) src).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) src).name())) : text("&4Server");
				Builder banBuilder = org.spongepowered.api.service.ban.Ban.builder().type(BanTypes.PROFILE).profile(user.profile()).startDate(Instant.now()).source(source);
				if(duration.isPresent()) banBuilder = banBuilder.expirationDate(Instant.now().plusSeconds(duration.get().getSeconds()));
				Optional<String> reason = getString(context, "Reason");
				if(reason.isPresent()) banBuilder = banBuilder.reason(text(reason.get()));
				org.spongepowered.api.service.ban.Ban ban = banBuilder.build();
				plugin.getPunishmentService().add(ban);
				if(user.player().isPresent() && user.isOnline()) user.player().get().kick(getBan(user.player().get()).getDisconnect(!ban.expirationDate().isPresent(), source, text(reason.orElse("-")), expire(user.player().get().locale(), ban)));
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
			ParameterSettings.of(CommandParameters.createUser(false), false, locale -> getExceptions(locale).getUserNotPresent()),
			ParameterSettings.of(CommandParameters.createDuration(true), true, locale -> getExceptions(locale).getDurationNotPresent()),
			ParameterSettings.of(CommandParameters.createStrings("Reason", true), true, locale -> getExceptions(locale).getReasonNotPresent())
		);
	}

	private Component expire(Locale locale, org.spongepowered.api.service.ban.Ban ban) {
		if(!ban.expirationDate().isPresent()) return Component.empty();
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(ban.expirationDate().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
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
