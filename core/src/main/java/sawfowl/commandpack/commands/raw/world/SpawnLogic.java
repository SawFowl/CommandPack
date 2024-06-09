package sawfowl.commandpack.commands.raw.world;

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
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;

public class SpawnLogic extends AbstractWorldCommand {

	public SpawnLogic(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		ServerWorld world = args.getWorld(0).get();
		boolean enable = args.getBoolean(1).get();
		world.properties().setPerformsSpawnLogic(enable);
		audience.sendMessage(enable ? getWorld(locale).getGenerate().getEnableSpawnLogic(world) : getWorld(locale).getGenerate().getDisableSpawnLogic(world));
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
		return "spawnlogic";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world spawnlogic <world> <Value>").clickEvent(ClickEvent.suggestCommand("/world spawnlogic"));
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
			RawArguments.createWorldArgument(false, false, 0, null, null, null, null, locale -> getExceptions(locale).getWorldNotPresent()),
			RawArguments.createBooleanArgument("Value", false, false, 1, null, null, null, null, locale -> getExceptions(locale).getBooleanNotPresent())
		);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
