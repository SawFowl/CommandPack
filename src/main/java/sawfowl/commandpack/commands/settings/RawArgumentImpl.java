package sawfowl.commandpack.commands.settings;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;

import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawCompleterSupplier;
import sawfowl.commandpack.api.commands.raw.arguments.RawResultSupplier;

public class RawArgumentImpl<T> implements RawArgument<T> {

	@SuppressWarnings("hiding")
	public <T> Builder<T> builder() {
		return new Builder<T>();
	}

	private RawCompleterSupplier<Stream<String>> variants;
	private RawResultSupplier<T> result;
	private boolean isOptional = false;
	private boolean isOptionalForConsole = false;
	private int cursor;
	private Object[] localesPath;
	private Class<?> clazz;
	private String permission;

	@Override
	public Stream<String> getVariants(CommandCause cause, String[] args) {
		return variants == null ? new ArrayList<String>().stream() : variants.get(cause, args);
	}

	@Override
	public Optional<T> getResult(Class<T> clazz, String[] args) {
		return result == null ? Optional.empty() : result.get(null, args);
	}

	@Override
	public Optional<?> getResultUnknownType(CommandCause cause, String[] args) {
		return result == null ? Optional.empty() : result.get(cause, args);
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
	public Optional<String> getPermision() {
		return Optional.ofNullable(permission);
	}

	@Override
	public boolean hasPermission(CommandCause cause) {
		return getPermision().map(p -> cause.hasPermission(p)).orElse(true);
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
			return (sawfowl.commandpack.api.commands.raw.arguments.RawArgument<T>) RawArgumentImpl.this;
		}

		@Override
		public Builder<T> variants(RawCompleterSupplier<Stream<String>> variants) {
			RawArgumentImpl.this.variants = variants;
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Builder<T> result(Class<?> clazz, RawResultSupplier<?> result) {
			((RawArgumentImpl<T>) RawArgumentImpl.this).result = (RawResultSupplier<T>) result;
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
		public Builder<T> localeTextPath(Object[] value) {
			localesPath = value;
			return this;
		}

		@Override
		public sawfowl.commandpack.api.commands.raw.arguments.RawArgument.Builder<T> permission(String value) {
			permission = value;
			return this;
		}
		
	}

}
