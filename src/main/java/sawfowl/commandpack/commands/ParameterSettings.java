package sawfowl.commandpack.commands;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;

public class ParameterSettings<T> {

	private final Parameter.Value<T> parameter;
	private final Boolean optional;
	private final Boolean optionalForPlayer;
	private final Object[] path;
	public ParameterSettings(Parameter.Value<T> parameter, boolean optional, boolean optionalForPlayer, Object... path) {
		this.parameter = parameter;
		this.optional = optional;
		this.optionalForPlayer = optionalForPlayer;
		this.path = path;
	}

	public Parameter.Value<T> getParameterUnknownType() {
		return parameter;
	}

	@SuppressWarnings({ "hiding", "unchecked" })
	public <T> Parameter.Value<T> getParameter(@NonNull final Class<T> object) {
		return (Value<T>) parameter;
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
