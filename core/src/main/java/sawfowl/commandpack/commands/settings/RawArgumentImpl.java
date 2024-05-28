package sawfowl.commandpack.commands.settings;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
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
import sawfowl.commandpack.api.commands.raw.arguments.RawCompleterSupplier;
import sawfowl.commandpack.api.commands.raw.arguments.RawResultSupplier;

public class RawArgumentImpl<T> implements RawArgument<T> {

	public Builder<T> builder() {
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
	private Argument<?> node = CommandTreeNodeTypes.STRING.get().createNode().customCompletions();
	private String treeKey;
	private Integer[] requiredIds = {};
	private String[] requiredKeys = {};

	@Override
	public Argument<?> getArgumentType() {
		return node;
	}

	@Override
	public Stream<String> getVariants(CommandCause cause, String[] args) {
		requireNonNull(cause, args);
		return variants == null ? new ArrayList<String>().stream() : variants.get(cause, args);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends T> Optional<V> getResult(CommandCause cause, String[] args) {
		requireNonNull(cause, args);
		if(result == null || clazz == null) return Optional.empty();
		try {
			return result.get(cause, args).map(value -> (V) value);
		} catch (Exception e) {
			return Optional.empty();
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
	public Class<?> getAssociatedClass() {
		return clazz;
	}

	@Override
	public Optional<String> getPermision() {
		return Optional.ofNullable(permission);
	}

	@Override
	public String getTreeKey() {
		return treeKey;
	}

	@Override
	public boolean hasPermission(CommandCause cause) {
		return cause == null || getPermision().map(p -> cause.hasPermission(p)).orElse(true);
	}

	@Override
	public int contentVersion() {
		return 1;
	}

	@Override
	public Integer[] getRequiredArgumentsById() {
		return requiredIds;
	}

	@Override
	public String[] getRequiredArgumentsByKey() {
		return requiredKeys;
	}

	@Override
	public boolean checkRequiredOtherArguments(CommandCause cause, Map<Integer, RawArgument<?>> args, String[] inputArgs) {
		if(args == null) return true;
		if(requiredIds.length > 0 && inputArgs.length >= cursor) {
			for(int id : requiredIds) {
				if(args.containsKey(id) && !args.get(id).getVariants(cause, inputArgs).filter(arg -> arg.equals(inputArgs[args.get(id).getCursor()])).findFirst().isPresent()) return false;
			}
		}
		if(requiredKeys.length > 0 && inputArgs.length >= cursor) {
			Map<String, RawArgument<?>> byKeys = args.values().stream().collect(Collectors.toMap(arg -> arg.getTreeKey(), arg -> arg));
			for(String key : requiredKeys) {
				if(byKeys.containsKey(key) && !byKeys.get(key).getVariants(cause, inputArgs).filter(arg -> arg.equals(inputArgs[byKeys.get(key).getCursor()])).findFirst().isPresent()) return false;
			}
			byKeys.clear();
		}
		return true;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(DataQuery.of("Variants"), variants)
				.set(DataQuery.of("Optional"), isOptional)
				.set(DataQuery.of("OptionalForConsole"), isOptionalForConsole)
				.set(DataQuery.of("Cursor"), cursor)
				.set(DataQuery.of("RequiredArguments", "ById"), requiredIds)
				.set(DataQuery.of("RequiredArguments", "ByKey"), requiredKeys)
				.set(DataQuery.of("LocalesPath"), localesPath)
				.set(Queries.CONTENT_VERSION, contentVersion());
	}

	@Override
	public int hashCode() {
		return Objects.hash(treeKey);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(this == obj) return true;
		if(getClass() != obj.getClass()) return false;
		return Objects.equals(treeKey, ((RawArgumentImpl<?>) obj).treeKey);
	}

	private void requireNonNull(Object... objects) {
		for(Object object : objects) Objects.requireNonNull(object);
	}

	@SuppressWarnings("hiding")
	private class Builder<T> extends RawArgumentImpl<T> implements RawArgument.Builder<T> {

		@SuppressWarnings("unchecked")
		@Override
		public @NotNull RawArgument<T> build() {
			if(node == null) node = CommandTreeNodeTypes.STRING.get().createNode();
			if(treeKey == null) treeKey = clazz.getSimpleName();
			return (RawArgument<T>) RawArgumentImpl.this;
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
		public Builder<T> localeTextPath(Object... value) {
			localesPath = value;
			return this;
		}

		@Override
		public Builder<T> permission(String value) {
			permission = value;
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <C extends CommandTreeNode<C>> Builder<T> setArgumentType(Argument<C> node) {
			if(node != null) RawArgumentImpl.this.node = (Argument<C>) node.customCompletions();
			return this;
		}

		@Override
		public Builder<T> treeKey(String value) {
			treeKey = value;
			return this;
		}

		@Override
		public Builder<T> setRequiredArguments(Integer... ids) {
			if(ids != null) requiredIds = ids;
			return this;
		}

		@Override
		public Builder<T> setRequiredArguments(String... keys) {
			if(keys != null) requiredKeys = keys;
			return this;
		}
		
	}

}
