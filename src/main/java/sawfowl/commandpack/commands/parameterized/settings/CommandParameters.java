package sawfowl.commandpack.commands.parameterized.settings;

import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.command.parameter.Parameter.Value.Builder;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerLocation;

import sawfowl.commandpack.Permissions;

public class CommandParameters {

	public static final Builder<ServerPlayer> PLAYER = Parameter.player().key("Player");

	public static final Builder<String> USER = Parameter.string().key("User");

	public static final Builder<Boolean> BOOLEAN = Parameter.bool();

	public static final Builder<String> STRING = Parameter.string();

	public static final Builder<ServerLocation> LOCATION = Parameter.location().key("Location");

	public static final Builder<String> INVENTORY_TYPES = Parameter.choices("all", "equipment", "hotbar", "primary", "enderchest").key("InventoryType");

	public static final Value<String> REPAIR = Parameter.choices("all", "armor", "hands").optional().key("Repair").requiredPermission(Permissions.REPAIR_SELECT).build();

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

	public static Value<String> createString(String key, String permission, boolean optional) {
		return (optional ? STRING.optional() : STRING).key(key).requiredPermission(permission).build();
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

}
