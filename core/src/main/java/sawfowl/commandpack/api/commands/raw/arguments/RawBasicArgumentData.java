package sawfowl.commandpack.api.commands.raw.arguments;

import java.time.Duration;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.command.registrar.tree.CommandTreeNode;
import org.spongepowered.api.command.registrar.tree.CommandTreeNode.Argument;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.Profile;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.world.WorldType;
import org.spongepowered.api.world.generation.ChunkGenerator;
import org.spongepowered.api.world.server.ServerWorld;

import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.api.data.player.Warp;
import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.api.data.punishment.Warns;

/**
 * 
 * @param key - Argument {@link String} key
 * @param <T> - Default Object.
 * @param cursor - The number of the argument in the array of arguments input.
 * @param permission - The permission required to use the argument. Can be `null`.
 * @param requiredArgs - An enumeration of the arguments that are required to use this argument.
 * 
 * @author SawFowl
 */
public record RawBasicArgumentData<T>(@Nullable T def, String key, int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {

	public <C extends CommandTreeNode<C>> RawArgumentData<C> toRawArgumentData(Argument<C> argumentNodeType) {
		return new RawArgumentData<C>(key, argumentNodeType, cursor, permission, requiredArgs);
	}

	public static RawBasicArgumentData<ServerWorld> createWorld(ServerWorld def, int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return create(def, "World", cursor, permission, requiredArgs);
	}

	public static RawBasicArgumentData<WorldType> createWorldType(int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return create("WorldType", cursor, permission, requiredArgs);
	}

	public static RawBasicArgumentData<ServerPlayer> createPlayer(int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return create("Player", cursor, permission, requiredArgs);
	}

	public static RawBasicArgumentData<GameProfile> createGameProfile(int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return create("GameProfile", cursor, permission, requiredArgs);
	}

	public static RawBasicArgumentData<CompletableFuture<Optional<User>>> createUser(int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return create("User", cursor, permission, requiredArgs);
	}

	public static RawBasicArgumentData<UniqueAccount> createUniqueAccount(int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return create("UniqueAccount", cursor, permission, requiredArgs);
	}

	public static RawBasicArgumentData<Account> createAccount(int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return create("Account", cursor, permission, requiredArgs);
	}

	public static RawBasicArgumentData<Profile> createProfile(int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return create("Profile", cursor, permission, requiredArgs);
	}

	public static RawBasicArgumentData<Ban.IP> createBanIP(int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return create("IP", cursor, permission, requiredArgs);
	}

	public static RawBasicArgumentData<Mute> createMute(int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return create("Player", cursor, permission, requiredArgs);
	}

	public static RawBasicArgumentData<Warns> createWarns(int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return create("Player", cursor, permission, requiredArgs);
	}

	public static RawBasicArgumentData<EnchantmentType> createEnchantmentType(int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return create("Enchantment", cursor, permission, requiredArgs);
	}

	public static RawBasicArgumentData<Locale> createLocale(int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return create("Locale", cursor, permission, requiredArgs);
	}

	public static RawBasicArgumentData<Currency> createCurrency(int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return create("Currency", cursor, permission, requiredArgs);
	}

	public static RawBasicArgumentData<Kit> createKit(int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return create("Kit", cursor, permission, requiredArgs);
	}

	public static RawBasicArgumentData<Warp> createWarp(int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return create("Warp", cursor, permission, requiredArgs);
	}

	public static RawBasicArgumentData<Duration> createDuration(int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return create("Duration", cursor, permission, requiredArgs);
	}

	public static RawBasicArgumentData<ChunkGenerator> createChunkGenerator(int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return create("ChunkGenerator", cursor, permission, requiredArgs);
	}

	private static <T> RawBasicArgumentData<T> create(T def, String key,  int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return new RawBasicArgumentData<T>(def, key, cursor, permission, requiredArgs);
	}

	private static <T> RawBasicArgumentData<T> create(String key, int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {
		return new RawBasicArgumentData<T>(null, key, cursor, permission, requiredArgs);
	}

}
