package sawfowl.commandpack.commands.parameterized.settings;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;

public class ParameterSettings {

	private final Parameter.Value<?> parameter;
	private final Boolean optional;
	private final Boolean optionalForConsole;
	private final Object[] path;
	public ParameterSettings(Parameter.Value<?> parameter, boolean optionalForConsole, Object... pathException) {
		this.parameter = parameter;
		this.optional = parameter.isOptional();
		this.optionalForConsole = parameter.isOptional() && optionalForConsole;
		this.path = pathException;
	}

	public Parameter.Value<?> getParameterUnknownType() {
		return parameter;
	}

	@SuppressWarnings("unchecked")
	public <T> Parameter.Value<T> getParameter(@NonNull final Class<T> object) {
		try {
			return (Value<T>) this.parameter;
		} catch (ClassCastException e) {
			return null;
		}
	}

	public <T> Optional<T> getParameterValue(@NonNull final Class<T> object, CommandContext context) {
		return getParameter(object) != null ? context.one(getParameter(object)) : Optional.empty();
	}

	public boolean isOptional() {
		return optional;
	}

	public boolean isOptionalForConsole() {
		return optionalForConsole;
	}

	public Object[] getPath() {
		return path;
	}

}
