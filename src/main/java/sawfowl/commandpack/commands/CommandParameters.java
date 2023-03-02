package sawfowl.commandpack.commands;

import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value.Builder;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class CommandParameters {

	public static final Builder<ServerPlayer> PLAYER = Parameter.player().optional().key("Player");

	public static final Parameter.Value<String> USER = Parameter.string().optional().key("User").build();

}
