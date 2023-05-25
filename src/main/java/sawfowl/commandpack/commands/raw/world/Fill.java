package sawfowl.commandpack.commands.raw.world;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.math.NumberUtils;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.raw.RawArgument;
import sawfowl.commandpack.api.commands.raw.RawSupplier;
import sawfowl.commandpack.commands.abstractcommands.raw.AbstractWorldCommand;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class Fill extends AbstractWorldCommand {

	private List<String> actions = Arrays.asList("start", "stop");
	public Fill(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, String[] args, Mutable arguments) throws CommandException {
		if(args.length == 0 || !Sponge.server().worldManager().world(ResourceKey.resolve(args[0])).isPresent()) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT);
		if(args.length == 1) exceptionAppendUsage(cause, locale, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
		ServerWorld world = Sponge.server().worldManager().world(ResourceKey.resolve(args[0])).get();
		boolean start = args[1].equals("start");
		int interval = args.length > 2 && NumberUtils.isParsable(args[2]) ? NumberUtils.toInt(args[2]) : 10;
	}

	@Override
	public List<CommandCompletion> complete(CommandCause cause, List<String> args, Mutable arguments, String currentInput) throws CommandException {
		if(!plugin.getMainConfig().isAutoCompleteRawCommands()) return getEmptyCompletion();
		if(args.size() == 0) return Sponge.server().worldManager().worlds().stream().map(ServerWorld::key).map(ResourceKey::asString).map(CommandCompletion::of).collect(Collectors.toList());
		if(args.size() == 1) {
			if(currentInput.endsWith(" ")) {
				return actions.stream().map(CommandCompletion::of).collect(Collectors.toList());
			} else return Sponge.server().worldManager().worlds().stream().map(ServerWorld::key).map(ResourceKey::asString).filter(k -> (k.split(":")[1].startsWith(args.get(0))) || (args.get(0).contains(k) && !args.get(0).contains(k + " "))).map(CommandCompletion::of).collect(Collectors.toList());
		}
		if(args.size() == 2 && !currentInput.endsWith(" ")) return actions.stream().filter(v -> v.startsWith(args.get(1))).map(CommandCompletion::of).collect(Collectors.toList());
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
		return "fill";
	}

	@Override
	public Component usage(CommandCause cause) {
		return text("&c/cworld fill <World> <Action> [Interval] [MaxMemory] [Chunks]");
	}

	@Override
	public List<RawArgument<?>> getArguments() {
		return Arrays.asList(
				RawArgument.of(ServerWorld.class, new Supplier<Stream<String>>() {
					@Override
					public Stream<String> get() {
						return Sponge.server().worldManager().worlds().stream().map(ServerWorld::key).map(ResourceKey::asString);
					}
				}, new RawSupplier<Optional<ServerWorld>>() {
					@Override
					public Optional<ServerWorld> get(String[] args) {
						return args.length >= 1 ? Sponge.server().worldManager().world(ResourceKey.resolve(args[0])) : Optional.empty();
					}
				}, false, false, null, 0, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT),
				RawArgument.of(String.class, new Supplier<Stream<String>>() {
					@Override
					public Stream<String> get() {
						return actions.stream();
					}
				}, new RawSupplier<Optional<String>>() {

					@Override
					public Optional<String> get(String[] args) {
						return args.length >= 2 ? actions.stream().filter(a -> a.equals(args[1])).findFirst() : Optional.empty();
					}
				}, false, false, null, 1, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
				RawArgument.of(Integer.class, null, new RawSupplier<Integer>() {
					@Override
					public Integer get(String[] args) {
						return args.length >= 3 && NumberUtils.isParsable(args[2]) ? NumberUtils.createInteger(args[2]) : 5;
					}
				}, false, false, 5, 2, aliases));
	}

}
