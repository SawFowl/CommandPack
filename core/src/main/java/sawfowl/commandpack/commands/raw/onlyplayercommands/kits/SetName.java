package sawfowl.commandpack.commands.raw.onlyplayercommands.kits;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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

public class SetName extends AbstractKitsEditCommand  {

	public SetName(CommandPackInstance plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, ServerPlayer src, Locale locale, Mutable arguments, RawArgumentsMap args) throws CommandException {
		Kit kit = args.<Kit>get(0).get();
		KitData kitData = (KitData) (kit instanceof KitData ? kit : Kit.builder().copyFrom(kit));
		Locale localeForName = args.getLocale(1).get();
		if(args.getInput().length < 3) exception(getExceptions(localeForName).getNameNotPresent());
		kitData.setName(localeForName, String.join(" ", Arrays.copyOfRange(args.getInput(), 2, args.getInput().length)));
		kitData.save();
		src.sendMessage(getCommands(locale).getKits().getSetName());
	}

	@Override
	public Component shortDescription(Locale locale) {
		return text("&3Changing the localized kit name.");
	}

	@Override
	public Component extendedDescription(Locale locale) {
		return text("&3Changing the localized kit name.");
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/kits setname <Kit> <Locale> <Name>");
	}

	@Override
	public String command() {
		return "setname";
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			kitArgument(0, false, false),
			RawArguments.createLocaleArgument(RawBasicArgumentData.createLocale(1, null, null), RawOptional.notOptional(), locale -> getExceptions(locale).getLocaleNotPresent()),
			RawArguments.createRemainingJoinedStringsArgument(new RawBasicArgumentData<String>(null, "Name", 2, null, null), RawOptional.notOptional(), locale -> getExceptions(locale).getNameNotPresent())
		);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
