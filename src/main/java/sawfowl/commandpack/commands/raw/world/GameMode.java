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

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArguments;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class GameMode extends AbstractWorldCommand {

	private Map<String, DefaultedRegistryReference<org.spongepowered.api.entity.living.player.gamemode.GameMode>> gamemodes = new HashMap<>();
	public GameMode(CommandPack plugin) {
		super(plugin);
		generateMap();
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		ServerWorld world = getWorld(args, 0).get();
		world.properties().setGameMode(gamemodes.get(getString(args, 1).get()).get());
		audience.sendMessage(TextUtils.replace(getText(locale, getLocalesPaths(args[1])), Placeholders.WORLD, world.key().asString()));
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
		return "worldgamemode";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world gamemode <World> <GameMode>");
	}

	private Object[] getLocalesPaths(String difficulty) {
		switch (difficulty) {
		case "creative":
			return LocalesPaths.COMMANDS_WORLD_GAMEMODE_CREATIVE;
		case "adventure":
			return LocalesPaths.COMMANDS_WORLD_GAMEMODE_ADVENTURE;
		case "spectator":
			return LocalesPaths.COMMANDS_WORLD_GAMEMODE_SPECTATOR;
		case "1":
			return LocalesPaths.COMMANDS_WORLD_GAMEMODE_CREATIVE;
		case "2":
			return LocalesPaths.COMMANDS_WORLD_GAMEMODE_ADVENTURE;
		case "3":
			return LocalesPaths.COMMANDS_WORLD_GAMEMODE_SPECTATOR;
		default:
			return LocalesPaths.COMMANDS_WORLD_GAMEMODE_SURVIVAL;
		}
	}

	@Override
	public List<RawArgument<?>> arguments() {
		if(gamemodes.isEmpty()) generateMap();
		return Arrays.asList(
			createWorldArg(),
			RawArguments.createStringArgument(gamemodes.keySet(), false, false, 1, null, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT)
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

}
