package sawfowl.commandpack.api.commands.raw;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.lang3.math.NumberUtils;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.world.WorldType;
import org.spongepowered.api.world.WorldTypes;
import org.spongepowered.api.world.server.ServerWorld;

public class RawArguments {

	public static RawArgument<String> createStringArgument(List<String> variants, boolean optional, boolean optionalForConsole, int cursor, String def, Object[] localesPath) {
		return RawArgument.of(String.class, new Supplier<Stream<String>>() {
			@Override
			public Stream<String> get() {
				return variants.stream();
			}
		}, new RawSupplier<String>() {

			@Override
			public Optional<String> get(String[] args) {
				return args.length >= cursor + 1 ? variants.stream().filter(a -> a.equals(args[cursor])).findFirst() : Optional.ofNullable(def);
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<Integer> createIntegerArgument(List<Integer> variants, boolean optional, boolean optionalForConsole, int cursor, Integer def, Object[] localesPath) {
		return RawArgument.of(Integer.class, new Supplier<Stream<String>>() {
			@Override
			public Stream<String> get() {
				return variants.stream().map(String::valueOf);
			}
		}, new RawSupplier<Integer>() {

			@Override
			public Optional<Integer> get(String[] args) {
				return args.length >= cursor + 1 ? variants.stream().filter(a -> NumberUtils.isParsable(args[cursor]) && a == NumberUtils.createInteger(args[cursor])).findFirst() : Optional.ofNullable(def);
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<Long> createLongArgument(List<Long> variants, boolean optional, boolean optionalForConsole, int cursor, Long def, Object[] localesPath) {
		return RawArgument.of(Long.class, new Supplier<Stream<String>>() {
			@Override
			public Stream<String> get() {
				return variants.stream().map(String::valueOf);
			}
		}, new RawSupplier<Long>() {

			@Override
			public Optional<Long> get(String[] args) {
				return args.length >= cursor + 1 ? variants.stream().filter(a -> NumberUtils.isParsable(args[cursor]) && a == NumberUtils.createLong(args[cursor])).findFirst() : Optional.ofNullable(def);
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<Double> createDoubleArgument(List<Double> variants, boolean optional, boolean optionalForConsole, int cursor, Double def, Object[] localesPath) {
		return RawArgument.of(Double.class, new Supplier<Stream<String>>() {
			@Override
			public Stream<String> get() {
				return variants.stream().map(String::valueOf);
			}
		}, new RawSupplier<Double>() {

			@Override
			public Optional<Double> get(String[] args) {
				return args.length >= cursor + 1 ? variants.stream().filter(a -> NumberUtils.isParsable(args[cursor]) && a == NumberUtils.createDouble(args[cursor])).findFirst() : Optional.ofNullable(def);
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<Boolean> createBooleanArgument(boolean optional, boolean optionalForConsole, int cursor, Boolean def, Object[] localesPath) {
		return RawArgument.of(Boolean.class, new Supplier<Stream<String>>() {
			@Override
			public Stream<String> get() {
				return Arrays.asList("true", "false").stream();
			}
		}, new RawSupplier<Boolean>() {

			@Override
			public Optional<Boolean> get(String[] args) {
				return args.length >= cursor + 1 ? Arrays.asList("true", "false").stream().filter(a -> a.equals(args[cursor])).map(Boolean::valueOf).findFirst() : Optional.ofNullable(def);
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<ServerWorld> createWorldArgument(boolean optional, boolean optionalForConsole, int cursor, ServerWorld def, Object[] localesPath) {
		return RawArgument.of(ServerWorld.class, new Supplier<Stream<String>>() {
			@Override
			public Stream<String> get() {
				return Sponge.server().worldManager().worlds().stream().map(ServerWorld::key).map(ResourceKey::asString);
			}
		}, new RawSupplier<ServerWorld>() {

			@Override
			public Optional<ServerWorld> get(String[] args) {
				return args.length >= cursor + 1 ? Sponge.server().worldManager().world(ResourceKey.resolve(args[cursor])) : Optional.ofNullable(def);
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<WorldType> createWorldTypeArgument(boolean optional, boolean optionalForConsole, int cursor, Object[] localesPath) {
		return RawArgument.of(WorldType.class, new Supplier<Stream<String>>() {
			@Override
			public Stream<String> get() {
				return WorldTypes.registry().streamEntries().map(e -> e.key().asString());
			}
		}, new RawSupplier<WorldType>() {

			@Override
			public Optional<WorldType> get(String[] args) {
				return args.length >= cursor + 1 ? WorldTypes.registry().streamEntries().filter(e -> e.key().asString().equals(args[cursor])).map(e -> e.value()).findFirst() : Optional.empty();
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<ServerPlayer> createPlayerArgument(boolean optional, boolean optionalForConsole, int cursor, Object[] localesPath) {
		return RawArgument.of(ServerPlayer.class, new Supplier<Stream<String>>() {
			@Override
			public Stream<String> get() {
				return Sponge.server().onlinePlayers().stream().map(ServerPlayer::name);
			}
		}, new RawSupplier<ServerPlayer>() {

			@Override
			public Optional<ServerPlayer> get(String[] args) {
				return args.length >= cursor + 1 ? Sponge.server().player(args[cursor]) : Optional.empty();
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<EnchantmentType> createEnchantmentArgument(boolean optional, boolean optionalForConsole, int cursor, Object[] localesPath) {
		return RawArgument.of(EnchantmentType.class, new Supplier<Stream<String>>() {
			@Override
			public Stream<String> get() {
				return EnchantmentTypes.registry().streamEntries().map(e -> e.key().asString());
			}
		}, new RawSupplier<EnchantmentType>() {

			@Override
			public Optional<EnchantmentType> get(String[] args) {
				return args.length >= cursor + 1 ? EnchantmentTypes.registry().streamEntries().filter(e -> e.key().asString().equals(args[cursor])).map(e -> e.value()).findFirst() : Optional.empty();
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

}
