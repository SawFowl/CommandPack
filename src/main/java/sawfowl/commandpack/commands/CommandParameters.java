package sawfowl.commandpack.commands;

import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.command.parameter.Parameter.Value.Builder;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class CommandParameters {

	public static final Builder<ServerPlayer> PLAYER = Parameter.player().key("Player");

	public static final Builder<String> USER = Parameter.string().key("User");

	public static final Builder<Boolean> BOOLEAN = Parameter.bool();

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

}
