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
import org.spongepowered.api.registry.DefaultedRegistryReference;
import org.spongepowered.api.world.difficulty.Difficulties;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawArgument;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.localeapi.api.TextUtils;

public class Difficulty extends AbstractWorldCommand {

	private Map<String, DefaultedRegistryReference<org.spongepowered.api.world.difficulty.Difficulty>> difficulties = new HashMap<>();
	public Difficulty(CommandPack plugin) {
		super(plugin);
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
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		if(args.length == 0 || !Sponge.server().worldManager().world(ResourceKey.resolve(args[0])).isPresent()) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT);
		if(args.length == 1 || !difficulties.containsKey(args[1])) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
		ServerWorld world = Sponge.server().worldManager().world(ResourceKey.resolve(args[0])).get();
		world.properties().setDifficulty(difficulties.get(args[1]).get());
		audience.sendMessage(TextUtils.replace(getText(locale, getLocalesPaths(args[1])), Placeholders.WORLD, world.key().asString()));
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		if(!plugin.getMainConfig().isAutoCompleteRawCommands()) return getEmptyCompletion();
		if(args.size() == 0) return Sponge.server().worldManager().worlds().stream().map(ServerWorld::key).map(ResourceKey::asString).map(CommandCompletion::of).collect(Collectors.toList());
		if(args.size() == 1) {
			if(currentInput.endsWith(" ")) {
				return difficulties.keySet().stream().map(CommandCompletion::of).collect(Collectors.toList());
			} else return Sponge.server().worldManager().worlds().stream().map(ServerWorld::key).map(ResourceKey::asString).filter(k -> (k.split(":")[1].startsWith(args.get(0))) || (args.get(0).contains(k) && !args.get(0).contains(k + " "))).map(CommandCompletion::of).collect(Collectors.toList());
		}
		if(args.size() == 2 && !currentInput.endsWith(" ")) return difficulties.keySet().stream().filter(v -> v.startsWith(args.get(1))).map(CommandCompletion::of).collect(Collectors.toList());
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
		return null;
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/world difficulty <World> <Difficulty>");
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
		return null;
	}

}
