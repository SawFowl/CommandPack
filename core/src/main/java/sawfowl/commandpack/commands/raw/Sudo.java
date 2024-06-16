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
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawOptional;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractRawCommand;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class Sudo extends AbstractRawCommand {

	public Sudo(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		if(!Sponge.server().commandManager().commandMapping(args.getInput()[1]).isPresent()) exception(getCommands(locale).getSudo().getCommandNotFound());
		CommandResult result = plugin.getAPI().getPlayersData().getOrCreatePlayerData(args.getPlayer(0).get()).runCommand(locale, args.getString(1).get());
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
			RawArguments.createPlayerArgument(RawBasicArgumentData.createPlayer(0, null, null), RawOptional.notOptional(), locale -> getExceptions(locale).getPlayerNotPresent()),
			RawArguments.createRemainingJoinedStringsArgument(new RawBasicArgumentData<>(null, "Command", 1, null, null), RawOptional.notOptional(), locale -> getExceptions(locale).getValueNotPresent())
		);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}


}
