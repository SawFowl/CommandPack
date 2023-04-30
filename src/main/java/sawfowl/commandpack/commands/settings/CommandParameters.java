package sawfowl.commandpack.commands.settings;

import java.time.Duration;
import java.util.stream.Stream;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.command.parameter.Parameter.Value.Builder;
import org.spongepowered.api.command.parameter.managed.standard.ResourceKeyedValueParameters;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;

import sawfowl.commandpack.Permissions;
import sawfowl.localeapi.api.EnumLocales;

public class CommandParameters {

	public static final Builder<ServerPlayer> PLAYER = Parameter.player().key("Player");

	public static final Builder<String> USER = Parameter.string().key("User").completer(ResourceKeyedValueParameters.PLAYER.get());

	public static final Builder<Boolean> BOOLEAN = Parameter.bool();

	public static final Builder<String> STRING = Parameter.string();

	public static final Builder<String> STRINGS = Parameter.remainingJoinedStrings();

	public static final Builder<Integer> INTEGER = Parameter.integerNumber();

	public static final Builder<Double> DOUBLE = Parameter.doubleNumber();

	public static final Builder<ServerLocation> LOCATION = Parameter.location().key("Location");

	public static final Builder<ServerWorld> WORLD = Parameter.world().key("World");

	public static final Builder<String> INVENTORY_TYPES = Parameter.choices("all", "equipment", "hotbar", "primary", "enderchest").key("InventoryType");

	public static final Value<Duration> DURATION = Parameter.duration().key("Duration").build();

	public static final Value<String> REPAIR = Parameter.choices("all", "armor", "hands").optional().key("Repair").requiredPermission(Permissions.REPAIR_SELECT).build();

	public static final Value<String> LOCALES = Parameter.choices(Stream.of(EnumLocales.values()).map(EnumLocales::getTag).toArray(String[]::new)).key("Locale").requiredPermission(Permissions.REPAIR_SELECT).build();

	public static final Value<String> PLUGINS = Parameter.choices(Sponge.pluginManager().plugins().stream().map(p -> p.metadata().id()).toArray(String[]::new)).key("Plugin").build();

	public static Value<ServerPlayer> createPlayer(boolean optional) {
		return (optional ? PLAYER.optional() : PLAYER).build();
	}

	public static Value<ServerPlayer> createPlayer(String permission, boolean optional) {
		return (optional ? PLAYER.optional() : PLAYER).requiredPermission(permission).build();
	}

	public static Value<String> createUser(boolean optional) {
		return (optional ? USER.optional() : USER).build();
	}

	public static Value<String> createUser(String permission, boolean optional) {
		return (optional ? USER.optional() : USER).requiredPermission(permission).build();
	}

	public static Value<Boolean> createBoolean(String key, boolean optional) {
		return (optional ? BOOLEAN.optional() : BOOLEAN).key(key).build();
	}

	public static Value<Boolean> createBoolean(String key, String permission, boolean optional) {
		return (optional ? BOOLEAN.optional() : BOOLEAN).key(key).requiredPermission(permission).build();
	}

	public static Value<String> createString(String key, boolean optional) {
		return (optional ? STRING.optional() : STRING).key(key).build();
	}

	public static Value<String> createStrings(String key, boolean optional) {
		return (optional ? STRINGS.optional() : STRINGS).key(key).build();
	}

	public static Value<String> createString(String key, String permission, boolean optional) {
		return (optional ? STRING.optional() : STRING).key(key).requiredPermission(permission).build();
	}

	public static Value<Integer> createInteger(String key, boolean optional) {
		return (optional ? INTEGER.optional() : INTEGER).key(key).build();
	}

	public static Value<Integer> createInteger(String key, String permission, boolean optional) {
		return (optional ? INTEGER.optional() : INTEGER).key(key).requiredPermission(permission).build();
	}

	public static Value<Integer>  createRangedInteger(String key, int min, int max, boolean optional) {
		Builder<Integer> builder = Parameter.rangedInteger(min, max).key(key);
		if(optional) builder.optional();
		return builder.build();
	}

	public static Value<Integer>  createRangedInteger(String key, String permission, int min, int max, boolean optional) {
		Builder<Integer> builder = Parameter.rangedInteger(min, max).requiredPermission(permission).key(key);
		if(optional) builder.optional();
		return builder.build();
	}

	public static Value<Double> createDouble(String key, boolean optional) {
		return (optional ? DOUBLE.optional() : DOUBLE).key(key).build();
	}

	public static Value<Double> createRangedDouble(String key, boolean optional, double min, double max) {
		Builder<Double> builder = Parameter.rangedDouble(min, max).key(key);
		if(optional) builder.optional();
		return builder.build();
	}

	public static Value<Double> createDouble(String key, String permission, boolean optional) {
		return (optional ? DOUBLE.optional() : DOUBLE).key(key).requiredPermission(permission).build();
	}

	public static Value<ServerLocation> createLocation(boolean optional) {
		return (optional ? LOCATION.optional() : LOCATION).build();
	}

	public static Value<String> createInventoryTypes(String permission, boolean optional) {
		return (optional ? INVENTORY_TYPES.optional() : INVENTORY_TYPES).requiredPermission(permission).build();
	}

	public static Value<String> createInventoryTypes(boolean optional) {
		return (optional ? INVENTORY_TYPES.optional() : INVENTORY_TYPES).build();
	}

	public static Value<ServerWorld> createWorld(String permission, boolean optional) {
		return (optional ? WORLD.optional() : WORLD).requiredPermission(permission).build();
	}

	public static Value<ServerWorld> createWorld(boolean optional) {
		return (optional ? WORLD.optional() : WORLD).build();
	}

}