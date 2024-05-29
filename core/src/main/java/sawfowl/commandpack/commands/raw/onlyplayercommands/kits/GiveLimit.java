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
import sawfowl.commandpack.configure.configs.kits.KitData;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class GiveLimit extends AbstractKitsEditCommand {

	public GiveLimit(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, Mutable arguments, RawArgumentsMap args) throws CommandException {
		Kit kit = args.<Kit>get(0).get();
		KitData kitData = (KitData) (kit instanceof KitData ? kit : Kit.builder().copyFrom(kit));
		Integer limit = args.getInteger(1).get();
		kitData.setLimit(limit);
		kitData.save();
		src.sendMessage(getComponent(locale, LocalesPaths.COMMANDS_KITS_GIVE_LIMIT));
	}

	@Override
	public Component shortDescription(Locale locale) {
		return text("&3Changing the limit for a player to get a kit.");
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("&3Changing the limit for a player to get a kit.");
	}

	@Override
	public String command() {
		return "givelimit";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/kits givelimit <Kit> <Limit>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			kitArgument(0, false, false),
			RawArguments.createIntegerArgument("Limit", new ArrayList<>(), false, false, 1, null, null, null, null, createComponentSupplier(LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT))
		);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
