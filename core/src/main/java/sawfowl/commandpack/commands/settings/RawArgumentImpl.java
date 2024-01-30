package sawfowl.commandpack.commands.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.registrar.tree.CommandCompletionProvider;
import org.spongepowered.api.command.registrar.tree.CommandTreeNode;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawCompleterSupplier;
import sawfowl.commandpack.api.commands.raw.arguments.RawResultSupplier;
import sawfowl.commandpack.utils.CommandsUtil;

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
	private String treeTooltip = "argument";
	private Component tooltip = Component.text("argument");
	private CommandTreeNode<?> node = null;

	@Override
	public String treeTooltip() {
		return treeTooltip;
	}

	@Override
	public Component getTooltip() {
		return tooltip;
	}

	@Override
	public CommandTreeNode<?> getTreeNode() {
		return node;
	}

	@Override
	public Stream<String> getVariants(CommandCause cause, String[] args) {
		return variants == null ? new ArrayList<String>().stream() : variants.get(cause, args);
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
		public Builder<T> permission(String value) {
			permission = value;
			return this;
		}

		@Override
		public Builder<T> setTreeTooltip(String tooltip) {
			treeTooltip = tooltip;
			return this;
		}

		@Override
		public Builder<T> setTooltip(Component tooltip) {
			RawArgumentImpl.this.tooltip = tooltip;
			return this;
		}

		@Override
		public Builder<T> setCommandTreeNode(CommandTreeNode<?> node) {
			if(node != null) {
				node.completions(new CommandCompletionProvider() {
					@Override
					public List<CommandCompletion> complete(CommandContext context, String currentInput) {
						String[] args = Stream.of(currentInput.split(" ")).filter(string -> (!string.equals(""))).toArray(String[]::new);
						if(args.length -1 != cursor) return CommandsUtil.EMPTY_COMPLETIONS;
						return getVariants(context.cause(), args).filter(var -> args.length < cursor && currentInput.endsWith(" ") || var.contains(args[cursor])).map(var -> CommandCompletion.of(var, getTooltip())).toList();
					}
				});
			}
			RawArgumentImpl.this.node = node;
			return this;
		}
		
	}

}
