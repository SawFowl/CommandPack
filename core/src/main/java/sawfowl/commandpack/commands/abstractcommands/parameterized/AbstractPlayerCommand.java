package sawfowl.commandpack.commands.abstractcommands.parameterized;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.commands.parameterized.ParameterizedPlayerCommand;

public abstract class AbstractPlayerCommand extends AbstractParameterizedCommand implements ParameterizedPlayerCommand {

	public AbstractPlayerCommand(CommandPackInstance plugin) {
		super(plugin);
	}
/*
	protected boolean canUse(CommandCause cause) {
		return cause.first(ServerPlayer.class).filter(player -> hasPermission(player)).isPresent();
	}

	@Override
	public Command.Parameterized fastBuild() {
		return (permission() == null ?  builderNoPerm() : builderNoPerm().executionRequirements(cause -> canUse(cause))).build();
	}

	private boolean hasPermission(ServerPlayer player) {
		return player.hasPermission(permission());
	}
*/
}
