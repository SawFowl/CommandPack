package sawfowl.commandpack.commands.raw.onlyplayercommands.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawArgument;
import sawfowl.commandpack.api.commands.raw.RawArguments;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractKitsEditCommand;
import sawfowl.commandpack.configure.configs.kits.KitData;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class AddCommand extends AbstractKitsEditCommand {

	public AddCommand(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, String[] args, Mutable arguments) throws CommandException {
		Kit kit = getKit(args, 0).get();
		KitData kitData = (KitData) (kit instanceof KitData ? kit : Kit.builder().copyFrom(kit));
		if(args.length < 2) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
		List<String> list = new ArrayList<>(Arrays.asList(args));
		list.remove(0);
		list.remove(0);
		String command = "";
		for (int i = 0; i < list.size(); i++) {
			command = command + list.get(i);
			if(i < list.size() - 1) {
				command = command + " ";
			}
		}
		kitData.addCommands(command);
		kit.save();
		src.sendMessage(getText(locale, LocalesPaths.COMMANDS_KITS_ADD_COMMAND));
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
			RawArguments.createStringArgument(new ArrayList<>(), false, false, 1, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

}
