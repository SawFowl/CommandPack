package sawfowl.commandpack.commands.settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.registrar.tree.CommandTreeNode;
import org.spongepowered.api.command.registrar.tree.CommandTreeNodeTypes;
import org.spongepowered.api.command.registrar.tree.CommandTreeNode.Argument;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;

import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawCompleterPredicate;
import sawfowl.commandpack.api.commands.raw.arguments.RawCompleterSupplier;
import sawfowl.commandpack.api.commands.raw.arguments.RawResultSupplier;
import sawfowl.commandpack.utils.CommandsUtil;

public class RawArgumentImpl<T> implements RawArgument<T> {

	public Builder<T> builder() {
		return new Builder<T>();
	}

	private RawCompleterSupplier<Stream<String>> variants;
	private Supplier<Stream<String>> collection;
	private RawResultSupplier<T> result;
	private boolean isOptional = false;
	private boolean isOptionalForConsole = false;
	private int cursor;
	private Object[] localesPath;
	private Class<?> clazz;
	private String permission;
	private Argument<?> node = CommandTreeNodeTypes.STRING.get().createNode();
	private RawCompleterPredicate<CommandCause, Stream<String>> canUse = (cause, variants, input) -> variants.filter(var -> var.equals(input)).findFirst().isPresent();

	@Override
	public Argument<?> getArgumentType() {
		return node;
	}

	@Override
	public Stream<String> getVariants(CommandCause cause, String[] args) {
		return variants == null ? new ArrayList<String>().stream() : variants.get(cause, args);
	}

	@Override
	public Collection<String> getVariants() {
		if(collection == null) return CommandsUtil.EMPTY_VARIANTS;
		Stream<String> variants = collection.get();
		return variants == null ? CommandsUtil.EMPTY_VARIANTS : variants.toList();
	}

	@Override
	public Optional<T> getResult(Class<T> clazz, String[] args) {
		return getResult(clazz, CommandCause.create(), args);
	}

	@Override
	public Optional<T> getResult(Class<T> clazz, CommandCause cause, String[] args) {
		return result == null ? Optional.empty() : result.get(cause, args);
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
		return cause == null || getPermision().map(p -> cause.hasPermission(p)).orElse(true);
	}

	@Override
	public boolean canUse(CommandCause cause, String input) {
		return canUse == null && collection != null ? hasPermission(cause) : canUse.test(cause, collection.get(), input);
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
			if(canUse == null) canUse = (cause, variants, input) -> variants.filter(var -> var.equals(input)).findFirst().isPresent();
			if(node == null) node = CommandTreeNodeTypes.STRING.get().createNode();
			return (RawArgument<T>) RawArgumentImpl.this;
		}

		@Override
		public Builder<T> variants(RawCompleterSupplier<Stream<String>> variants) {
			RawArgumentImpl.this.variants = variants;
			return this;
		}

		@Override
		public Builder<T> variants(Supplier<Stream<String>> variants) {
			if(variants != null && variants.get() != null) RawArgumentImpl.this.collection = variants;
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
		public Builder<T> localeTextPath(Object... value) {
			localesPath = value;
			return this;
		}

		@Override
		public Builder<T> permission(String value) {
			permission = value;
			return this;
		}

		@Override
		public <C extends CommandTreeNode<C>> Builder<T> setArgumentType(Argument<C> node) {
			if(node != null) RawArgumentImpl.this.node = node;
			return this;
		}

		@Override
		public Builder<T> canUse(RawCompleterPredicate<CommandCause, Stream<String>> predicate) {
			if(predicate != null) canUse = predicate;
			return this;
		}
		
	}

}
