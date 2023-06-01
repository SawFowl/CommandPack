package sawfowl.commandpack.commands.raw;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Sudo extends AbstractRawCommand {

	public Sudo(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		if(!Sponge.server().commandManager().commandMapping(args[1]).isPresent()) exception(getText(locale, LocalesPaths.COMMANDS_SUDO_COMMAND_NOT_FOUND));
		CommandResult result = plugin.getAPI().playersData().getOrCreatePlayerData(getPlayer(args, 0).get()).runCommand(locale, getString(args, 1).get());
		if(!result.isSuccess() && result.errorMessage().isPresent()) exception(result.errorMessage().get());
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
		return Permissions.SUDO_STAFF;
	}

	@Override
	public String command() {
		return "sudo";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/sudo <Player> <Command>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			RawArguments.createPlayerArgument(false, false, 0, LocalesPaths.COMMANDS_EXCEPTION_PLAYER_NOT_PRESENT),
			RawArguments.createRemainingJoinedStringsArgument(false, false, 1, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}


}
