package sawfowl.commandpack.commands;

import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.command.parameter.Parameter.Value.Builder;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class CommandParameters {

	public static final Builder<ServerPlayer> PLAYER = Parameter.player().optional().key("Player");

	public static final Builder<String> USER = Parameter.string().optional().key("User");

	public static Value<ServerPlayer> createPlayer() {
		return PLAYER.build();
	}

	public static Value<ServerPlayer> createPlayer(String permission) {
		return PLAYER.requiredPermission(permission).build();
	}

	public static Value<String> createUser() {
		return USER.build();
	}

	public static Value<String> createUser(String permission) {
		return USER.requiredPermission(permission).build();
	}

}
