package sawfowl.commandpack.api.commands.raw;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.world.WorldType;
import org.spongepowered.api.world.WorldTypes;
import org.spongepowered.api.world.server.ServerWorld;

import sawfowl.commandpack.CommandPack;
import sawfowl.localeapi.api.EnumLocales;
import sawfowl.localeapi.api.TextUtils;

public class RawArguments {

	public static final List<String> EMPTY = new ArrayList<>();

	public static RawArgument<String> createStringArgument(Collection<String> variants, boolean optional, boolean optionalForConsole, int cursor, String def, Object[] localesPath) {
		return RawArgument.of(String.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return variants.stream();
			}
		}, new RawResultSupplier<String>() {

			@Override
			public Optional<String> get(String[] args) {
				return args.length >= cursor + 1 ? (variants.isEmpty() ? Optional.ofNullable(args[cursor]) : variants.stream().filter(a -> a.equals(args[cursor])).findFirst()) : Optional.ofNullable(def);
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<Integer> createIntegerArgument(Collection<Integer> variants, boolean optional, boolean optionalForConsole, int cursor, Integer def, Object[] localesPath) {
		return RawArgument.of(Integer.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return variants.stream().map(String::valueOf);
			}
		}, new RawResultSupplier<Integer>() {

			@Override
			public Optional<Integer> get(String[] args) {
				return args.length >= cursor + 1 && NumberUtils.isParsable(args[cursor]) && (variants.isEmpty() || variants.stream().filter(a -> a == NumberUtils.createInteger(args[cursor])).findFirst().isPresent()) ? Optional.ofNullable(NumberUtils.createInteger(args[cursor])) : Optional.ofNullable(def);
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<Long> createLongArgument(Collection<Long> variants, boolean optional, boolean optionalForConsole, int cursor, Long def, Object[] localesPath) {
		return RawArgument.of(Long.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return variants.stream().map(String::valueOf);
			}
		}, new RawResultSupplier<Long>() {

			@Override
			public Optional<Long> get(String[] args) {
				return args.length >= cursor + 1 && NumberUtils.isParsable(args[cursor]) && (variants.isEmpty() || variants.stream().filter(a -> a == NumberUtils.createLong(args[cursor])).findFirst().isPresent()) ? Optional.ofNullable(NumberUtils.createLong(args[cursor])) : Optional.ofNullable(def);
				}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<Double> createDoubleArgument(Collection<Double> variants, boolean optional, boolean optionalForConsole, int cursor, Double def, Object[] localesPath) {
		return RawArgument.of(Double.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return variants.stream().map(String::valueOf);
			}
		}, new RawResultSupplier<Double>() {

			@Override
			public Optional<Double> get(String[] args) {
				return args.length >= cursor + 1 && NumberUtils.isParsable(args[cursor]) && (variants.isEmpty() || variants.stream().filter(a -> a == NumberUtils.createDouble(args[cursor])).findFirst().isPresent()) ? Optional.ofNullable(NumberUtils.createDouble(args[cursor])) : Optional.ofNullable(def);
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<BigDecimal> createBigDecimalArgument(Collection<BigDecimal> variants, boolean optional, boolean optionalForConsole, int cursor, BigDecimal def, Object[] localesPath) {
		return RawArgument.of(BigDecimal.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return variants.stream().map(v -> String.valueOf(v.doubleValue()));
			}
		}, new RawResultSupplier<BigDecimal>() {

			@Override
			public Optional<BigDecimal> get(String[] args) {
				return args.length >= cursor + 1 && NumberUtils.isParsable(args[cursor]) && (variants.isEmpty() || variants.stream().filter(a -> a.doubleValue() == NumberUtils.createDouble(args[cursor])).findFirst().isPresent()) ? Optional.ofNullable(BigDecimal.valueOf(NumberUtils.createDouble(args[cursor]))) : Optional.ofNullable(def);
				}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<Boolean> createBooleanArgument(boolean optional, boolean optionalForConsole, int cursor, Boolean def, Object[] localesPath) {
		return RawArgument.of(Boolean.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return Arrays.asList("true", "false").stream();
			}
		}, new RawResultSupplier<Boolean>() {

			@Override
			public Optional<Boolean> get(String[] args) {
				return args.length >= cursor + 1 && BooleanUtils.toBooleanObject(args[cursor]) != null ? Optional.ofNullable(BooleanUtils.toBooleanObject(args[cursor])) : Optional.ofNullable(def);
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<ServerWorld> createWorldArgument(boolean optional, boolean optionalForConsole, int cursor, ServerWorld def, Object[] localesPath) {
		return RawArgument.of(ServerWorld.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return Sponge.server().worldManager().worlds().stream().map(ServerWorld::key).map(ResourceKey::asString);
			}
		}, new RawResultSupplier<ServerWorld>() {

			@Override
			public Optional<ServerWorld> get(String[] args) {
				return args.length >= cursor + 1 ? Sponge.server().worldManager().world(ResourceKey.resolve(args[cursor])) : Optional.ofNullable(def);
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<WorldType> createWorldTypeArgument(boolean optional, boolean optionalForConsole, int cursor, Object[] localesPath) {
		return RawArgument.of(WorldType.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return WorldTypes.registry().streamEntries().map(e -> e.key().asString());
			}
		}, new RawResultSupplier<WorldType>() {

			@Override
			public Optional<WorldType> get(String[] args) {
				return args.length >= cursor + 1 ? WorldTypes.registry().streamEntries().filter(e -> e.key().asString().equals(args[cursor])).map(e -> e.value()).findFirst() : Optional.empty();
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<ServerPlayer> createPlayerArgument(boolean optional, boolean optionalForConsole, int cursor, Object[] localesPath) {
		return RawArgument.of(ServerPlayer.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return Sponge.server().onlinePlayers().stream().map(ServerPlayer::name);
			}
		}, new RawResultSupplier<ServerPlayer>() {

			@Override
			public Optional<ServerPlayer> get(String[] args) {
				return args.length >= cursor + 1 ? Sponge.server().player(args[cursor]) : Optional.empty();
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<EnchantmentType> createEnchantmentArgument(boolean optional, boolean optionalForConsole, int cursor, Object[] localesPath) {
		return RawArgument.of(EnchantmentType.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return EnchantmentTypes.registry().streamEntries().map(e -> e.key().asString());
			}
		}, new RawResultSupplier<EnchantmentType>() {

			@Override
			public Optional<EnchantmentType> get(String[] args) {
				return args.length >= cursor + 1 ? EnchantmentTypes.registry().streamEntries().filter(e -> e.key().asString().equals(args[cursor])).map(e -> e.value()).findFirst() : Optional.empty();
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<Locale> createLocaleArgument(boolean optional, boolean optionalForConsole, int cursor, Object[] localesPath) {
		return RawArgument.of(Locale.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return Stream.of(EnumLocales.values()).map(EnumLocales::getTag);
			}
		}, new RawResultSupplier<Locale>() {

			@Override
			public Optional<Locale> get(String[] args) {
				return args.length >= cursor + 1 ? Stream.of(EnumLocales.values()).filter(l -> l.getTag().equalsIgnoreCase(args[cursor])).map(EnumLocales::get).findFirst() : Optional.empty();
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

	public static RawArgument<Currency> createCurrencyArgument(boolean optional, boolean optionalForConsole, int cursor, Object[] localesPath) {
		return RawArgument.of(Currency.class, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return CommandPack.getInstance().getEconomy().getCurrencies().stream().map(Currency::symbol).map(c -> TextUtils.clearDecorations(c));
			}
		}, new RawResultSupplier<Currency>() {

			@Override
			public Optional<Currency> get(String[] args) {
				return args.length >= cursor + 1 ? Optional.ofNullable(CommandPack.getInstance().getEconomy().checkCurrency(args[cursor])) : Optional.empty();
			}
		}, optional, optionalForConsole, cursor, localesPath);
	}

}
