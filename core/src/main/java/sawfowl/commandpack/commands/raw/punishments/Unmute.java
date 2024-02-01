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
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

@Register
public class Unmute extends AbstractRawCommand {

	public Unmute(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		Mute mute = getArgument(Mute.class, cause, args, 0).get();
		plugin.getPunishmentService().removeMute(mute);
		Sponge.systemSubject().sendMessage(getText(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale(), LocalesPaths.COMMANDS_UNMUTE_ANNOUNCEMENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER}, new Component[] {(isPlayer ? ((ServerPlayer) audience).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) audience).name())) : text("&cServer")), text(mute.getName())}).get());
		if(!plugin.getMainConfig().getPunishment().getAnnounce().isUnban()) {
			audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_UNMUTE_SUCCESS).replace(Placeholders.PLAYER, mute.getName()).get());
			Sponge.server().player(mute.getUniqueId()).ifPresent(player -> {
				player.sendMessage(getComponent(player, LocalesPaths.COMMANDS_UNMUTE_SUCCESS_TARGET));
			});
		} else Sponge.server().onlinePlayers().forEach(player -> {
			player.sendMessage(getText(player, LocalesPaths.COMMANDS_UNMUTE_ANNOUNCEMENT).replace(new String[] {Placeholders.SOURCE, Placeholders.PLAYER}, (isPlayer ? ((ServerPlayer) audience).get(Keys.DISPLAY_NAME).orElse(text(((ServerPlayer) audience).name())) : text("&cServer")), text(mute.getName())).get());
		});
		plugin.getAPI().updateCommandTree(command());
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
		return Arrays.asList(RawArguments.createMuteArgument(false, false, 0, null, LocalesPaths.COMMANDS_MUTEINFO_NOT_PRESENT));
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
