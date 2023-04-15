package sawfowl.commandpack.commands.settings;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;

import org.jetbrains.annotations.NotNull;

import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;

public class ParameterSettings implements sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings {

	private Parameter.Value<?> parameter;
	private Boolean optional;
	private Boolean optionalForConsole = false;
	private Object[] path = {"NaN"};
	private String key;
	public ParameterSettings() {}
	public ParameterSettings(Parameter.Value<?> parameter, boolean optionalForConsole, Object... pathException) {
		this.key = parameter.key().key();
		this.parameter = parameter;
		this.optional = parameter.isOptional();
		this.optionalForConsole = optional && optionalForConsole;
		this.path = pathException;
	}

	public Builder builder() {
		return new Builder();
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public Parameter.Value<?> getParameterUnknownType() {
		return parameter;
	}

	@Override
	public <T> Optional<T> getParameterValue(@NonNull final Class<T> object, CommandContext context) {
		Parameter.Value<T> value = getParameter(object);
		return value != null ? context.one(value) : Optional.empty();
	}

	@Override
	public boolean isOptional() {
		return optional;
	}

	@Override
	public boolean isOptionalForConsole() {
		return optionalForConsole;
	}

	@Override
	public Object[] getPath() {
		return path;
	}

	@SuppressWarnings("unchecked")
	private <T> Parameter.Value<T> getParameter(@NonNull final Class<T> object) {
		try {
			return (Value<T>) this.parameter;
		} catch (ClassCastException e) {
			return null;
		}
	}

	@Override
	public int contentVersion() {
		return 0;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(DataQuery.of("key"), key)
				.set(DataQuery.of("parameter"), parameter)
				.set(DataQuery.of("optional"), optional)
				.set(DataQuery.of("optionalForConsole"), optionalForConsole)
				.set(DataQuery.of("path"), path)
				.set(Queries.CONTENT_VERSION, contentVersion());
	}

	public class Builder implements sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings.Builder {

		@Override
		public Builder reset() {
			return this;
		}

		@Override
		public Builder value(Value<?> value) {
			parameter = value;
			key = value.key().key();
			optional = value.isOptional();
			return this;
		}

		@Override
		public Builder optionalforConsole(boolean optional) {
			optionalForConsole = optional;
			return this;
		}

		@Override
		public ParameterSettings.Builder localeTextPath(Object[] path) {
			ParameterSettings.this.path = path;
			return this;
		}

		@Override
		public @NotNull ParameterSettings build() {
			return ParameterSettings.this;
		}
		
	}

}
