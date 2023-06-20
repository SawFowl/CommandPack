package sawfowl.commandpack.commands.parameterized.punishments;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
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

	private Value<Duration> duration;
	private Value<String> reason;
	public Ban(CommandPack plugin) {
		super(plugin);
		duration = CommandParameters.createDuration(true);
		reason = CommandParameters.createStrings("Reason", true);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		String userName = getUser(context).get();
		Sponge.server().userManager().load(userName).thenAccept(optional -> {
			if(!optional.isPresent()) {
				src.sendMessage(getText(locale, LocalesPaths.COMMANDS_EXCEPTION_USER_NOT_PRESENT));
				return;
			}
			User user = optional.get();
			if(user.hasPermission(Permissions.IGNORE_BAN)) {
				src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_BAN_IGNORE), Placeholders.PLAYER, user.name()));
				return;
			}
			plugin.getPunishmentService().find(user.profile()).thenAccept(optBan -> {
				if(optBan.isPresent()) {
					src.sendMessage(TextUtils.replace(getText(locale, LocalesPaths.COMMANDS_BAN_ALREADY_BANNED), Placeholders.PLAYER, user.name()));
					return;
				}
				Builder banBuilder = org.spongepowered.api.service.ban.Ban.builder().type(BanTypes.PROFILE).profile(user.profile()).startDate(Instant.now()).source(isPlayer ? ((ServerPlayer) src).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) src).name())) : text("&4Server"));
				Optional<Duration> duration = getArgument(context, this.duration);
				if(duration.isPresent()) banBuilder = banBuilder.expirationDate(Instant.now().plusSeconds(duration.get().getSeconds()));
				Optional<String> reason = getString(context, "Reason");
				if(reason.isPresent()) banBuilder = banBuilder.reason(text(reason.get()));
				plugin.getPunishmentService().add(banBuilder.build());
				if(duration.isPresent()) {
					Sponge.server().onlinePlayers().forEach(player -> {
						player.sendMessage(TextUtils.replace(getText(player, LocalesPaths.COMMANDS_BAN_ANNOUNCEMENT), new String[] {Placeholders.SOURCE, Placeholders.PLAYER, Placeholders.TIME, Placeholders.VALUE}, new Component[] {(isPlayer ? ((ServerPlayer) src).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) src).name())) : text("&4Server")), text(user.name())}));
					});
				}
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
			ParameterSettings.of(duration, true, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
			ParameterSettings.of(reason, true, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
			
		);
	}

}
