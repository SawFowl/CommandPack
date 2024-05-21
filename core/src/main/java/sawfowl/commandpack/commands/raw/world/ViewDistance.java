package sawfowl.commandpack.commands.raw.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class ViewDistance extends AbstractWorldCommand {

	public ViewDistance(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		ServerWorld world = getWorld(args, cause, 0).get();
		int distance = getInteger(args, cause, 1).get();
		if(distance < 1) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
		world.properties().setViewDistance(distance);
		audience.sendMessage(getText(locale, LocalesPaths.COMMANDS_WORLD_VIEWDISTANCE).replace(new String[] {Placeholders.WORLD, Placeholders.VALUE}, world.key().asString(), distance).get());
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
	public String command() {
		return "viewdistance";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world viewdistance <World> <Distance>").clickEvent(ClickEvent.suggestCommand("/world viewdistance"));
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			createWorldArg(),
			RawArguments.createIntegerArgument("Distance", new ArrayList<>(), false, false, 1, null, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
