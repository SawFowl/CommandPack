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
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractKitsEditCommand;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Create extends AbstractKitsEditCommand {

	public Create(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, Mutable arguments, RawArgumentsMap args) throws CommandException {
		Kit kit = Kit.builder().id(args.getInput()[0]).build();
		kit.asMenu(getContainer(), src, false).open(src);
		plugin.getKitService().addKit(kit);
	}

	@Override
	public Component shortDescription(Locale locale) {
		return text("Create kits");
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("Create kits");
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/kits create <Name>");
	}

	@Override
	public String command() {
		return "create";
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(RawArguments.createStringArgument("name", new ArrayList<>(), false, false, 0, null, null, null, null, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT));
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
