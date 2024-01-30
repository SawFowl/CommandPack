package sawfowl.commandpack.commands.raw.world;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.registry.DefaultedRegistryReference;
import org.spongepowered.api.world.difficulty.Difficulties;
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

public class Difficulty extends AbstractWorldCommand {

	private Map<String, DefaultedRegistryReference<org.spongepowered.api.world.difficulty.Difficulty>> difficulties = new HashMap<>();
	public Difficulty(CommandPack plugin) {
		super(plugin);
		generateMap();
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		ServerWorld world = getWorld(args, 0).get();
		world.properties().setDifficulty(difficulties.get(getString(args, 1).get()).get());
		audience.sendMessage(getText(locale, getLocalesPaths(args[1])).replace(Placeholders.WORLD, world.key().asString()).get());
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
		return null;
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world difficulty <World> <Difficulty>").clickEvent(ClickEvent.suggestCommand("/world difficulty"));
	}

	private Object[] getLocalesPaths(String difficulty) {
		switch (difficulty) {
		case "easy":
			return LocalesPaths.COMMANDS_WORLD_DIFFICULTY_EASY;
		case "normal":
			return LocalesPaths.COMMANDS_WORLD_DIFFICULTY_NORMAL;
		case "hard":
			return LocalesPaths.COMMANDS_WORLD_DIFFICULTY_HARD;
		case "1":
			return LocalesPaths.COMMANDS_WORLD_DIFFICULTY_EASY;
		case "2":
			return LocalesPaths.COMMANDS_WORLD_DIFFICULTY_NORMAL;
		case "3":
			return LocalesPaths.COMMANDS_WORLD_DIFFICULTY_HARD;
		default:
			return LocalesPaths.COMMANDS_WORLD_DIFFICULTY_PEACEFUL;
		}
	}

	@Override
	public List<RawArgument<?>> arguments() {
		if(difficulties.isEmpty()) generateMap();
		return Arrays.asList(
			createWorldArg(),
			RawArguments.createStringArgument(difficulties.keySet(), false, false, 1, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
		);
	}

	private void generateMap() {
		difficulties.put("peaceful", Difficulties.PEACEFUL);
		difficulties.put("easy", Difficulties.EASY);
		difficulties.put("normal", Difficulties.NORMAL);
		difficulties.put("hard", Difficulties.HARD);
		difficulties.put("0", Difficulties.PEACEFUL);
		difficulties.put("1", Difficulties.EASY);
		difficulties.put("2", Difficulties.NORMAL);
		difficulties.put("3", Difficulties.HARD);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
