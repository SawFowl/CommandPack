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

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawOptional;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;

public class Difficulty extends AbstractWorldCommand {

	private Map<String, DefaultedRegistryReference<org.spongepowered.api.world.difficulty.Difficulty>> difficulties = new HashMap<>();
	public Difficulty(CommandPackInstance plugin) {
		super(plugin);
		generateMap();
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		ServerWorld world = args.getWorld(0).get();
		world.properties().setDifficulty(difficulties.get(args.getString(1).get()).get());
		audience.sendMessage(getLocaleizedComponent(args.getInput()[1], locale, world));
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
		return "difficulty";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world difficulty <World> <Difficulty>").clickEvent(ClickEvent.suggestCommand("/world difficulty"));
	}

	private Component getLocaleizedComponent(String difficulty, Locale locale, ServerWorld world) {
		switch (difficulty) {
		case "easy":
			return getCommands(locale).getWorld().getDifficulty().getLow(world);
		case "normal":
			return getCommands(locale).getWorld().getDifficulty().getNormal(world);
		case "hard":
			return getCommands(locale).getWorld().getDifficulty().getHard(world);
		case "1":
			return getCommands(locale).getWorld().getDifficulty().getLow(world);
		case "2":
			return getCommands(locale).getWorld().getDifficulty().getNormal(world);
		case "3":
			return getCommands(locale).getWorld().getDifficulty().getHard(world);
		default:
			return getCommands(locale).getWorld().getDifficulty().getPeaceful(world);
		}
	}

	@Override
	public List<RawArgument<?>> arguments() {
		if(difficulties.isEmpty()) generateMap();
		return Arrays.asList(
			createWorldArg(),
			RawArguments.createStringArgument(difficulties.keySet(), new RawBasicArgumentData<String>(null, "Difficulty", 1, null, null), RawOptional.notOptional(), locale -> getExceptions(locale).getValueNotPresent())
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
