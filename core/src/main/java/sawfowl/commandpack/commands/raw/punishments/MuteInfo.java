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
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class MuteInfo extends AbstractRawCommand {

	public MuteInfo(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		Mute mute = args.<Mute>get(0).get();
		Component title = getCommands(locale).getMuteInfo().getTitle(mute.getName());
		if(isPlayer) {
			delay((ServerPlayer) audience, locale, consumer -> {
				sendPaginationList(audience, title, text("=").color(title.color()), 10, Arrays.asList(getCommands(locale).getMuteInfo().getSuccess(mute.getSource().orElse(text("&4Server")), created(locale, mute), expire(locale, mute), mute.getReason().orElse(text("-")))));
			});
		} else sendPaginationList(audience, title, text("=").color(title.color()), 10, Arrays.asList(getCommands(locale).getMuteInfo().getSuccess(mute.getSource().orElse(text("&4Server")), created(locale, mute), expire(locale, mute), mute.getReason().orElse(text("-")))));
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
		return Permissions.MUTEINFO;
	}

	@Override
	public String command() {
		return "muteinfo";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/muteinfo <Profile>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(RawArguments.createMuteArgument(RawBasicArgumentData.createMute(0, null, null), RawOptional.notOptional(), locale -> getCommands(locale).getMuteInfo().getNotPresent()));
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

	private String created(Locale locale, sawfowl.commandpack.api.data.punishment.Mute mute) {
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(mute.getCreated().toEpochMilli());
		return format.format(calendar.getTime());
	}

	private Component expire(Locale locale, sawfowl.commandpack.api.data.punishment.Mute mute) {
		if(!mute.getExpiration().isPresent()) return plugin.getLocales().getLocale(locale).getCommands().getMuteInfo().getPermanent();
		SimpleDateFormat format = new SimpleDateFormat(plugin.getLocales().getLocale(locale).getTime().getFormat());
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeInMillis(mute.getExpiration().get().toEpochMilli());
		return text(format.format(calendar.getTime()));
	}

}
