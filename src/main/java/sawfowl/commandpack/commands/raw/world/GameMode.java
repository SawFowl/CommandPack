package sawfowl.commandpack.commands.raw.world;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.registry.DefaultedRegistryReference;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class GameMode extends AbstractWorldCommand {

	private Map<String, DefaultedRegistryReference<org.spongepowered.api.entity.living.player.gamemode.GameMode>> gamemodes = new HashMap<>();
	public GameMode(CommandPack plugin) {
		super(plugin);
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
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		if(args.length == 0 || !Sponge.server().worldManager().world(ResourceKey.resolve(args[0])).isPresent()) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT);
		if(args.length == 1 || !gamemodes.containsKey(args[1])) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
		ServerWorld world = Sponge.server().worldManager().world(ResourceKey.resolve(args[0])).get();
		world.properties().setGameMode(gamemodes.get(args[1]).get());
		audience.sendMessage(TextUtils.replace(getText(locale, getLocalesPaths(args[1])), Placeholders.WORLD, world.key().asString()));
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		if(!plugin.getMainConfig().isAutoCompleteRawCommands()) return getEmptyCompletion();
		if(args.size() == 0) return Sponge.server().worldManager().worlds().stream().map(ServerWorld::key).map(ResourceKey::asString).map(CommandCompletion::of).collect(Collectors.toList());
		if(args.size() == 1) {
			if(currentInput.endsWith(" ")) {
				return gamemodes.keySet().stream().map(CommandCompletion::of).collect(Collectors.toList());
			} else return Sponge.server().worldManager().worlds().stream().map(ServerWorld::key).map(ResourceKey::asString).filter(k -> (k.split(":")[1].startsWith(args.get(0))) || (args.get(0).contains(k) && !args.get(0).contains(k + " "))).map(CommandCompletion::of).collect(Collectors.toList());
		}
		if(args.size() == 2 && !currentInput.endsWith(" ")) return gamemodes.keySet().stream().filter(v -> v.startsWith(args.get(1))).map(CommandCompletion::of).collect(Collectors.toList());
		return getEmptyCompletion();
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
		return text("&c/cworld gamemode <World> <GameMode>");
	}

	private Object getLocalesPaths(String difficulty) {
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

}