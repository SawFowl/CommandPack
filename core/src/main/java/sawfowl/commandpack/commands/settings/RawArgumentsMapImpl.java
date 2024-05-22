package sawfowl.commandpack.commands.settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;

import sawfowl.commandpack.api.commands.raw.RawCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;

public class RawArgumentsMapImpl implements RawArgumentsMap {

	private transient Set<Integer> ids = Collections.emptySet();
	private transient Set<String> keys = Collections.emptySet();
	private transient List<Result<?>> results = Collections.emptyList();
	private transient String[] args = {};

	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<T> get(String key) {
		return results.stream().filter(r -> r.key.equals(key)).findFirst().map(r -> {
			try {
				return (T) r.value;
			} catch (Exception e) {
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<T> get(int id) {
		return results.stream().filter(r -> r.id == id).findFirst().map(r -> {
			try {
				return (T) r.value;
			} catch (Exception e) {
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<T> get(Class<T> valueType, String key) {
		return results.stream().filter(r -> r.key.equals(key)).findFirst().map(r -> {
			try {
				return (T) r.value;
			} catch (Exception e) {
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<T> get(Class<T> valueType, int id) {
		return results.stream().filter(r -> r.id == id).findFirst().map(r -> {
			try {
				return (T) r.value;
			} catch (Exception e) {
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, V extends Object> void forEach(Class<K> key, BiConsumer<? super K, ? super V> action) {
		if(key.isAssignableFrom(Integer.class)) {
			results.stream().collect(Collectors.toMap(r -> r.id, r -> r.value)).forEach((BiConsumer<? super Integer, ? super Object>) action);
		} else if(key.isAssignableFrom(String.class)) {
			results.stream().collect(Collectors.toMap(r -> r.key, r -> r.value)).forEach((BiConsumer<? super String, ? super Object>) action);
		} else throw new IllegalStateException("An invalid key type is specified. Only `String` and `Integer` are allowed.\n");
	}

	@Override
	public Set<Integer> idSet() {
		return ids;
	}

	@Override
	public Set<String> keySet() {
		return keys;
	}

	@Override
	public Collection<Object> values() {
		return results.stream().map(r -> r.value).collect(Collectors.toUnmodifiableList());
	}

	@Override
	public String[] getInput() {
		return args;
	}

	@Override
	public void clear() {
		ids = Collections.unmodifiableSet(new HashSet<>());
		keys = Collections.unmodifiableSet(new HashSet<>());
		results = Collections.unmodifiableList(new ArrayList<>());
	}

	@Override
	public int size() {
		return results.size();
	}

	@Override
	public boolean isEmpty() {
		return results.isEmpty();
	}

	@Override
	public int contentVersion() {
		return 1;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(DataQuery.of("KeysMap"), results.stream().collect(Collectors.toMap(r -> r.key, r -> r.value)))
				.set(DataQuery.of("IdsMap"), results.stream().collect(Collectors.toMap(r -> r.id, r -> r.value)));
	}

	public Builder builder() {
		return new Builder() {
			
			@Override
			public @NotNull RawArgumentsMap build() {
				return RawArgumentsMapImpl.this;
			}
			
			@Override
			public RawArgumentsMap create(RawCommand command, CommandCause cause, String[] input) {
				if(input != null) args = input;
				if(command == null || command.getArguments() == null || cause == null || input == null || args.length == 0) return build();
				results = command.getArguments().values().stream().map(arg -> arg.hasPermission(cause) ? arg.getResult(cause, args)
						.map(value -> new Result<>(arg.getCursor(), arg.getTreeKey(), value)).orElse(null) : null)
						.filter(result -> result != null).collect(Collectors.toUnmodifiableList());
				ids = results.stream().map(r -> r.id).collect(Collectors.toUnmodifiableSet());
				keys = results.stream().map(r -> r.key).collect(Collectors.toUnmodifiableSet());
				return build();
			}
		};
	}

	private class Result<T> {
		// transient ?
		private transient final int id;
		private transient final String key;
		private transient final T value;
		public Result(int id, String key, T value) {
			this.id = id;
			this.key = key;
			this.value = value;
		}
	}

}
