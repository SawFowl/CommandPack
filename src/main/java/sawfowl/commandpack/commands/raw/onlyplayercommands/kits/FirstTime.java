package sawfowl.commandpack.commands.raw.onlyplayercommands.kits;

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

public class FirstTime extends AbstractKitsEditCommand {

	public FirstTime(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, String[] args, Mutable arguments) throws CommandException {
		Kit kit = getKit(args, 0).get();
		KitData kitData = (KitData) (kit instanceof KitData ? kit : Kit.builder().copyFrom(kit));
		boolean value = getBoolean(args, 1).get();
		kitData.setFirstTime(value);
		kitData.save();
		src.sendMessage(getText(locale, value ? LocalesPaths.COMMANDS_KITS_FIRST_TIME_ENABLE : LocalesPaths.COMMANDS_KITS_FIRST_TIME_DISABLE));
	}

	@Override
	public Component shortDescription(Locale locale) {
		return text("&3Automatic issuance of a kit upon join.");
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("&3Determine whether a kit should be given at entry if the player has not received one before.");
	}

	@Override
	public String command() {
		return "firsttime";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/kits firsttime <Kit> <Value>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			kitArgument(0, false, false),
			RawArguments.createBooleanArgument(false, false, 1, false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

}
