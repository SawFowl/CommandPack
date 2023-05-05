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

public class ParameterSettingsImpl implements sawfowl.commandpack.api.commands.parameterized.ParameterSettings {

	private Parameter.Value<?> parameter;
	private Boolean optional;
	private Boolean optionalForConsole = false;
	private Object[] path = {"NaN"};
	private String key;
	private int position = 0;
	public ParameterSettingsImpl() {}
	public ParameterSettingsImpl(Parameter.Value<?> parameter, boolean optional, boolean optionalForConsole, Object... pathException) {
		this.key = parameter.key().key();
		this.parameter = parameter;
		this.optionalForConsole = optional && optionalForConsole;
		this.path = pathException;
	}

	@Override
	public boolean containsIn(CommandContext context) {
		return context.one(parameter).isPresent();
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
	public int getPosition() {
		return position;
	}

	@Override
	public int contentVersion() {
		return 1;
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

	public class Builder implements sawfowl.commandpack.api.commands.parameterized.ParameterSettings.Builder {

		@Override
		public Builder reset() {
			return this;
		}

		@Override
		public Builder value(Value<?> value) {
			parameter = value;
			key = value.key().key();
			optional = parameter.isOptional();
			return this;
		}

		@Override
		public Builder optionalforConsole(boolean optional) {
			optionalForConsole = optional;
			return this;
		}

		@Override
		public Builder setOptional(boolean optional) {
			ParameterSettingsImpl.this.optional = optional;
			return this;
		}

		@Override
		public ParameterSettingsImpl.Builder localeTextPath(Object[] path) {
			ParameterSettingsImpl.this.path = path;
			return this;
		}

		@Override
		public Builder position(int position) {
			ParameterSettingsImpl.this.position = position;
			return this;
		}

		@Override
		public @NotNull ParameterSettingsImpl build() {
			return ParameterSettingsImpl.this;
		}
		
	}

}
