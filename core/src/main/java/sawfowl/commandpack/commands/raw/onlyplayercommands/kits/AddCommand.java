package sawfowl.commandpack.commands.raw.onlyplayercommands.kits;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawOptional;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractKitsEditCommand;
import sawfowl.commandpack.configure.configs.kits.KitData;

public class AddCommand extends AbstractKitsEditCommand {

	public AddCommand(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, Mutable arguments, RawArgumentsMap args) throws CommandException {
		Kit kit = args.<Kit>get(0).get();
		KitData kitData = (KitData) (kit instanceof KitData ? kit : Kit.builder().copyFrom(kit));
		Optional<String> command = args.get(1);
		if(!command.isPresent()) exception(getExceptions(locale).getValueNotPresent());
		kitData.addCommands(command.get());
		kitData.save();
		src.sendMessage(getCommands(locale).getKits().getAddCommand());
	}

	@Override
	public Component shortDescription(Locale locale) {
		return text("&3Adding a command to a kit.");
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("&3Adding a command to a kit.");
	}

	@Override
	public String command() {
		return "addcommand";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/kits addcommand <Kit> <Command>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			kitArgument(0, false, false),
			RawArguments.createRemainingJoinedStringsArgument(new RawBasicArgumentData<String>(null, "Command", 1, null, null), RawOptional.notOptional(), locale -> getExceptions(locale).getValueNotPresent())
		);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
