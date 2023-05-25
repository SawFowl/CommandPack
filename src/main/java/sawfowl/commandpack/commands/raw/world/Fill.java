package sawfowl.commandpack.commands.raw.world;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.lang3.math.NumberUtils;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
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
		Optional<ServerWorld> world = getWorld(args, 0);
		boolean start = getString(args, 1).map(arg -> arg.equals("start")).isPresent();
		int interval = getInteger(args, 2).orElse(5);
		long maxMemory = getLong(args, 3).orElse(Runtime.getRuntime().totalMemory() - ((Runtime.getRuntime().totalMemory() / 100) * 20));
		int chunks = getInteger(args, 3).orElse(5);
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
		return text("&c/world fill <World> <Action> [Interval] [MaxMemory] [Chunks]");
	}

	@Override
	public List<RawArgument<?>> arguments() {
		return Arrays.asList(
				RawArgument.of(ServerWorld.class, new Supplier<Stream<String>>() {
					@Override
					public Stream<String> get() {
						return Sponge.server().worldManager().worlds().stream().map(ServerWorld::key).map(ResourceKey::asString);
					}
				}, new RawSupplier<ServerWorld>() {
					@Override
					public Optional<ServerWorld> get(String[] args) {
						return args.length >= 1 ? Sponge.server().worldManager().world(ResourceKey.resolve(args[0])) : Optional.empty();
					}
				}, false, false, 0, LocalesPaths.COMMANDS_EXCEPTION_WORLD_NOT_PRESENT),
				RawArgument.of(String.class, new Supplier<Stream<String>>() {
					@Override
					public Stream<String> get() {
						return actions.stream();
					}
				}, new RawSupplier<String>() {

					@Override
					public Optional<String> get(String[] args) {
						return args.length >= 2 ? actions.stream().filter(a -> a.equals(args[1])).findFirst() : Optional.empty();
					}
				}, false, false, 1, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT),
				RawArgument.of(Integer.class, null, new RawSupplier<Integer>() {
					@Override
					public Optional<Integer> get(String[] args) {
						return Optional.of(args.length >= 3 && NumberUtils.isParsable(args[2]) ? NumberUtils.createInteger(args[2]) : 5);
					}
				}, false, false, 2, aliases));
	}

}
