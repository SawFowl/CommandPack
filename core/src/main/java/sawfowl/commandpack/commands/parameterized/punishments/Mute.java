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

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.punishment.Mute.Builder;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Mute extends AbstractParameterizedCommand {

	public Mute(CommandPackInstance plugin) {
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
			if(user.hasPermission(Permissions.IGNORE_MUTE) && isPlayer) {
				src.sendMessage(getMute(locale).getIgnore(userName));
				return;
			}
			if(plugin.getPunishmentService().getMute(user.uniqueId()).isPresent()) {
				src.sendMessage(getMute(locale).getAlreadyMuted(userName));
				return;
			}
			Optional<Duration> duration = getArgument(context, Duration.class, "Duration");
			if(!duration.isPresent() && !context.cause().hasPermission(Permissions.PERMANENT_MUTE_ACCESS)) {
				src.sendMessage(getExceptions(locale).getDurationNotPresent());
				return;
			}
			Component source = isPlayer ? ((ServerPlayer) src).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) src).name())) : text("&4Server");
			Builder muteBuilder = sawfowl.commandpack.api.data.punishment.Mute.builder().created(Instant.now()).source(source).target(user);
			if(duration.isPresent()) muteBuilder = muteBuilder.expiration(Instant.now().plusSeconds(duration.get().getSeconds()));
			Optional<String> reason = getString(context, "Reason");
			if(reason.isPresent()) muteBuilder = muteBuilder.reason(text(reason.get()));
			sawfowl.commandpack.api.data.punishment.Mute mute = muteBuilder.build();
			plugin.getPunishmentService().addMute(mute);
			Sponge.systemSubject().sendMessage(getMute().getAnnouncement(mute.isIndefinitely(), source, mute.getName(), expire(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), mute), mute.getReason().orElse(text("-"))));	
			if(plugin.getMainConfig().getPunishment().getAnnounce().isMute()) {
				if(duration.isPresent()) {
					Sponge.server().onlinePlayers().forEach(player -> {
						player.sendMessage(getMute(player).getAnnouncement(source, mute.getName(), expire(player.locale(), mute), mute.getReason().orElse(text("-"))));
					});	
				} else Sponge.server().onlinePlayers().forEach(player -> {
					player.sendMessage(getMute(player).getAnnouncementPermanent(source, mute.getName(), mute.getReason().orElse(text("-"))));
				});
			} else {
				src.sendMessage(getMute(locale).getSuccess(mute.getName()));
				if(user.player().isPresent() && user.isOnline()) user.player().get().sendMessage(getMute(user.player().get()).getSuccessTarget(mute.isIndefinitely(), source, expire(user.player().get().locale(), mute), mute.getReason().orElse(text("-"))));
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
		return Permissions.MUTE_STAFF;
	}

	@Override
	public String command() {
		return "mute";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.createUser(false), false, locale -> getExceptions(locale).getUserNotPresent()),
			ParameterSettings.of(CommandParameters.createDuration(true), true, locale -> getExceptions(locale).getDurationNotPresent()),
			ParameterSettings.of(CommandParameters.createStrings("Reason", true), true, locale -> getExceptions(locale).getReasonNotPresent())
		);
	}

	private Component expire(Locale locale, sawfowl.commandpack.api.data.punishment.Mute mute) {
		if(!mute.getExpiration().isPresent()) return Component.empty();
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(mute.getExpiration().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
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

}
