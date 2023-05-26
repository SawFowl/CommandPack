package sawfowl.commandpack.commands.settings;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;

import sawfowl.commandpack.api.commands.raw.RawArgument;
import sawfowl.commandpack.api.commands.raw.RawSupplier;

public class RawArgumentImpl<T> implements RawArgument<T> {

	@SuppressWarnings("hiding")
	public <T> Builder<T> builder() {
		return new Builder<T>();
	}

	private Supplier<Stream<String>> variants;
	private RawSupplier<T> result;
	private boolean isOptional = false;
	private boolean isOptionalForConsole = false;
	private int cursor;
	private Object[] localesPath;
	private Class<?> clazz;

	@Override
	public Stream<String> getVariants() {
		return variants == null ? new ArrayList<String>().stream() : variants.get();
	}

	@Override
	public Optional<T> getResult(Class<T> clazz, String[] args) {
		return result == null ? Optional.empty() : result.get(args);
	}

	@Override
	public Optional<?> getResultUnknownType(String[] args) throws CommandException {
		try {
			return result == null ? Optional.empty() : result.get(args);
		} catch (Exception e) {
			return null;
		}
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
	public Class<?> getClazz() {
		return clazz;
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
				.set(DataQuery.of("Cursor"), cursor)
				.set(DataQuery.of("LocalesPath"), localesPath)
				.set(Queries.CONTENT_VERSION, contentVersion());
	}

	@SuppressWarnings("hiding")
	private class Builder<T> implements RawArgument.Builder<T> {

		@SuppressWarnings("unchecked")
		@Override
		public @NotNull RawArgument<T> build() {
			return (@NotNull RawArgument<T>) RawArgumentImpl.this;
		}

		@Override
		public Builder<T> variants(Supplier<Stream<String>> variants) {
			RawArgumentImpl.this.variants = variants;
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Builder<T> result(Class<?> clazz, RawSupplier<?> result) {
			((RawArgumentImpl<T>) RawArgumentImpl.this).result = (RawSupplier<T>) result;
			RawArgumentImpl.this.clazz = clazz;
			return this;
		}

		@Override
		public Builder<T> optional(boolean value) {
			isOptional = value;
			return this;
		}

		@Override
		public Builder<T> optionalForConsole(boolean value) {
			isOptionalForConsole = value;
			return this;
		}

		@Override
		public Builder<T> cursor(int value) {
			cursor = value;
			return this;
		}

		@Override
		public Builder<T> localesPath(Object[] value) {
			localesPath = value;
			return this;
		}
		
	}

}
