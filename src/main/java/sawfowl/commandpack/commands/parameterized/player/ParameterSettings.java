package sawfowl.commandpack.commands.parameterized.player;

import org.spongepowered.api.command.parameter.Parameter;

public class ParameterSettings {

	private final Parameter.Value<?> parameter;
	private final Boolean optional;
	private final Boolean optionalForPlayer;
	private final Object[] path;
	public ParameterSettings(Parameter.Value<?> parameter, boolean optional, boolean optionalForPlayer, Object... path) {
		this.parameter = parameter;
		this.optional = optional;
		this.optionalForPlayer = optionalForPlayer;
		this.path = path;
	}

	public Parameter.Value<?> getParameter() {
		return parameter;
	}

	public boolean isOptional() {
		return optional;
	}

	public boolean isOptionalForPlayer() {
		return optionalForPlayer;
	}

	public Object[] getPath() {
		return path;
	}

}
