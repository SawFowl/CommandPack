package sawfowl.commandpack.commands.settings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import org.jetbrains.annotations.NotNull;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;

import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;

public class RawArgumentsMapImpl implements RawArgumentsMap {

	private Map<String, Object> mapByKey = new HashMap<>();
	private Map<Integer, Object> mapById = new HashMap<>();

	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<T> get(String key) {
		if(mapByKey.containsKey(key)) {
			try {
				return Optional.ofNullable((T) mapByKey.get(key));
			} catch (Exception e) {
				return Optional.empty();
			}
		} else return Optional.empty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<T> get(int id) {
		if(mapById.containsKey(id)) {
			try {
				return Optional.ofNullable((T) mapById.get(id));
			} catch (Exception e) {
				return Optional.empty();
			}
		} else return Optional.empty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<T> get(Class<T> valueType, String key) {
		return mapByKey.containsKey(key) ? Optional.ofNullable(mapByKey.get(key).getClass().isAssignableFrom(valueType) ? (T) mapByKey.get(key) : null) : Optional.empty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<T> get(Class<T> valueType, int id) {
		return mapById.containsKey(id) ? Optional.ofNullable(mapById.get(id).getClass().isAssignableFrom(valueType) ? (T) mapById.get(id) : null) : Optional.empty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, V extends Object> void forEach(Class<K> key, BiConsumer<? super K, ? super V> action) {
		if(key.isAssignableFrom(Integer.class)) {
			mapById.forEach((BiConsumer<? super Integer, ? super Object>) action);
		} else if(key.isAssignableFrom(String.class)) {
			mapByKey.forEach((BiConsumer<? super String, ? super Object>) action);
		} else throw new IllegalStateException("An invalid key type is specified. Only `String` and `Integer` are allowed.\n");
	}

	@Override
	public Set<Integer> idSet() {
		return mapById.keySet();
	}

	@Override
	public Set<String> keySet() {
		return mapByKey.keySet();
	}

	@Override
	public Collection<Object> values() {
		return mapByKey.values();
	}

	@Override
	public void clear() {
		mapById.clear();
		mapByKey.clear();
	}

	@Override
	public int contentVersion() {
		return 1;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(DataQuery.of("KeysMap"), mapByKey)
				.set(DataQuery.of("IdsMap"), mapById);
	}

	public Builder builder() {
		return new Builder() {
			
			@Override
			public @NotNull RawArgumentsMap build() {
				return RawArgumentsMapImpl.this;
			}
			
			@Override
			public RawArgumentsMap create(RawCommand command, CommandCause cause, String[] args) {
				if(command != null && command.getArguments() != null && cause != null && args != null && args.length > 0) command.getArguments().forEach((k, v) -> {
					if(!mapByKey.containsKey(v.getTreeKey()) && !mapById.containsKey(k)) v.getResult(cause, args).ifPresent(value -> {
						if(!mapByKey.containsValue(value) && !mapById.containsValue(value)) {
							mapByKey.put(v.getTreeKey(), value);
							mapById.put(k, value);
						}
					});
				});
				return build();
			}
		};
	}


}
