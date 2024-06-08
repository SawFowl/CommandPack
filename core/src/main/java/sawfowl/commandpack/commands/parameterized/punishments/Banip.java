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
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.ban.BanTypes;
import org.spongepowered.api.service.ban.Ban.Builder;
import org.spongepowered.api.service.ban.Ban.IP;
import org.spongepowered.api.service.ban.Ban.Profile;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Banip extends AbstractParameterizedCommand {

	public Banip(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		ServerPlayer target = getPlayer(context).get();
		if(isPlayer && target.name().equals(((ServerPlayer) src).name())) exception(getExceptions(locale).getTargetSelf());
		if(target.hasPermission(Permissions.IGNORE_BANIP) && isPlayer) exception(getBan(locale).getIgnore(target.user()));
		Component source = isPlayer ? ((ServerPlayer) src).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) src).name())) : text("&4Server");
		Builder banBuilder = org.spongepowered.api.service.ban.Ban.builder().type(BanTypes.PROFILE).profile(target.profile()).startDate(Instant.now()).source(source);
		Builder banIPBuilder = org.spongepowered.api.service.ban.Ban.builder().type(BanTypes.IP).address(target.connection().address().getAddress()).startDate(Instant.now()).source(source);
		Optional<Duration> duration = getArgument(context, Duration.class, "Duration");
		if(duration.isPresent()) {
			banIPBuilder = banIPBuilder.expirationDate(Instant.now().plusSeconds(duration.get().getSeconds()));
			banBuilder = banBuilder.expirationDate(Instant.now().plusSeconds(duration.get().getSeconds()));
		}
		Optional<String> reason = getString(context, "Reason");
		if(reason.isPresent()) {
			banIPBuilder = banIPBuilder.reason(text(reason.get()));
			banBuilder = banBuilder.reason(text(reason.get()));
		}
		org.spongepowered.api.service.ban.Ban.IP ban = (IP) banIPBuilder.build();
		plugin.getPunishmentService().saveBans((Profile) banBuilder.build(), ban);
		if(target.isOnline()) target.kick(getBan(target).getDisconnect(!ban.expirationDate().isPresent(), source, text(reason.orElse("-")), expire(target.locale(), ban)));
		Sponge.systemSubject().sendMessage(getBanIP().getAnnouncement(!ban.expirationDate().isPresent(), source, expire(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), ban), ban, target));	
		if(plugin.getMainConfig().getPunishment().getAnnounce().isBan()) {
			if(ban.expirationDate().isPresent()) {
				Sponge.server().onlinePlayers().forEach(player -> {
					player.sendMessage(getBanIP(player).getAnnouncement(source, expire(player.locale(), ban), ban, player));
				});
			} else {
				Sponge.server().onlinePlayers().forEach(player -> {
					player.sendMessage(getBanIP(player).getAnnouncementPermanent(source, ban, player));
				});
			}
		} else src.sendMessage(getBanIP(locale).getSuccess(target));
	}

	@Override
	public Parameterized build() {
		if(!plugin.getMainConfig().getPunishment().isEnable()) return null;
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.BANIP_STAFF;
	}

	@Override
	public String command() {
		return "banip";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.createPlayer(false), false, locale -> getExceptions(locale).getPlayerNotPresent()),
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

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.BanIP getBanIP(Locale locale) {
		return plugin.getLocales().getLocale(locale).getCommands().getBanIP();
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.BanIP getBanIP(ServerPlayer player) {
		return getBanIP(player.locale());
	}

	private sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.BanIP getBanIP() {
		return plugin.getLocales().getSystemLocale().getCommands().getBanIP();
	}

}
