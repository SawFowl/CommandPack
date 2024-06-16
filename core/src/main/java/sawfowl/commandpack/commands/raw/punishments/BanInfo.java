package sawfowl.commandpack.commands.raw.punishments;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.service.ban.Ban.Profile;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawOptional;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class BanInfo extends AbstractRawCommand {

	public BanInfo(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		Profile ban = args.<Profile>get(0).get();
		Component title = getCommands(locale).getBanInfo().getTitle(ban.profile().name().orElse(ban.profile().examinableName()));
		if(isPlayer) {
			delay((ServerPlayer) audience, locale, consumer -> {
				sendPaginationList(audience, title, text("=").color(title.color()), 10, Arrays.asList(getCommands(locale).getBanInfo().getSuccess(ban.banSource().orElse(text("&4Server")), created(locale, ban), expire(locale, ban), ban.reason().orElse(text("-")))));
			});
		} else sendPaginationList(audience, title, text("=").color(title.color()), 10, Arrays.asList(getCommands(locale).getBanInfo().getSuccess(ban.banSource().orElse(text("&4Server")), created(locale, ban), expire(locale, ban), ban.reason().orElse(text("-")))));
	}

	@Override
	public Component shortDescription(Locale locale) {
		return null;
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return null;
	}

	@Override
	public String permission() {
		return Permissions.BANINFO;
	}

	@Override
	public String command() {
		return "baninfo";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/baninfo <Profile>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(RawArguments.createProfileArgument(RawBasicArgumentData.createProfile(0, null, null), RawOptional.notOptional(), locale -> getCommands(locale).getBanInfo().getNotPresent()));
	}

	@Override
	public void register(RegisterCommandEvent<Raw> event) {
		if(!plugin.getMainConfig().getPunishment().isEnable()) return;
		if(getCommandSettings() == null) {
			event.register(getContainer(), this, command());
		} else {
			if(!getCommandSettings().isEnable()) return;
			if(getCommandSettings().getAliases() != null && getCommandSettings().getAliases().length > 0) {
				event.register(getContainer(), this, command(), getCommandSettings().getAliases());
			} else event.register(getContainer(), this, command());
		}
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

	private String created(Locale locale, org.spongepowered.api.service.ban.Ban ban) {
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(ban.creationDate().toEpochMilli());
		return format.format(calendar.getTime());
	}

	private Component expire(Locale locale, org.spongepowered.api.service.ban.Ban ban) {
		if(!ban.expirationDate().isPresent()) return plugin.getLocales().getLocale(locale).getCommands().getBanInfo().getPermanent();
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(ban.expirationDate().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

}
