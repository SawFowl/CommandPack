package sawfowl.commandpack.commands.raw.world;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.registry.DefaultedRegistryReference;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.api.commands.raw.arguments.RawBasicArgumentData;
import sawfowl.commandpack.api.commands.raw.arguments.RawOptional;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;

public class GameMode extends AbstractWorldCommand {

	private Map<String, DefaultedRegistryReference<org.spongepowered.api.entity.living.player.gamemode.GameMode>> gamemodes = new HashMap<>();
	public GameMode(CommandPack plugin) {
		super(plugin);
		generateMap();
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException {
		ServerWorld world = args.getWorld(0).get();
		world.properties().setGameMode(gamemodes.get(args.getString(1).get()).get());
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
		return "gamemode";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world gamemode <World> <GameMode>").clickEvent(ClickEvent.suggestCommand("/world gamemode"));
	}

	private Component getLocaleizedComponent(String gamemode, Locale locale, ServerWorld world) {
		switch (gamemode) {
		case "creative":
			return getWorld(locale).getGameMode().getCreative(world);
		case "adventure":
			return getWorld(locale).getGameMode().getAdventure(world);
		case "spectator":
			return getWorld(locale).getGameMode().getSpectator(world);
		case "1":
			return getWorld(locale).getGameMode().getCreative(world);
		case "2":
			return getWorld(locale).getGameMode().getAdventure(world);
		case "3":
			return getWorld(locale).getGameMode().getSpectator(world);
		default:
			return getWorld(locale).getGameMode().getSurvival(world);
		}
	}

	@Override
	public List<RawArgument<?>> arguments() {
		if(gamemodes.isEmpty()) generateMap();
		return Arrays.asList(
			createWorldArg(),
			RawArguments.createStringArgument(gamemodes.keySet(), new RawBasicArgumentData<String>(null, "GameMode", 1, null, null), RawOptional.notOptional(), locale -> getExceptions(locale).getValueNotPresent())
		);
	}

	private void generateMap() {
		gamemodes.put("survival", GameModes.SURVIVAL);
		gamemodes.put("creative", GameModes.CREATIVE);
		gamemodes.put("adventure", GameModes.ADVENTURE);
		gamemodes.put("spectator", GameModes.SPECTATOR);
		gamemodes.put("0", GameModes.SURVIVAL);
		gamemodes.put("1", GameModes.CREATIVE);
		gamemodes.put("2", GameModes.ADVENTURE);
		gamemodes.put("3", GameModes.SPECTATOR);
	}

	@Override
	public List<RawCommand> childCommands() {
		return null;
	}

}
