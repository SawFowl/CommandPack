package sawfowl.commandpack.commands.raw.punishments;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.data.Keys;
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
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Unmute extends AbstractRawCommand {

	public Unmute(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		Mute mute = args.<Mute>get(0).get();
		plugin.getPunishmentService().removeMute(mute);
		Component source = isPlayer ? ((ServerPlayer) audience).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) audience).name())) : text("&4Server");
		Sponge.systemSubject().sendMessage(getCommands().getUnmute().getAnnouncement(source, mute.getName()));
		if(!plugin.getMainConfig().getPunishment().getAnnounce().isUnban()) {
			audience.sendMessage(getCommands(locale).getUnmute().getSuccess(mute.getName()));
			Sponge.server().player(mute.getUniqueId()).ifPresent(player -> {
				player.sendMessage(getCommands(player.locale()).getUnmute().getSuccessTarget());
			});
		} else Sponge.server().onlinePlayers().forEach(player -> {
			player.sendMessage(getCommands(player.locale()).getUnmute().getAnnouncement(source, mute.getName()));
		});
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
		return Permissions.UNMUTE_STAFF;
	}

	@Override
	public String command() {
		return "unmute";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/unmute <Profile>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(RawArguments.createMuteArgument(false, false, 0, null, null, null, locale -> getCommands(locale).getMuteInfo().getNotPresent()));
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

}
