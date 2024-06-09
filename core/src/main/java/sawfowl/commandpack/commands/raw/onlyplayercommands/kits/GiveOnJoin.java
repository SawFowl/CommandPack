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
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractKitsEditCommand;
import sawfowl.commandpack.configure.configs.kits.KitData;

public class GiveOnJoin extends AbstractKitsEditCommand {

	public GiveOnJoin(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, Mutable arguments, RawArgumentsMap args) throws CommandException {
		Kit kit = args.<Kit>get(0).get();
		KitData kitData = (KitData) (kit instanceof KitData ? kit : Kit.builder().copyFrom(kit));
		boolean value = args.getBoolean(1).get();
		kitData.setGiveOnJoin(value);
		kitData.save();
		src.sendMessage(value ? getCommands(locale).getKits().getEnableGiveOnJoin() : getCommands(locale).getKits().getDisableGiveOnJoin());
	}

	@Override
	public Component shortDescription(Locale locale) {
		return text("&3Automatic issuance of a kit upon join.");
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("&3Automatic issuance of a kit upon join.");
	}

	@Override
	public String command() {
		return "giveonjoin";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/kits giveonjoin <Kit> <Value>");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			kitArgument(0, false, false),
			RawArguments.createBooleanArgument("Value", false, false, 1, null, null, null, null, locale -> getExceptions(locale).getValueNotPresent())
		);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
