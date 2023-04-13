package sawfowl.commandpack.commands.settings;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.NonNull;

import org.jetbrains.annotations.NotNull;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.Key;
import org.spongepowered.api.data.SerializableDataHolder;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.CollectionValue;
import org.spongepowered.api.data.value.MapValue;
import org.spongepowered.api.data.value.MergeFunction;
import org.spongepowered.api.data.value.ValueContainer;

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
	public void setRawData(DataView container) throws InvalidDataException {
	}

	@Override
	public SerializableDataHolder.Mutable copy() {
		return new ParameterSettings(parameter, isOptionalForConsole(), path);
	}

	@Override
	public boolean validateRawData(DataView container) {
		return toContainer().contains(container.currentPath());
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
				.set(DataQuery.of("path"), path);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> Optional<E> get(Key<? extends org.spongepowered.api.data.value.Value<E>> key) {
		return toContainer().contains(DataQuery.of(key.key().asString())) ? (Optional<E>) Optional.ofNullable(toContainer().get(DataQuery.of(key.key().asString()))) : Optional.empty();
	}

	@Override
	public <E, V extends org.spongepowered.api.data.value.Value<E>> Optional<V> getValue(Key<V> key) {
		return Optional.empty();
	}

	@Override
	public boolean supports(Key<?> key) {
		return false;
	}

	@Override
	public Set<Key<?>> getKeys() {
		return new HashSet<>(Arrays.asList(
					Key.from(ResourceKey.of("parametersetting", "key"), String.class),
					Key.from(ResourceKey.of("parametersetting", "parameter"), Parameter.Value.class),
					Key.from(ResourceKey.of("parametersetting", "optional"), Boolean.class),
					Key.from(ResourceKey.of("parametersetting", "optionalForConsole"), Boolean.class),
					Key.from(ResourceKey.of("parametersetting", "path"), String[].class)
				));
	}

	@Override
	public Set<org.spongepowered.api.data.value.Value.Immutable<?>> getValues() {
		return new HashSet<>();
	}

	@Override
	public <E> DataTransactionResult offer(Key<? extends org.spongepowered.api.data.value.Value<E>> key, E value) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public DataTransactionResult offer(org.spongepowered.api.data.value.Value<?> value) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public <E> DataTransactionResult offerSingle(Key<? extends CollectionValue<E, ?>> key, E element) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public <K, V> DataTransactionResult offerSingle(Key<? extends MapValue<K, V>> key, K valueKey, V value) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public <K, V> DataTransactionResult offerAll(Key<? extends MapValue<K, V>> key, Map<? extends K, ? extends V> map) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public DataTransactionResult offerAll(MapValue<?, ?> value) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public DataTransactionResult offerAll(CollectionValue<?, ?> value) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public <E> DataTransactionResult offerAll(Key<? extends CollectionValue<E, ?>> key, Collection<? extends E> elements) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public <E> DataTransactionResult removeSingle(Key<? extends CollectionValue<E, ?>> key, E element) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public <K> DataTransactionResult removeKey(Key<? extends MapValue<K, ?>> key, K mapKey) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public DataTransactionResult removeAll(CollectionValue<?, ?> value) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public <E> DataTransactionResult removeAll(Key<? extends CollectionValue<E, ?>> key, Collection<? extends E> elements) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public DataTransactionResult removeAll(MapValue<?, ?> value) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public <K, V> DataTransactionResult removeAll(Key<? extends MapValue<K, V>> key, Map<? extends K, ? extends V> map) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public <E> DataTransactionResult tryOffer(Key<? extends org.spongepowered.api.data.value.Value<E>> key, E value) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public DataTransactionResult remove(Key<?> key) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public DataTransactionResult undo(DataTransactionResult result) {
		return DataTransactionResult.failNoData();
	}

	@Override
	public DataTransactionResult copyFrom(ValueContainer that, MergeFunction function) {
		return DataTransactionResult.failNoData();
	}

	public class Builder implements sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings.Builder {

		ParameterSettings settings;
		public Builder() {
			reset();
		}

		@Override
		public <V> Builder add(Key<? extends org.spongepowered.api.data.value.Value<V>> key, V value) {
			return this;
		}

		@Override
		public Builder from(sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings holder) {
			settings.key = holder.getKey();
			settings.optional = holder.isOptional();
			settings.optionalForConsole = holder.isOptionalForConsole();
			settings.parameter = holder.getParameterUnknownType();
			settings.path = holder.getPath();
			return this;
		}

		@Override
		public Builder reset() {
			settings = new ParameterSettings();
			return this;
		}

		@Override
		public Builder value(Value<?> value) {
			settings.parameter = value;
			key = value.key().key();
			return this;
		}

		@Override
		public Builder optionalforConsole(boolean optional) {
			settings.optionalForConsole = optional;
			return this;
		}

		@Override
		public ParameterSettings.Builder localeTextPath(Object[] path) {
			settings.path = path;
			return this;
		}

		@Override
		public @NotNull ParameterSettings build() {
			return settings;
		}
		
	}

}
