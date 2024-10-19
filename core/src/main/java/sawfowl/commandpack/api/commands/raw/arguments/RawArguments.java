package sawfowl.commandpack.api.commands.raw.arguments;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.registrar.tree.CommandTreeNodeTypes;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.command.Command.Raw;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.Profile;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.world.WorldType;
import org.spongepowered.api.world.WorldTypes;
import org.spongepowered.api.world.generation.ChunkGenerator;
import org.spongepowered.api.world.server.ServerWorld;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.api.data.player.Warp;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warns;
import sawfowl.localeapi.api.ComponentSupplier;
import sawfowl.localeapi.api.EnumLocales;

/**
 * This class is designed to quickly create some arguments for {@link Raw} commands.<br>
 * Some more variants may be added in the future.
 * 
 * @author SawFowl
 */
public class RawArguments {

	private static final CommandPackInstance plugin = CommandPackInstance.getInstance();
	public static final List<String> EMPTY = new ArrayList<>();
	public static final Integer[] emptyIds = {};
	public static final String[] emptyKeys = {};
	/**
	 * Can be used as a token.
	*/
	@SuppressWarnings("unchecked")
	public static final Class<CompletableFuture<Optional<User>>> USER_LOAD_CLASS = (Class<CompletableFuture<Optional<User>>>) new CompletableFuture<Optional<User>>().getClass();

	public static RawArgument<String> createStringArgument(@NotNull Stream<String> variants, RawBasicArgumentData<String> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			String.class,
			(cause, args) -> variants,
			(cause, args) -> args.length >= data.cursor() + 1 ? Optional.ofNullable(variants.filter(var -> var.equals(args[data.cursor()])).findFirst().orElse(data.def())) : Optional.ofNullable(data.def()),
			data.toRawArgumentData(CommandTreeNodeTypes.STRING.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<String> createStringArgument(@NotNull Collection<String> variants, RawBasicArgumentData<String> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			String.class,
			(cause, args) -> variants.size() > 10000 ? variants.parallelStream() : variants.stream(),
			(cause, args) -> args.length >= data.cursor() + 1 ? (variants.isEmpty() ? Optional.ofNullable(args[data.cursor()]) : Optional.ofNullable(variants.stream().filter(var -> var.equals(args[data.cursor()])).findFirst().orElse(data.def()))) : Optional.ofNullable(data.def()),
			data.toRawArgumentData(CommandTreeNodeTypes.STRING.get().createNode()),
			rawOptional,
			supplier
		);
	}

	/**
	 * This argument should be specified last in the queue.
	 */
	public static RawArgument<String> createRemainingJoinedStringsArgument(RawBasicArgumentData<String> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			String.class,
			(cause, args) -> Stream.empty(),
			(cause, args) -> args.length >= data.cursor() + 1 ?  Optional.ofNullable(String.join(" ", Stream.of(Arrays.copyOfRange(args, data.cursor(), args.length)).filter(string -> string != null).toArray(String[]::new))) : Optional.ofNullable(data.def()),
			data.toRawArgumentData(CommandTreeNodeTypes.STRING.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<Integer> createIntegerArgument(@NotNull Collection<Integer> variants, RawBasicArgumentData<Integer> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			Integer.class,
			(cause, args) -> (variants.size() > 10000 ? variants.parallelStream() : variants.stream()).map(String::valueOf),
			(cause, args) -> args.length >= data.cursor() + 1 && NumberUtils.isParsable(args[data.cursor()]) && (variants.isEmpty() || variants.stream().filter(a -> a == NumberUtils.createInteger(args[data.cursor()])).findFirst().isPresent()) ? Optional.ofNullable(NumberUtils.createInteger(args[data.cursor()])) : Optional.ofNullable(data.def()),
			data.toRawArgumentData(CommandTreeNodeTypes.INTEGER.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<Long> createLongArgument(@NotNull Collection<Long> variants, RawBasicArgumentData<Long> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			Long.class,
			(cause, args) -> (variants.size() > 10000 ? variants.parallelStream() : variants.stream()).map(String::valueOf),
			(cause, args) -> args.length >= data.cursor() + 1 && NumberUtils.isParsable(args[data.cursor()]) && (variants.isEmpty() || variants.stream().filter(a -> a == NumberUtils.createLong(args[data.cursor()])).findFirst().isPresent()) ? Optional.ofNullable(NumberUtils.createLong(args[data.cursor()])) : Optional.ofNullable(data.def()),
			data.toRawArgumentData(CommandTreeNodeTypes.LONG.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<Double> createDoubleArgument(@NotNull Collection<Double> variants, RawBasicArgumentData<Double> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			Double.class,
			(cause, args) -> (variants.size() > 10000 ? variants.parallelStream() : variants.stream()).map(String::valueOf),
			(cause, args) -> args.length >= data.cursor() + 1 && NumberUtils.isParsable(args[data.cursor()]) && (variants.isEmpty() || variants.stream().filter(a -> a == NumberUtils.createDouble(args[data.cursor()])).findFirst().isPresent()) ? Optional.ofNullable(NumberUtils.createDouble(args[data.cursor()])) : Optional.ofNullable(data.def()),
			data.toRawArgumentData(CommandTreeNodeTypes.DOUBLE.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<BigDecimal> createBigDecimalArgument(@NotNull Collection<BigDecimal> variants, RawBasicArgumentData<BigDecimal> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			BigDecimal.class,
			(cause, args) -> (variants.size() > 10000 ? variants.parallelStream() : variants.stream()).map(String::valueOf),
			(cause, args) -> args.length >= data.cursor() + 1 && NumberUtils.isParsable(args[data.cursor()]) && (variants.isEmpty() || variants.stream().filter(a -> a.doubleValue() == NumberUtils.createDouble(args[data.cursor()])).findFirst().isPresent()) ? Optional.ofNullable(NumberUtils.createBigDecimal(args[data.cursor()])) : Optional.ofNullable(data.def()),
			data.toRawArgumentData(CommandTreeNodeTypes.DOUBLE.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<Boolean> createBooleanArgument(RawBasicArgumentData<Boolean> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			Boolean.class,
			(cause, args) -> Stream.of("true", "false"),
			(cause, args) -> args.length >= data.cursor() + 1 && BooleanUtils.toBooleanObject(args[data.cursor()]) != null ? Optional.ofNullable(BooleanUtils.toBooleanObject(args[data.cursor()])) : Optional.ofNullable(data.def()),
			data.toRawArgumentData(CommandTreeNodeTypes.BOOL.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<ServerWorld> createWorldArgument(RawBasicArgumentData<ServerWorld> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			ServerWorld.class,
			(cause, args) -> Sponge.server().worldManager().worlds().stream().map(w -> w.key().asString()),
			(cause, args) -> args.length >= data.cursor() + 1 ? Optional.ofNullable(Sponge.server().worldManager().worlds().stream().filter(w -> w.key().asString().equals(args[data.cursor()])).findFirst().orElse(data.def())) : Optional.ofNullable(data.def()),
			data.toRawArgumentData(CommandTreeNodeTypes.DIMENSION.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<WorldType> createWorldTypeArgument(RawBasicArgumentData<WorldType> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			WorldType.class,
			(cause, args) -> WorldTypes.registry().streamEntries().map(w -> w.key().asString()),
			(cause, args) -> args.length >= data.cursor() + 1 ? WorldTypes.registry().streamEntries().filter(e -> e.key().asString().equals(args[data.cursor()])).map(e -> e.value()).findFirst() : Optional.empty(),
			data.toRawArgumentData(CommandTreeNodeTypes.DIMENSION.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<ChunkGenerator> createChunkGenerator(RawBasicArgumentData<ChunkGenerator> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			ChunkGenerator.class,
			(cause, args) -> plugin.getAPI().getAvailableGenerators().stream(),
			(cause, args) -> args.length >= data.cursor() + 1 ? plugin.getAPI().getCustomGenerator(args[data.cursor()]) : Optional.empty(),
			data.toRawArgumentData(CommandTreeNodeTypes.DIMENSION.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<ServerPlayer> createPlayerArgument(RawBasicArgumentData<ServerPlayer> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			ServerPlayer.class,
			(CommandCause cause, String[] args) -> CommandPackInstance.getInstance().getPlayersData().getTempData().streamOnlinePlayers().filter(name -> Sponge.server().player(name).filter(player -> !player.get(Keys.VANISH_STATE).map(state -> state.invisible()).orElse(false)).isPresent()),
			(CommandCause cause, String[] args) -> args.length >= data.cursor() + 1 ? Sponge.server().onlinePlayers().stream().filter(player -> player.name().equals(args[data.cursor()])).findFirst() : Optional.empty(),
			data.toRawArgumentData(CommandTreeNodeTypes.GAME_PROFILE.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<GameProfile> createGameProfile(RawBasicArgumentData<GameProfile> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			GameProfile.class,
			(CommandCause cause, String[] args) -> Sponge.server().gameProfileManager().cache().all().stream().map(profile -> profile.name().orElse(profile.examinableName())),
			(CommandCause cause, String[] args) -> args.length >= data.cursor() + 1 ? Sponge.server().gameProfileManager().cache().findByName(args[0]) : Optional.empty(),
			data.toRawArgumentData(CommandTreeNodeTypes.GAME_PROFILE.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<CompletableFuture<Optional<User>>> createUserArgument(RawBasicArgumentData<CompletableFuture<Optional<User>>> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			USER_LOAD_CLASS,
			(CommandCause cause, String[] args) -> CommandPackInstance.getInstance().getPlayersData().getTempData().streamOnlinePlayers().filter(name -> Sponge.server().player(name).filter(player -> !player.get(Keys.VANISH_STATE).map(state -> state.invisible()).orElse(false)).isPresent()),
			(CommandCause cause, String[] args) -> args.length >= data.cursor() + 1 ? plugin.getPlayersData().getTempData().getUser(args[data.cursor()]) : Optional.empty(),
			data.toRawArgumentData(CommandTreeNodeTypes.GAME_PROFILE.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<UniqueAccount> createUniqueAccountArgument(RawBasicArgumentData<UniqueAccount> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			UniqueAccount.class,
			(CommandCause cause, String[] args) -> plugin.getEconomy().getEconomyService().streamUniqueAccounts().map(account -> account.identifier()),
			(CommandCause cause, String[] args) -> args.length >= data.cursor() + 1 ? plugin.getEconomy().getEconomyService().streamUniqueAccounts().filter(account -> account.identifier().equals(args[data.cursor()])).findFirst() : Optional.empty(),
			data.toRawArgumentData(CommandTreeNodeTypes.GAME_PROFILE.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<Account> createAccountArgument(RawBasicArgumentData<Account> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			Account.class,
			(CommandCause cause, String[] args) -> getAllAccounts().map(account -> account.identifier()),
			(CommandCause cause, String[] args) -> args.length >= data.cursor() + 1 ? getAllAccounts().filter(account -> account.identifier().equals(args[data.cursor()])).findFirst() : Optional.empty(),
			data.toRawArgumentData(CommandTreeNodeTypes.GAME_PROFILE.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<Profile> createProfileArgument(RawBasicArgumentData<Profile> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			Profile.class,
			(CommandCause cause, String[] args) -> plugin.getPunishmentService().getAllProfileBans().stream().map(ban -> ban.profile().name().orElse(ban.profile().examinableName())),
			(CommandCause cause, String[] args) -> args.length >= data.cursor() + 1 ? plugin.getPunishmentService().getAllProfileBans().stream().filter(ban -> ban.profile().name().orElse(ban.profile().examinableName()).equals(args[data.cursor()])).findFirst() : Optional.empty(),
			data.toRawArgumentData(CommandTreeNodeTypes.GAME_PROFILE.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<Ban.IP> createBanIPArgument(RawBasicArgumentData<Ban.IP> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			Ban.IP.class,
			(cause, args) -> plugin.getPunishmentService().getAllIPBans().stream().map(i -> i.address().getHostAddress()),
			(cause, args) -> args.length == 0 || plugin.getPunishmentService().getAllIPBans().isEmpty() ? Optional.empty() : plugin.getPunishmentService().getAllIPBans().stream().filter(i -> i.address().getHostAddress().equals(args[0])).findFirst(),
			data.toRawArgumentData(CommandTreeNodeTypes.STRING.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<Mute> createMuteArgument(RawBasicArgumentData<Mute> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			Mute.class,
			(CommandCause cause, String[] args) -> plugin.getPunishmentService().getAllMutes().stream().map(mute -> mute.getName()),
			(CommandCause cause, String[] args) -> args.length >= data.cursor() + 1 ? plugin.getPunishmentService().getAllMutes().stream().filter(m -> m.getName().equals(args[data.cursor()])).findFirst() : Optional.empty(),
			data.toRawArgumentData(CommandTreeNodeTypes.GAME_PROFILE.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<Warns> createWarnsArgument(RawBasicArgumentData<Warns> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			Warns.class,
			(CommandCause cause, String[] args) -> plugin.getPunishmentService().getAllWarns().stream().map(w -> w.getName()),
			(CommandCause cause, String[] args) -> args.length >= data.cursor() + 1 ? plugin.getPunishmentService().getAllWarns().stream().filter(w -> w.getName().equals(args[data.cursor()])).findFirst() : Optional.empty(),
			data.toRawArgumentData(CommandTreeNodeTypes.GAME_PROFILE.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<EnchantmentType> createEnchantmentArgument(RawBasicArgumentData<EnchantmentType> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			EnchantmentType.class,
			(CommandCause cause, String[] args) -> EnchantmentTypes.registry().streamEntries().map(e -> e.key().asString()),
			(CommandCause cause, String[] args) -> args.length >= data.cursor() + 1 ? EnchantmentTypes.registry().streamEntries().filter(e -> e.key().asString().equals(args[data.cursor()])).map(e -> e.value()).findFirst() : Optional.empty(),
			data.toRawArgumentData(CommandTreeNodeTypes.RESOURCE_LOCATION.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<Locale> createLocaleArgument(RawBasicArgumentData<Locale> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			Locale.class,
			(CommandCause cause, String[] args) -> Stream.of(EnumLocales.values()).map(EnumLocales::getTag),
			(CommandCause cause, String[] args) -> args.length >= data.cursor() + 1 ? Stream.of(EnumLocales.values()).filter(l -> l.getTag().equalsIgnoreCase(args[data.cursor()])).map(EnumLocales::get).findFirst() : Optional.empty(),
			data.toRawArgumentData(CommandTreeNodeTypes.STRING.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<Currency> createCurrencyArgument(RawBasicArgumentData<Currency> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			Currency.class,
			(CommandCause cause, String[] args) -> RegistryTypes.CURRENCY.get().streamEntries().map(e -> e.key().asString()),
			(CommandCause cause, String[] args) -> args.length >= data.cursor() + 1 ? RegistryTypes.CURRENCY.get().streamEntries().filter(e -> e.key().asString().equals(args[data.cursor()])).findFirst().map(e -> e.value()) : Optional.empty(),
			data.toRawArgumentData(CommandTreeNodeTypes.RESOURCE_LOCATION.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<Kit> createKitArgument(RawBasicArgumentData<Kit> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			Kit.class,
			(CommandCause cause, String[] args) -> plugin.getKitService().getKits().stream().map(Kit::id),
			(CommandCause cause, String[] args) -> args.length >= data.cursor() + 1 ? plugin.getKitService().getKit(args[data.cursor()]) : Optional.empty(),
			data.toRawArgumentData(CommandTreeNodeTypes.STRING.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<Warp> createWarpArgument(RawBasicArgumentData<Warp> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			Warp.class,
			(CommandCause cause, String[] args) -> plugin.getPlayersData().streamAllWarps().filter(warp -> ((args.length == 0 || warp.getName().contains(args[0])) && ((!warp.isPrivate() || (cause.first(ServerPlayer.class).isPresent() && (warp.getOwner().filter(uuid -> cause.first(ServerPlayer.class).get().uniqueId().equals(uuid)).isPresent()))) || cause.hasPermission(Permissions.WARP_STAFF) || cause.hasPermission(Permissions.getWarpPermission(warp.getPlainName()))))).map(Warp::getPlainName),
			(CommandCause cause, String[] args) -> args.length >= data.cursor() + 1 ? plugin.getPlayersData().streamAllWarps().filter(warp -> ((warp.getName().equals(args[0])) && ((!warp.isPrivate() || (cause.first(ServerPlayer.class).isPresent() && (warp.getOwner().filter(uuid -> cause.first(ServerPlayer.class).get().uniqueId().equals(uuid)).isPresent()))) || cause.hasPermission(Permissions.WARP_STAFF) || cause.hasPermission(Permissions.getWarpPermission(warp.getPlainName()))))).findFirst() : Optional.empty(),
			data.toRawArgumentData(CommandTreeNodeTypes.STRING.get().createNode()),
			rawOptional,
			supplier
		);
	}

	public static RawArgument<Duration> createDuration(RawBasicArgumentData<Duration> data, RawOptional rawOptional, ComponentSupplier supplier) {
		return RawArgument.of(
			Duration.class,
			(CommandCause cause, String[] args) -> EMPTY.stream(),
			(CommandCause cause, String[] args) -> args.length >= data.cursor() + 1 ? parseDuration(args[data.cursor()], cause.first(ServerPlayer.class).map(p -> p.locale()).orElse(plugin.getLocales().getLocaleService().getSystemOrDefaultLocale())) : Optional.ofNullable(data.def()),
			data.toRawArgumentData(CommandTreeNodeTypes.STRING.get().createNode()),
			rawOptional,
			supplier
		);
	}

	private static Stream<Account> getAllAccounts() {
		return CommandPackInstance.getInstance().isStarted() ? Stream.concat(plugin.getEconomy().getEconomyService().streamUniqueAccounts(), plugin.getEconomy().getEconomyService().streamVirtualAccounts()) : Stream.empty();
	}

	private static Optional<Duration> parseDuration(String s, Locale locale) throws CommandException {
		s = s.toUpperCase();
		if (!s.contains("T")) {
			if (s.contains("D")) {
				if (s.contains("H") || s.contains("M") || s.contains("S")) {
					s = s.replace("D", "DT");
				}
			} else {
				if (s.startsWith("P")) {
					s = "PT" + s.substring(1);
				} else {
					s = "T" + s;
				}
			}
		}
		if (!s.startsWith("P")) {
			s = "P" + s;
		}
		try {
			return Optional.ofNullable(Duration.parse(s));
		} catch (final DateTimeParseException ex) {
			throw new CommandException(plugin.getLocales().getLocale(locale).getCommandExceptions().getDurationNotPresent());
		}
	}

}
