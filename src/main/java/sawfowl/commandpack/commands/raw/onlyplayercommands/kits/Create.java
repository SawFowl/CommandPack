package sawfowl.commandpack.commands.raw.onlyplayercommands.kits;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawArgument;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractKitsEditCommand;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Create extends AbstractKitsEditCommand {

	public Create(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, String[] args, Mutable arguments) throws CommandException {
		if(args.length == 0) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_NAME_NOT_PRESENT);
		if(getKit(args).isPresent()) exception(locale, LocalesPaths.COMMANDS_KITS_CREATE_EXIST);
		Kit kit = Kit.builder().id(args[0]).build();
		kit.asMenu(getContainer(), src, false).open(src);
		plugin.getKitService().addKit(kit);
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		return getEmptyCompletion();
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
	public List<RawArgument<?>> getArguments() {
		return null;
	}

}
