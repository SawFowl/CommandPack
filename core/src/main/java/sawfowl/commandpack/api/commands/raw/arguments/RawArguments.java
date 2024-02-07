package sawfowl.commandpack.api.commands.raw.arguments;

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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.registrar.tree.CommandTreeNodeTypes;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.Profile;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.world.WorldType;
import org.spongepowered.api.world.WorldTypes;
import org.spongepowered.api.world.server.ServerWorld;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.api.data.player.Warp;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warns;
import sawfowl.localeapi.api.EnumLocales;

/**
 * This class is designed to quickly create some arguments for {@link RawSettings} commands.<br>
 * Some more variants may be added in the future.
 * 
 * @author SawFowl
 *
 */
public class RawArguments {

	public static final List<String> EMPTY = new ArrayList<>();
	private static final CommandPack plugin = CommandPack.getInstance();

	public static RawArgument<String> createStringArgument(@NotNull Stream<String> variants, boolean optional, boolean optionalForConsole, int cursor, @Nullable String def, Object[] localesPath) {
		return RawArgument.of(
			String.class,
			CommandTreeNodeTypes.STRING.get().createNode(),
			(cause, args) -> variants,
			(cause, args) -> args.length >= cursor + 1 ? Optional.ofNullable(variants.filter(var -> var.equals(args[cursor])).findFirst().orElse(def)) : Optional.ofNullable(def),
			"String",
			optional,
			optionalForConsole,
			cursor,
			null,
			localesPath
		);
	}

	public static RawArgument<String> createStringArgument(@NotNull Collection<String> variants, boolean optional, boolean optionalForConsole, int cursor, @Nullable String def, Object[] localesPath) {
		return RawArgument.of(
			String.class,
			CommandTreeNodeTypes.STRING.get().createNode(),
			(cause, args) -> variants.size() > 10000 ? variants.parallelStream() : variants.stream(),
			(cause, args) -> args.length >= cursor + 1 ? (variants.isEmpty() ? Optional.ofNullable(args[cursor]) : Optional.ofNullable(variants.stream().filter(var -> var.equals(args[cursor])).findFirst().orElse(def))) : Optional.ofNullable(def),
			"String",
			optional,
			optionalForConsole,
			cursor,
			null,
			localesPath
		);
	}

	public static RawArgument<String> createStringArgument(@NotNull Collection<String> variants, boolean optional, boolean optionalForConsole, int cursor, @Nullable String def, String permission, Object[] localesPath) {
		return RawArgument.of(
			String.class,
			CommandTreeNodeTypes.STRING.get().createNode(),
			(cause, args) -> variants.size() > 10000 ? variants.parallelStream() : variants.stream(),
			(cause, args) -> args.length >= cursor + 1 ? (variants.isEmpty() ? Optional.ofNullable(args[cursor]) : Optional.ofNullable(variants.stream().filter(var -> var.equals(args[cursor])).findFirst().orElse(def))) : Optional.ofNullable(def),
			"String",
			optional,
			optionalForConsole,
			cursor,
			permission,
			localesPath
		);
	}

	public static RawArgument<String> createRemainingJoinedStringsArgument(boolean optional, boolean optionalForConsole, int cursor, @Nullable String def, Object[] localesPath) {
		return RawArgument.of(
			String.class,
			CommandTreeNodeTypes.STRING.get().createNode(),
			(cause, args) -> Stream.empty(),
			(cause, args) -> args.length >= cursor + 1 ?  Optional.ofNullable(String.join(" ", Stream.of(Arrays.copyOfRange(args, cursor, args.length)).filter(string -> string != null).toArray(String[]::new))) : Optional.ofNullable(def),
			"Strings...",
			optional,
			optionalForConsole,
			cursor,
			null,
			localesPath
		);
	}

	public static RawArgument<Integer> createIntegerArgument(@NotNull Collection<Integer> variants, boolean optional, boolean optionalForConsole, int cursor, @Nullable Integer def, Object[] localesPath) {
		return RawArgument.of(
			Integer.class,
			CommandTreeNodeTypes.INTEGER.get().createNode(),
			(cause, args) -> (variants.size() > 10000 ? variants.parallelStream() : variants.stream()).map(String::valueOf),
			(cause, args) -> args.length >= cursor + 1 && NumberUtils.isParsable(args[cursor]) && (variants.isEmpty() || variants.stream().filter(a -> a == NumberUtils.createInteger(args[cursor])).findFirst().isPresent()) ? Optional.ofNullable(NumberUtils.createInteger(args[cursor])) : Optional.ofNullable(def),
			"IntValue",
			optional,
			optionalForConsole,
			cursor,
			null,
			localesPath
		);
	}

	public static RawArgument<Long> createLongArgument(@NotNull Collection<Long> variants, boolean optional, boolean optionalForConsole, int cursor, @Nullable Long def, Object[] localesPath) {
		return RawArgument.of(
			Long.class,
			CommandTreeNodeTypes.LONG.get().createNode(),
			(cause, args) -> (variants.size() > 10000 ? variants.parallelStream() : variants.stream()).map(String::valueOf),
			(cause, args) -> args.length >= cursor + 1 && NumberUtils.isParsable(args[cursor]) && (variants.isEmpty() || variants.stream().filter(a -> a == NumberUtils.createLong(args[cursor])).findFirst().isPresent()) ? Optional.ofNullable(NumberUtils.createLong(args[cursor])) : Optional.ofNullable(def),
			"LongValue",
			optional,
			optionalForConsole,
			cursor,
			null,
			localesPath
		);
	}

	public static RawArgument<Double> createDoubleArgument(@NotNull Collection<Double> variants, boolean optional, boolean optionalForConsole, int cursor, @Nullable Double def, Object[] localesPath) {
		return RawArgument.of(
			Double.class,
			CommandTreeNodeTypes.DOUBLE.get().createNode(),
			(cause, args) -> (variants.size() > 10000 ? variants.parallelStream() : variants.stream()).map(String::valueOf),
			(cause, args) -> args.length >= cursor + 1 && NumberUtils.isParsable(args[cursor]) && (variants.isEmpty() || variants.stream().filter(a -> a == NumberUtils.createDouble(args[cursor])).findFirst().isPresent()) ? Optional.ofNullable(NumberUtils.createDouble(args[cursor])) : Optional.ofNullable(def),
			"DoubleValue",
			optional,
			optionalForConsole,
			cursor,
			null,
			localesPath
		);
	}

	public static RawArgument<BigDecimal> createBigDecimalArgument(@NotNull Collection<BigDecimal> variants, boolean optional, boolean optionalForConsole, int cursor, @Nullable BigDecimal def, Object[] localesPath) {
		return RawArgument.of(
			BigDecimal.class,
			CommandTreeNodeTypes.DOUBLE.get().createNode(),
			(cause, args) -> (variants.size() > 10000 ? variants.parallelStream() : variants.stream()).map(String::valueOf),
			(cause, args) -> args.length >= cursor + 1 && NumberUtils.isParsable(args[cursor]) && (variants.isEmpty() || variants.stream().filter(a -> a.doubleValue() == NumberUtils.createDouble(args[cursor])).findFirst().isPresent()) ? Optional.ofNullable(NumberUtils.createBigDecimal(args[cursor])) : Optional.ofNullable(def),
			"BigDecimalValue",
			optional,
			optionalForConsole,
			cursor,
			null,
			localesPath
		);
	}

	public static RawArgument<Boolean> createBooleanArgument(boolean optional, boolean optionalForConsole, int cursor, @Nullable Boolean def, Object[] localesPath) {
		return RawArgument.of(
			Boolean.class,
			CommandTreeNodeTypes.BOOL.get().createNode(),
			(cause, args) -> Stream.of("true", "false"),
			(cause, args) -> args.length >= cursor + 1 && BooleanUtils.toBooleanObject(args[cursor]) != null ? Optional.ofNullable(BooleanUtils.toBooleanObject(args[cursor])) : Optional.ofNullable(def),
			"BooleanValue",
			optional,
			optionalForConsole,
			cursor,
			null,
			localesPath
		);
	}

	public static RawArgument<ServerWorld> createWorldArgument(boolean optional, boolean optionalForConsole, int cursor, @Nullable ServerWorld def, Object[] localesPath) {
		return RawArgument.of(
			ServerWorld.class,
			CommandTreeNodeTypes.DIMENSION.get().createNode(),
			(cause, args) -> Sponge.server().worldManager().worlds().stream().map(w -> w.key().asString()),
			(cause, args) -> args.length >= cursor + 1 ? Optional.ofNullable(Sponge.server().worldManager().worlds().stream().filter(w -> w.key().asString().equals(args[cursor])).findFirst().orElse(def)) : Optional.ofNullable(def),
			"World",
			optional,
			optionalForConsole,
			cursor,
			null,
			localesPath
		);
	}

	public static RawArgument<ServerWorld> createWorldArgument(boolean optional, boolean optionalForConsole, int cursor, @Nullable ServerWorld def, String permission, Object[] localesPath) {
		return RawArgument.of(
			ServerWorld.class,
			CommandTreeNodeTypes.DIMENSION.get().createNode(),
			(cause, args) -> Sponge.server().worldManager().worlds().stream().map(w -> w.key().asString()),
			(cause, args) -> args.length >= cursor + 1 ? Optional.ofNullable(Sponge.server().worldManager().worlds().stream().filter(w -> w.key().asString().equals(args[cursor])).findFirst().orElse(def)) : Optional.ofNullable(def),
			"World",
			optional,
			optionalForConsole,
			cursor,
			permission,
			localesPath
		);
	}

	public static RawArgument<WorldType> createWorldTypeArgument(boolean optional, boolean optionalForConsole, int cursor, Object[] localesPath) {
		return RawArgument.of(
			WorldType.class,
			CommandTreeNodeTypes.DIMENSION.get().createNode(),
			(cause, args) -> WorldTypes.registry().streamEntries().map(w -> w.key().asString()),
			(cause, args) -> args.length >= cursor + 1 ? WorldTypes.registry().streamEntries().filter(e -> e.key().asString().equals(args[cursor])).map(e -> e.value()).findFirst() : Optional.empty(),
			"WorldType",
			optional,
			optionalForConsole,
			cursor,
			null,
			localesPath
		);
	}

	public static RawArgument<ServerPlayer> createPlayerArgument(boolean optional, boolean optionalForConsole, int cursor, Object[] localesPath) {
		return RawArgument.of(
			ServerPlayer.class,
			CommandTreeNodeTypes.GAME_PROFILE.get().createNode(),
			(CommandCause cause, String[] args) -> Sponge.server().onlinePlayers().stream().filter(player -> !player.get(Keys.VANISH_STATE).map(state -> state.invisible()).orElse(false)).map(ServerPlayer::name),
			(CommandCause cause, String[] args) -> args.length >= cursor + 1 ? Sponge.server().onlinePlayers().stream().filter(player -> player.name().equals(args[cursor])).findFirst() : Optional.empty(),
			"Player",
			optional,
			optionalForConsole,
			cursor,
			null,
			localesPath
		);
	}

	public static RawArgument<ServerPlayer> createPlayerArgument(boolean optional, boolean optionalForConsole, int cursor, String key, Object[] localesPath) {
		return RawArgument.of(
			ServerPlayer.class,
			CommandTreeNodeTypes.GAME_PROFILE.get().createNode(),
			(CommandCause cause, String[] args) -> Sponge.server().onlinePlayers().stream().filter(player -> !player.get(Keys.VANISH_STATE).map(state -> state.invisible()).orElse(false)).map(ServerPlayer::name),
			(CommandCause cause, String[] args) -> args.length >= cursor + 1 ? Sponge.server().onlinePlayers().stream().filter(player -> player.name().equals(args[cursor])).findFirst() : Optional.empty(),
			"Player",
			optional,
			optionalForConsole,
			cursor,
			null,
			localesPath
		);
	}

	public static RawArgument<UniqueAccount> createUniqueAccountArgument(boolean optional, boolean optionalForConsole, int cursor, String permission, Object[] localesPath) {
		return RawArgument.of(
			UniqueAccount.class,
			CommandTreeNodeTypes.GAME_PROFILE.get().createNode(),
			(CommandCause cause, String[] args) -> plugin.getEconomy().getEconomyService().streamUniqueAccounts().map(account -> account.identifier()),
			(CommandCause cause, String[] args) -> args.length >= cursor + 1 ? plugin.getEconomy().getEconomyService().streamUniqueAccounts().filter(account -> account.identifier().equals(args[cursor])).findFirst() : Optional.empty(),
			"Player",
			optional,
			optionalForConsole,
			cursor,
			permission,
			localesPath
		);
	}

	public static RawArgument<Account> createAccountArgument(boolean optional, boolean optionalForConsole, int cursor, String permission, Object[] localesPath) {
		return RawArgument.of(
			Account.class,
			CommandTreeNodeTypes.GAME_PROFILE.get().createNode(),
			(CommandCause cause, String[] args) -> getAllAccounts().map(account -> account.identifier()),
			(CommandCause cause, String[] args) -> args.length >= cursor + 1 ? getAllAccounts().filter(account -> account.identifier().equals(args[cursor])).findFirst() : Optional.empty(),
			"Player",
			optional,
			optionalForConsole,
			cursor,
			permission,
			localesPath
		);
	}

	public static RawArgument<Profile> createProfileArgument(boolean optional, boolean optionalForConsole, int cursor, String permission, Object[] localesPath) {
		return RawArgument.of(
			Profile.class,
			CommandTreeNodeTypes.GAME_PROFILE.get().createNode(),
			(CommandCause cause, String[] args) -> plugin.getPunishmentService().getAllProfileBans().stream().map(ban -> ban.profile().name().orElse(ban.profile().examinableName())),
			(CommandCause cause, String[] args) -> args.length >= cursor + 1 ? plugin.getPunishmentService().getAllProfileBans().stream().filter(ban -> ban.profile().name().orElse(ban.profile().examinableName()).equals(args[cursor])).findFirst() : Optional.empty(),
			"Player",
			optional,
			optionalForConsole,
			cursor,
			permission,
			localesPath
		);
	}

	public static RawArgument<Ban.IP> createBanIPArgument(boolean optional, boolean optionalForConsole, int cursor, String permission, Object[] localesPath) {
		return RawArgument.of(
			Ban.IP.class,
			null,
			(cause, args) -> plugin.getPunishmentService().getAllIPBans().stream().map(i -> i.address().getHostAddress()),
			(cause, args) -> args.length == 0 || plugin.getPunishmentService().getAllIPBans().isEmpty() ? Optional.empty() : plugin.getPunishmentService().getAllIPBans().stream().filter(i -> i.address().getHostAddress().equals(args[0])).findFirst(),
			"Address",
			false,
			false,
			cursor,
			permission,
			localesPath
		);
	}

	public static RawArgument<Mute> createMuteArgument(boolean optional, boolean optionalForConsole, int cursor, String permission, Object[] localesPath) {
		return RawArgument.of(Mute.class,
			CommandTreeNodeTypes.GAME_PROFILE.get().createNode(),
			(CommandCause cause, String[] args) -> plugin.getPunishmentService().getAllMutes().stream().map(mute -> mute.getName()),
			(CommandCause cause, String[] args) -> args.length >= cursor + 1 ? plugin.getPunishmentService().getAllMutes().stream().filter(m -> m.getName().equals(args[cursor])).findFirst() : Optional.empty(),
			"Player",
			optional,
			optionalForConsole,
			cursor,
			permission,
			localesPath
		);
	}

	public static RawArgument<Warns> createWarnsArgument(boolean optional, boolean optionalForConsole, int cursor, String permission, Object[] localesPath) {
		return RawArgument.of(Warns.class,
			CommandTreeNodeTypes.GAME_PROFILE.get().createNode(),
			(CommandCause cause, String[] args) -> plugin.getPunishmentService().getAllWarns().stream().map(w -> w.getName()),
			(CommandCause cause, String[] args) -> args.length >= cursor + 1 ? plugin.getPunishmentService().getAllWarns().stream().filter(w -> w.getName().equals(args[cursor])).findFirst() : Optional.empty(),
			"Player",
			optional,
			optionalForConsole,
			cursor,
			permission,
			localesPath
		);
	}

	public static RawArgument<EnchantmentType> createEnchantmentArgument(boolean optional, boolean optionalForConsole, int cursor, Object[] localesPath) {
		return RawArgument.of(EnchantmentType.class,
			CommandTreeNodeTypes.RESOURCE_LOCATION.get().createNode(),
			(CommandCause cause, String[] args) -> EnchantmentTypes.registry().streamEntries().map(e -> e.key().asString()),
			(CommandCause cause, String[] args) -> args.length >= cursor + 1 ? EnchantmentTypes.registry().streamEntries().filter(e -> e.key().asString().equals(args[cursor])).map(e -> e.value()).findFirst() : Optional.empty(),
			"Enchant",
			optional,
			optionalForConsole,
			cursor,
			null,
			localesPath
		);
	}

	public static RawArgument<Locale> createLocaleArgument(boolean optional, boolean optionalForConsole, int cursor, Object[] localesPath) {
		return RawArgument.of(Locale.class,
			null,
			(CommandCause cause, String[] args) -> Stream.of(EnumLocales.values()).map(EnumLocales::getTag),
			(CommandCause cause, String[] args) -> args.length >= cursor + 1 ? Stream.of(EnumLocales.values()).filter(l -> l.getTag().equalsIgnoreCase(args[cursor])).map(EnumLocales::get).findFirst() : Optional.empty(),
			"Locale",
			optional,
			optionalForConsole,
			cursor,
			null,
			localesPath
		);
	}

	public static RawArgument<Currency> createCurrencyArgument(boolean optional, boolean optionalForConsole, int cursor, Object[] localesPath) {
		return RawArgument.of(Currency.class,
			null,
			(CommandCause cause, String[] args) -> Sponge.game().findRegistry(RegistryTypes.CURRENCY).map(registry -> registry .streamEntries().map(e -> e.key().asString())).orElse(Stream.empty()),
			(CommandCause cause, String[] args) -> args.length >= cursor + 1 ? Sponge.game().findRegistry(RegistryTypes.CURRENCY).map(registry -> registry.streamEntries()).orElse(Stream.empty()).filter(e -> e.key().asString().equals(args[cursor])).findFirst().map(e -> e.value()) : Optional.empty(),
			"Currency",
			optional,
			optionalForConsole,
			cursor,
			null,
			localesPath
		);
	}

	public static RawArgument<Kit> createKitArgument(boolean optional, boolean optionalForConsole, int cursor, Object[] localesPath) {
		return RawArgument.of(
			Kit.class,
			null,
			(CommandCause cause, String[] args) -> plugin.getKitService().getKits().stream().map(Kit::id),
			(CommandCause cause, String[] args) -> args.length >= cursor + 1 ? plugin.getKitService().getKit(args[cursor]) : Optional.empty(),
			"Kit",
			optional,
			optionalForConsole,
			cursor,
			null,
			localesPath
		);
	}

	public static RawArgument<Warp> createWarpArgument(boolean optional, boolean optionalForConsole, int cursor, Object[] localesPath) {
		return RawArgument.of(Warp.class,
			null,
			(CommandCause cause, String[] args) -> plugin.getPlayersData().streamAllWarps().filter(warp -> (args.length == 0 || warp.getName().startsWith(args[0]) && ((!warp.isPrivate() || (cause.first(ServerPlayer.class).isPresent() && plugin.getPlayersData().getPlayerData(cause.first(ServerPlayer.class).get().uniqueId()).get().containsWarp(warp.getPlainName()))) || cause.hasPermission(Permissions.WARP_STAFF) || cause.hasPermission(Permissions.getWarpPermission(warp.getPlainName()))))).map(Warp::getPlainName),
			(CommandCause cause, String[] args) -> args.length >= cursor + 1 ? plugin.getPlayersData().streamAllWarps().filter(warp -> (args.length == 0 || warp.getName().startsWith(args[0]) && ((!warp.isPrivate() || (cause.first(ServerPlayer.class).isPresent() && plugin.getPlayersData().getPlayerData(cause.first(ServerPlayer.class).get().uniqueId()).get().containsWarp(warp.getPlainName()))) || cause.hasPermission(Permissions.WARP_STAFF) || cause.hasPermission(Permissions.getWarpPermission(warp.getPlainName()))))).findFirst() : Optional.empty(),
			"Warp",
			optional,
			optionalForConsole,
			cursor,
			null,
			localesPath
		);
	}

	private static Stream<Account> getAllAccounts() {
		return CommandPack.getInstance().isStarted() ? Stream.concat(plugin.getEconomy().getEconomyService().streamUniqueAccounts(), plugin.getEconomy().getEconomyService().streamVirtualAccounts()) : Stream.empty();
	}

}
