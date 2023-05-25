package sawfowl.commandpack.commands.settings;

import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;

import sawfowl.commandpack.api.commands.raw.RawArgument;
import sawfowl.commandpack.api.commands.raw.RawSupplier;

public class RawArgumentImpl<T> implements RawArgument<T> {

	public Builder builder() {
		return new Builder();
	}

	private Supplier<Stream<String>> variants;
	private RawSupplier<T> result;
	private boolean isOptional = false;
	private boolean isOptionalForConsole = false;
	private Object defaultValue;
	private int cursor;
	private Object[] localesPath = new Object[]{};

	@Override
	public Stream<String> getVariants() {
		return variants == null ? new ArrayList<String>().stream() : variants.get();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getResult(Class<T> object, String[] args) {
		if(result == null) return (T) defaultValue;
		return defaultValue == null ? result.get(args) : (T) defaultValue;
	}

	@Override
	public boolean isOptional() {
		return isOptional;
	}

	@Override
	public boolean isOptionalForConsole() {
		return isOptionalForConsole;
	}

	@Override
	public int getCursor() {
		return cursor;
	}

	@Override
	public Object[] getLocalesPath() {
		return localesPath;
	}

	@Override
	public int contentVersion() {
		return 1;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(DataQuery.of("Variants"), variants)
				.set(DataQuery.of("Optional"), isOptional)
				.set(DataQuery.of("OptionalForConsole"), isOptionalForConsole)
				.set(DataQuery.of("Default"), defaultValue)
				.set(DataQuery.of("Cursor"), cursor)
				.set(DataQuery.of("LocalesPath"), localesPath)
				.set(Queries.CONTENT_VERSION, contentVersion());
	}

	private class Builder implements RawArgument.Builder {

		@Override
		public @NotNull RawArgument<?> build() {
			return RawArgumentImpl.this;
		}

		@Override
		public Builder variants(Supplier<Stream<String>> variants) {
			RawArgumentImpl.this.variants = variants;
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Builder result(RawSupplier<?> result) {
			RawArgumentImpl.this.result = (RawSupplier<T>) result;
			return this;
		}

		@Override
		public Builder optional(boolean value) {
			return this;
		}

		@Override
		public Builder optionalForConsole(boolean value) {
			return this;
		}

		@Override
		public Builder defaultValue(Object value) {
			return this;
		}

		@Override
		public Builder cursor(int value) {
			return this;
		}

		@Override
		public Builder localesPath(Object[] value) {
			return this;
		}
		
	}

}
