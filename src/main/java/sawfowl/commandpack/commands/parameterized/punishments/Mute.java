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
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.api.data.punishment.Mute.Builder;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Mute extends AbstractParameterizedCommand {

	public Mute(CommandPack plugin) {
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
			if(user.hasPermission(Permissions.IGNORE_MUTE) && isPlayer) {
				src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_BAN_IGNORE), Placeholders.PLAYER, user.name()));
				return;
			}
			if(plugin.getPunishmentService().getMute(user.uniqueId()).isPresent()) {
				src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_MUTE_ALREADY_MUTED), Placeholders.PLAYER, user.name()));
				return;
			}
			Optional<Duration> duration = getArgument(context, Duration.class, "Duration");
			if(!duration.isPresent() && !context.cause().hasPermission(Permissions.PERMANENT_MUTE_ACCESS)) {
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_EXCEPTION_DURATION_NOT_PRESENT));
				return;
			}
			Component source = isPlayer ? ((ServerPlayer) src).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) src).name())) : text("&4Server");
			Builder muteBuilder = sawfowl.commandpack.api.data.punishment.Mute.builder().creationDate(Instant.now()).source(source).target(user);
			if(duration.isPresent()) muteBuilder = muteBuilder.expirationDate(Instant.now().plusSeconds(duration.get().getSeconds()));
			Optional<String> reason = getString(context, "Reason");
			if(reason.isPresent()) muteBuilder = muteBuilder.reason(text(reason.get()));
			sawfowl.commandpack.api.data.punishment.Mute mute = muteBuilder.build();
			plugin.getPunishmentService().addMute(mute);
			if(duration.isPresent()) {
				Sponge.systemSubject().sendMessage(TextUtils.replaceToComponents(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_MUTE_ANNOUNCEMENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, new Component[] {(isPlayer ? ((ServerPlayer) src).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) src).name())) : text("&4Server")), text(user.name()), expire(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), mute), mute.getReason().orElse(text("&f-"))}));	
			} else Sponge.systemSubject().sendMessage(TextUtils.replaceToComponents(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_MUTE_ANNOUNCEMENT_PERMANENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {(isPlayer ? ((ServerPlayer) src).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) src).name())) : text("&4Server")), text(user.name()), mute.getReason().orElse(text("&f-"))}));
			if(plugin.getMainConfig().getPunishment().getAnnounce().isMute()) {
				if(duration.isPresent()) {
					Sponge.server().onlinePlayers().forEach(player -> {
						player.sendMessage(TextUtils.replaceToComponents(getText(player, LocalesPaths.COMMANDS_MUTE_ANNOUNCEMENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, new Component[] {source, text(user.name()), expire(player.locale(), mute), mute.getReason().orElse(text("&f-"))}));
					});	
				} else Sponge.server().onlinePlayers().forEach(player -> {
					player.sendMessage(TextUtils.replaceToComponents(getText(player, LocalesPaths.COMMANDS_MUTE_ANNOUNCEMENT_PERMANENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.VALUE}, new Component[] {source, text(user.name()), mute.getReason().orElse(text("&f-"))}));
				});
			} else {
				src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_MUTE_SUCCESS), Placeholders.PLAYER, userName));
				if(user.player().isPresent() && user.isOnline()) user.player().get().sendMessage(TextUtils.replaceToComponents(getText(locale, mute.getExpirationDate().isPresent() ? LocalesPaths.COMMANDS_MUTE_SUCCESS_TARGET : LocalesPaths.COMMANDS_MUTE_SUCCESS_TARGET_PERMANENT), new String[] {Placeholders.SOURCE, Placeholders.TIME, Placeholders.VALUE}, new Component[] {source, expire(user.player().get().locale(), mute), mute.getReason().orElse(text("-"))}));
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
			ParameterSettings.of(CommandParameters.createUser(false), false, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.createDuration(true), true, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			ParameterSettings.of(CommandParameters.createStrings("Reason", true), true, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

	private Component expire(Locale locale, sawfowl.commandpack.api.data.punishment.Mute mute) {
		if(!mute.getExpirationDate().isPresent()) return Component.empty();
		SimpleDateFormat format = new SimpleDateFormat(getString(locale, LocalesPaths.COMMANDS_SERVERSTAT_TIMEFORMAT));
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(mute.getExpirationDate().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

}
