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
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.ban.BanTypes;
import org.spongepowered.api.service.ban.Ban.Builder;

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

public class Banip extends AbstractParameterizedCommand {

	private Value<Duration> duration;
	private Value<String> reason;
	public Banip(CommandPack plugin) {
		super(plugin);
		duration = CommandParameters.createDuration(true);
		reason = CommandParameters.createStrings("Reason", true);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		ServerPlayer target = getPlayer(context).get();
		if(isPlayer && target.name().equals(((ServerPlayer) src).name())) exception(getText(locale, LocalesPaths.COMMANDS_EXCEPTION_TARGET_SELF));
		if(target.hasPermission(Permissions.IGNORE_BANIP)) exception(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_BAN_IGNORE), Placeholders.PLAYER, target.name()));
		Component source = isPlayer ? ((ServerPlayer) src).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) src).name())) : text("&4Server");
		Builder banBuilder = org.spongepowered.api.service.ban.Ban.builder().type(BanTypes.PROFILE).profile(target.profile()).startDate(Instant.now()).source(source);
		Builder banIPBuilder = org.spongepowered.api.service.ban.Ban.builder().type(BanTypes.IP).address(target.connection().address().getAddress()).startDate(Instant.now()).source(source);
		Optional<Duration> duration = getArgument(context, this.duration);
		if(duration.isPresent()) {
			banIPBuilder = banIPBuilder.expirationDate(Instant.now().plusSeconds(duration.get().getSeconds()));
			banBuilder = banBuilder.expirationDate(Instant.now().plusSeconds(duration.get().getSeconds()));
		}
		Optional<String> reason = getString(context, "Reason");
		if(reason.isPresent()) {
			banIPBuilder = banIPBuilder.reason(text(reason.get()));
			banBuilder = banBuilder.reason(text(reason.get()));
		}
		org.spongepowered.api.service.ban.Ban ban = banIPBuilder.build();
		plugin.getPunishmentService().add(banBuilder.build());
		plugin.getPunishmentService().add(ban);
		if(target.isOnline()) target.kick(TextUtils.replace(getText(target, LocalesPaths.COMMANDS_BANIP_DISCONNECT), new String[] {Placeholders.SOURCE, Placeholders.VALUE}, new Component[] {source, text(reason.orElse("-"))}));
		if(ban.expirationDate().isPresent()) {
			Sponge.systemSubject().sendMessage(TextUtils.replace(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_BANIP_ANNOUNCEMENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, new Component[] {(isPlayer ? ((ServerPlayer) src).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) src).name())) : text("&4Server")), text(target.name()), expire(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), ban), ban.reason().orElse(text("&f-"))}));	
		} else Sponge.systemSubject().sendMessage(TextUtils.replace(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_BANIP_ANNOUNCEMENT_PERMANENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {(isPlayer ? ((ServerPlayer) src).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) src).name())) : text("&4Server")), text(target.name()), ban.reason().orElse(text("&f-"))}));
		if(plugin.getMainConfig().getPunishment().getAnnounce().isBan()) {
			if(ban.expirationDate().isPresent()) {
				Sponge.server().onlinePlayers().forEach(player -> {
					player.sendMessage(TextUtils.replace(getText(player, LocalesPaths.COMMANDS_BANIP_ANNOUNCEMENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, new Component[] {source, text(player.name()), expire(player.locale(), ban), ban.reason().orElse(text("&f-"))}));
				});
			} else {
				Sponge.server().onlinePlayers().forEach(player -> {
					player.sendMessage(TextUtils.replace(getText(player, LocalesPaths.COMMANDS_BANIP_ANNOUNCEMENT_PERMANENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {source, text(player.name()), ban.reason().orElse(text("&f-"))}));
				});
			}
		} else src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_BANIP_SUCCESS), Placeholders.PLAYER, target.name()));
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
			ParameterSettings.of(CommandParameters.createPlayer(false), false, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT),
			ParameterSettings.of(duration, true, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			ParameterSettings.of(reason, true, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

	private Component expire(Locale locale, org.spongepowered.api.service.ban.Ban ban) {
		SimpleDateFormat format = new SimpleDateFormat(getString(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT));
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(ban.expirationDate().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

}
