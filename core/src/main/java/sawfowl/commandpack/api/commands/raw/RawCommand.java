package sawfowl.commandpack.api.commands.raw;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.command.Command.Raw;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.command.registrar.tree.CommandCompletionProvider;
import org.spongepowered.api.command.registrar.tree.CommandTreeNode;
import org.spongepowered.api.command.registrar.tree.CommandTreeNodeTypes;
import org.spongepowered.api.command.registrar.tree.CommandTreeNode.Argument;
import org.spongepowered.api.command.registrar.tree.CommandTreeNode.Basic;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.common.command.registrar.tree.builder.AbstractCommandTreeNode;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.commands.PluginCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.configure.locale.LocalesPaths;
import sawfowl.commandpack.utils.CommandsUtil;

/**
 * This interface is designed to simplify the creation of {@link Raw} commands.
 *
 * @author SawFowl
 */
public interface RawCommand extends PluginCommand, Raw {

	/**
	 * Command code execution.
	 */
	void process(CommandCause cause, Audience audience, Locale locale, boolean isPlayer, Mutable arguments, RawArgumentsMap args) throws CommandException;

	Component shortDescription(Locale locale);

	Component extendedDescription(Locale locale);

	/**
	 * Subcommand map.<br>
	 * The subcommands automatically get to the first place when the arguments are autocomplete.
	 */
	Map<String, RawCommand> getChildExecutors();

	/**
	 * Map of command arguments.
	 */
	Map<Integer, RawArgument<?>> getArguments();

	/**
	 * If `false`, the player will not get a list of available command arguments. <br>
	 * Regardless of the return value, the player will receive variants of the child commands.<br>
	 * This parameter will be ignored when overriding the {@link #complete(CommandCause, Mutable)} method.
	 */
	default boolean enableAutoComplete() {
		return getCommandSettings() == null || getCommandSettings().isAutoComplete().orElse(true);
	}

	default CommandTreeNode.Root commandTree() {
		CommandTreeNode.Root root = containsChild() ?
			CommandTreeNode.root().executable().child("subcommand", CommandTreeNode.literal().customCompletions()) :
			CommandTreeNode.root().customCompletions();
		if(!containsChild() && !containsArgs()) return root.executable();
		if(containsChild()) {
			root.completions(new CommandCompletionProvider() {
				@Override
				public List<CommandCompletion> complete(CommandContext context, String currentInput) {
					return completeArgs(context.cause(), Stream.of(currentInput.split(" ")).filter(string -> (!string.equals(""))).collect(Collectors.toList()), currentInput);
				}
			});
			Map<String, Basic> finishedChilds = new HashMap<>();
			for(Entry<String, RawCommand> entry : getChildExecutors().entrySet()) {
				if(!finishedChilds.containsKey(entry.getValue().command())) {
					Basic childNode = commandTree(entry.getValue()).requires(cause -> entry.getValue().canExecute(cause));
					root.child(entry.getKey(), childNode);
					finishedChilds.put(entry.getValue().command(), childNode);
					childNode = null;
				} else root.child(entry.getKey(), finishedChilds.get(entry.getValue().command()));
			}
			finishedChilds.clear();
			finishedChilds = null;
		}
		if(containsArgs()) {
			argsTree(0, (AbstractCommandTreeNode<?, ?>) root, this);
		}
		return root;
	}

	private CommandTreeNode.Basic commandTree(RawCommand child) {
		Basic node = child.containsChild() ?
				CommandTreeNode.literal().executable().child("subcommand", CommandTreeNode.literal().customCompletions()) :
				CommandTreeNode.literal().customCompletions();
		if(!child.containsChild() && !child.containsArgs()) return node.executable();
		if(child.containsChild()) {
			Map<String, Basic> finishedChilds = new HashMap<>();
			for(Entry<String, RawCommand> entry : child.getChildExecutors().entrySet()) {
				if(!finishedChilds.containsKey(entry.getValue().command())) {
					Basic childNode = commandTree(entry.getValue()).requires(cause -> entry.getValue().canExecute(cause));
					node.child(entry.getKey(), childNode);
					finishedChilds.put(entry.getValue().command(), childNode);
					childNode = null;
				} else node.child(entry.getKey(), finishedChilds.get(entry.getValue().command()));
			}
			finishedChilds.clear();
			finishedChilds = null;
		}
		if(child.containsArgs()) {
			argsTree(0, (AbstractCommandTreeNode<?, ?>) node, child);
		}
		return node;
	}

	private void argsTree(int depth, AbstractCommandTreeNode<?, ?> parrent, RawCommand command) {
		if(command.getArguments() == null || !command.getArguments().containsKey(depth)) return;
		Argument<?> node = command.getArguments().get(depth).getArgumentType() != null ? command.getArguments().get(depth).getArgumentType() : CommandTreeNodeTypes.STRING.get().createNode().greedy().customCompletions();
		if(!command.getArguments().containsKey(depth + 1)) node.executable();
		if(!parrent.getChildren().containsKey(command.getArguments().get(depth).getTreeKey())) {
			parrent.child(command.getArguments().get(depth).getTreeKey(), node);
		}
		argsTree(depth + 1, (AbstractCommandTreeNode<?, ?>) node, command);
		node = null;
	}

	/**
	 * Checks for child commands and who activated the command.
	 */
	@Override
	default CommandResult process(CommandCause cause, Mutable arguments) throws CommandException {
		boolean isPlayer = cause.first(ServerPlayer.class).isPresent();
		Locale locale = getLocale(cause);
		RawArgumentsMap map = checkChildAndArguments(cause, createResultCollection(cause, Stream.of(arguments.input().split(" ")).map(String::toString).filter(string -> (string != null && !string.equals(""))).toArray(String[]::new)), isPlayer, locale);
		Object[] child = getChildExecutor(cause, map);
		if(child != null && child.length > 0) {
			((RawCommand) child[0]).process(cause, cause.audience(), locale, isPlayer, arguments, ((RawArgumentsMap) child[1]));
			child = null;
		} else process(cause, cause.first(ServerPlayer.class).map(player -> (Audience) player).orElse(cause.audience()), locale, isPlayer, arguments, map);
		/*
		 * 
		String[] args = checkChildAndArguments(cause, Stream.of(arguments.input().split(" ")).map(String::toString).filter(string -> (string != null && !string.equals(""))).toArray(String[]::new), isPlayer, locale);
		Object[] child = getChildExecutor(cause, args);
		if(child != null && child.length > 0) {
			((RawCommand) child[0]).process(cause, cause.audience(), locale, isPlayer, (String[]) child[1], arguments);
			child = null;
		} else process(cause, cause.first(ServerPlayer.class).map(player -> (Audience) player).orElse(cause.audience()), locale, isPlayer, args, arguments);*/
		return success();
	}

	@Override
	default List<CommandCompletion> complete(CommandCause cause, Mutable arguments) throws CommandException {
		return enableAutoComplete() ? completeChild(cause, Stream.of(arguments.input().split(" ")).filter(string -> (!string.equals(""))).collect(Collectors.toList()), arguments.input()) : CommandsUtil.EMPTY_COMPLETIONS;
	}

	default RawArgumentsMap checkChildAndArguments(CommandCause cause, RawArgumentsMap args, boolean isPlayer, Locale locale) throws CommandException {
		if(args.getInput().length != 0 && getChildExecutors() != null && !getChildExecutors().isEmpty() && getChildExecutors().containsKey(args.getInput()[0]) && getChildExecutors().get(args.getInput()[0]).canExecute(cause)) {
			return getChildExecutors().get(args.getInput()[0]).checkChildAndArguments(cause, (args.getInput().length > 1 ? createResultCollection(cause, Stream.of(Arrays.copyOfRange(args.getInput(), 1, args.getInput().length + 1)).filter(arg -> arg != null).toArray(String[]::new)) : RawArgumentsMap.empty()), isPlayer, locale);
		} else return checkArguments(cause, args, isPlayer, locale);
	}

	default RawArgumentsMap checkArguments(CommandCause cause, RawArgumentsMap args, boolean isPlayer, Locale locale) throws CommandException {
		if(getArguments() != null && args != null && !args.isEmpty()) {
			Optional<RawArgument<?>> emptyArg = getArguments().values().stream().filter(arg -> (!arg.isOptional() || (!isPlayer && !arg.isOptionalForConsole())) && !args.idSet().contains(arg.getCursor())).findFirst();
			if(emptyArg.isPresent()) exceptionAppendUsage(cause, getComponent(locale, emptyArg.get().getLocalesPath()));
		}
		return args;
	}

	@SuppressWarnings("unused")
	@Deprecated
	private String[] checkChildAndArguments(CommandCause cause, String[] args, boolean isPlayer, Locale locale) throws CommandException {
		if(args.length != 0 && getChildExecutors() != null && !getChildExecutors().isEmpty() && getChildExecutors().containsKey(args[0]) && getChildExecutors().get(args[0]).canExecute(cause)) {
			getChildExecutors().get(args[0]).checkChildAndArguments(cause, (args.length > 1 ? Stream.of(Arrays.copyOfRange(args, 1, args.length + 1)).filter(arg -> arg != null).toArray(String[]::new) : new String[] {}), isPlayer, locale);
			return args;
		} else return checkArguments(cause, args, isPlayer, locale);
	}

	@Deprecated
	private String[] checkArguments(CommandCause cause, String[] args, boolean isPlayer, Locale locale) throws CommandException {
		if(getArguments() != null) {
			Optional<RawArgument<?>> emptyArg = getArguments().values().stream().filter(arg -> !arg.getResult(cause, args).isPresent() && (!arg.isOptional() || !isPlayer && !arg.isOptionalForConsole())).findFirst();
			if(emptyArg.isPresent()) exceptionAppendUsage(cause, getComponent(locale, emptyArg.get().getLocalesPath()));
			if(args.length != 0) {
				int i = 0;
				for(String arg : args) {
					if(arg == null || (getArguments().containsKey(i) && getArguments().get(i).getResult(cause, args).isPresent() && !getArguments().get(i).hasPermission(cause))) return i - 1 < 0 ? args : Arrays.copyOfRange(args, 0, i - 1);
					i++;
				}
			}
		}
		return args;
	}

	default List<CommandCompletion> completeChild(CommandCause cause, List<String> args, String currentInput) {
		if(getChildExecutors() != null && !getChildExecutors().isEmpty()) {
			if(args.size() == 0 || (args.size() == 1 && !currentInput.endsWith(" "))) {
				return new ArrayList<CommandCompletion>() {
					private static final long serialVersionUID = 1L;
					{
						addAll(getChildExecutors().keySet().stream().filter(command -> (args.isEmpty() || ((command.equals(args.get(0)) || command.startsWith(args.get(0))) && getChildExecutors().get(command).canExecute(cause)))).map(CommandCompletion::of).collect(Collectors.toList()));
						addAll(completeArgs(cause, args, currentInput));
					}
				};
			} else if(getChildExecutors().containsKey(args.get(0))) {
				return new ArrayList<CommandCompletion>() {
					private static final long serialVersionUID = 1L;
					{
						addAll(getChildExecutors().get(args.get(0)).completeChild(cause, args.subList(1, args.size()), currentInput.contains(args.get(0) + " ") ? currentInput.replace(args.get(0) + " ", "") : currentInput.replace(args.get(0), "")));
						addAll(completeArgs(cause, args, currentInput));
					}
				};
			}
		}
		return completeArgs(cause, args, currentInput);
	}

	/**
	 * Auto-complete command arguments.
	 */
	default List<CommandCompletion> completeArgs(CommandCause cause, List<String> args, String currentInput) {
		if(!enableAutoComplete() || getArguments() == null || getArguments().size() < args.size() || (!getArguments().isEmpty() && !getArguments().get(args.size() > 0 ? args.size() - 1 : 0).hasPermission(cause))) return CommandsUtil.EMPTY_COMPLETIONS;
		if(currentInput.endsWith(" ") || args.size() == 0) {
			if(getArguments().containsKey(args.size())) return getArguments().get(args.size()).getVariants(cause, args.toArray(new String[] {})).map(CommandCompletion::of).collect(Collectors.toList());
			return CommandsUtil.EMPTY_COMPLETIONS;
		} else return getArguments().get(args.size() - 1).getVariants(cause, args.toArray(new String[] {})).filter(v -> ((v.contains(args.get(args.size() - 1)) && !currentInput.endsWith(" ")) || (args.get(args.size() - 1).contains(v) && !args.get(args.size() - 1).contains(v + " ")) || (v.contains(":") && (v.split(":")[0].contains(args.get(args.size() - 1)) || v.split(":")[1].contains(args.get(args.size() - 1)))))).map(CommandCompletion::of).collect(Collectors.toList());
	}

	@Override
	default Optional<Component> shortDescription(CommandCause cause) {
		return Optional.ofNullable(shortDescription(getLocale(cause)));
	}

	@Override
	default Optional<Component> extendedDescription(CommandCause cause) {
		return Optional.ofNullable(extendedDescription(getLocale(cause)));
	}

	/**
	 * Determining who can use the command.
	 */
	@Override
	default boolean canExecute(CommandCause cause) {
		return permission() == null || cause.hasPermission(permission());
	}

	/**
	 * Command registration.
	 */
	default void register(RegisterCommandEvent<Raw> event) {
		if(getCommandSettings() == null) {
			if(getCommandSettings() == null || getCommandSettings().isEnable()) CommandPack.getInstance().getAPI().registerCommand(this);
		} else {
			if(!getCommandSettings().isEnable()) return;
			if(getCommandSettings() == null || getCommandSettings().isEnable()) CommandPack.getInstance().getAPI().registerCommand(this);
		}
		CommandPack.getInstance().getPlayersData().getTempData().addTrackingCooldownCommand(this);
	}

	default CommandException exceptionAppendUsage(CommandCause cause, Component text) throws CommandException {
		throw new CommandException(usage(cause) == null ? text : text.append(Component.newline()).append(usage(cause)));
	}

	default CommandException exceptionAppendUsage(CommandCause cause, Locale locale, Object[] localePath) throws CommandException {
		throw new CommandException(getComponent(locale, localePath).append(Component.newline()).append(usage(cause)));
	}
/*
	default Optional<ServerWorld> getWorld(String[] args, CommandCause cause, int cursor) {
		return getArgument(cause, args, cursor);
	}

	default Optional<WorldType> getWorldType(String[] args, CommandCause cause, int cursor) {
		return getArgument(cause, args, cursor);
	}
	default Optional<ServerPlayer> getPlayer(String[] args, CommandCause cause, int cursor) {
		return getArgument(cause, args, cursor);
	}

	default Optional<EnchantmentType> getEnchantmentType(String[] args, CommandCause cause, int cursor) {
		return getArgument(cause, args, cursor);
	}

	default Optional<String> getString(String[] args, CommandCause cause, int cursor) {
		return getArgument(cause, args, cursor);
	}

	default Optional<Boolean> getBoolean(String[] args, CommandCause cause, int cursor) {
		return getArgument(cause, args, cursor);
	}

	default Optional<Integer> getInteger(String[] args, CommandCause cause, int cursor) {
		return getArgument(cause, args, cursor);
	}

	default Optional<Long> getLong(String[] args, CommandCause cause, int cursor) {
		return getArgument(cause, args, cursor);
	}

	default Optional<Double> getDouble(String[] args, CommandCause cause, int cursor) {
		return getArgument(cause, args, cursor);
	}

	default Optional<BigDecimal> getBigDecimal(String[] args, CommandCause cause, int cursor) {
		return getArgument(BigDecimal.class, cause, args, cursor);
	}

	default Optional<Locale> getLocale(String[] args, CommandCause cause, int cursor) {
		return getArgument(cause, args, cursor);
	}

	default Optional<Currency> getCurrency(String[] args, CommandCause cause, int cursor) {
		return getArgument(cause, args, cursor);
	}

	default Optional<Duration> getDurationArg(String[] args, CommandCause cause, int cursor, Locale locale) throws CommandException {
		return getString(args, cause, cursor).map(value -> {
			try {
				return parseDuration(value, locale);
			} catch (CommandException e) {
				return null;
			}
		}).orElse(Optional.empty());
	}

	/**
	 * Getting an object from a command argument.
	 * 
	 * @param <T> - The type of the returned object.
	 * @param clazz - The class of the returned object.
	 * @param cause - {@link CommandCause}
	 * @param args - All command arguments.
	 * @param cursor - The argument number of the command.
	 * @return {@link Optional}
	 */
	/*@SuppressWarnings("unchecked")
	default <T> Optional<T> getArgument(Class<T> clazz, CommandCause cause, String[] args, int cursor) {
		return getArguments() == null || !getArguments().containsKey(cursor) || !getArguments().get(cursor).getAssociatedClass().isAssignableFrom(clazz) ? Optional.empty() : getArguments().get(cursor).getResult(cause, args).map(value -> {
				try {
					return (T) value;
				} catch (Exception e) {
					return null;
				}
			}
		);
	}

	/**
	 * Getting an object from a command argument.
	 * 
	 * @param <T> - The type of the returned object.
	 * @param cause - {@link CommandCause}
	 * @param args - All command arguments.
	 * @param cursor - The argument number of the command.
	 * @return {@link Optional}
	 */
	/*@SuppressWarnings("unchecked")
	default <T> Optional<T> getArgument(CommandCause cause, String[] args, int cursor) {
		return getArguments() == null || !getArguments().containsKey(cursor) ? Optional.empty() : getArguments().get(cursor).getResult(cause, args).map(value -> {
				try {
					return (T) value;
				} catch (Exception e) {
					return null;
				}
			}
		);
	}

	/**
	 * Getting an object from a command argument.
	 * 
	 * @param <T> - The type of the returned object.
	 * @param cause - {@link CommandCause}
	 * @param clazz - The class of the returned object.
	 * @param args - All command arguments.
	 * @return {@link Optional}
	 */
	/*@SuppressWarnings("unchecked")
	default <T> Optional<T> getArgument(Class<T> clazz, CommandCause cause, String[] args) {
		return getArguments() == null ? Optional.empty() : getArguments().values().stream().filter(arg -> arg.getAssociatedClass().isAssignableFrom(clazz)).findFirst().map(arg -> arg.getResult(cause, args).map(value -> {
					try {
						return (T) value;
					} catch (Exception e) {
						return null;
					}
				}
			)
		)
		.filter(value -> value.isPresent())
		.orElse(Optional.empty());
	}

	/**
	 * Getting an object from a command argument.
	 * 
	 * @param <T> - The type of the returned object.
	 * @param cause - {@link CommandCause}
	 * @param args - All command arguments.
	 * @return {@link Optional}
	 */
	/*@SuppressWarnings("unchecked")
	default <T> Optional<T> getArgument(CommandCause cause, String[] args) {
		return getArguments() == null ? Optional.empty() : getArguments().values().stream().map(arg -> arg.getResult(cause, args)).filter(value -> !value.isEmpty()).findFirst().map(value -> {
				try {
					return (T) value;
				} catch (Exception e) {
					return null;
				}
			}
		);
	}

	/**
	 * Convert string to object {@link Duration}
	 */
	default Optional<Duration> parseDuration(String s, Locale locale) throws CommandException {
		s = s.toUpperCase();
		if (!s.contains("T")) {
			if (s.contains("D")) {
				if (s.contains("H") || s.contains("M") || s.contains("S")) {
					s = s.replace("D", "DT");
				}
			} else {
				if (s.startsWith("P")) {
					s = "PT" + s.substring(1);
				} else {
					s = "T" + s;
				}
			}
		}
		if (!s.startsWith("P")) {
			s = "P" + s;
		}
		try {
			return Optional.ofNullable(Duration.parse(s));
		} catch (final DateTimeParseException ex) {
			throw exception(CommandPack.getInstance().getLocales().getText(locale, LocalesPaths.COMMANDS_EXCEPTION_COOLDOWN_INCORRECT_TIME));
		}
	}

	@SuppressWarnings("unused")
	@Deprecated
	private Object[] getChildExecutor(CommandCause cause, String[] args) {
		if(args.length != 0 && getChildExecutors() != null && !getChildExecutors().isEmpty() && getChildExecutors().containsKey(args[0]) && getChildExecutors().get(args[0]).canExecute(cause)) {
			return getChildExecutors().get(args[0]).getChildExecutor(cause, args.length > 1 ? Stream.of(Arrays.copyOfRange(args, 1, args.length + 1)).filter(arg -> arg != null).toArray(String[]::new) : new String[] {});
		}
		return new Object[] {this, args};
	}

	private Object[] getChildExecutor(CommandCause cause, RawArgumentsMap args) {
		if(args.getInput().length != 0 && getChildExecutors() != null && !getChildExecutors().isEmpty() && getChildExecutors().containsKey(args.getInput()[0]) && getChildExecutors().get(args.getInput()[0]).canExecute(cause)) {
			return getChildExecutors().get(args.getInput()[0]).getChildExecutor(cause, args.getInput().length > 1 ? createResultCollection(cause, Stream.of(Arrays.copyOfRange(args.getInput(), 1, args.getInput().length + 1)).filter(arg -> arg != null).toArray(String[]::new)) : RawArgumentsMap.empty());
		}
		return new Object[] {this, args};
	}

	/**
	 * No need to {@link Override} any more.
	 */
	default List<CommandCompletion> getEmptyCompletion() {
		return CommandsUtil.EMPTY_COMPLETIONS;
	}

	/**
	 * Creating a command argument map from a list.
	 * 
	 * @param args - A collection of arguments.
	 * @return Arguments {@link Map}. Key {@link Integer} - argument number, Value {@link RawArgument} - raw argument.
	 */
	default Map<Integer, RawArgument<?>> createArgumentsMap(@Nullable Collection<RawArgument<?>> args) {
		return args == null ? new HashMap<>() : args.stream().collect(Collectors.toUnmodifiableMap(arg -> arg.getCursor(), arg -> arg, (arg1, arg2) -> {
			CommandPack.getInstance().getLogger().warn("A duplicate command argument key was detected. The duplicate will not be added to the argument map. Command: \"" + command() + "\". Command class: \"" + getClass().getName() + "\". Argument key: \"" + arg1.getTreeKey() + "\"");
			return arg1;
		}));
	}

	// Need tests
	default RawArgumentsMap createResultCollection(CommandCause cause, String[] args) {
		return RawArgumentsMap.create(this, cause, args);
	}

	default boolean containsChild() {
		return getChildExecutors() != null && !getChildExecutors().isEmpty();
	}

	default boolean containsArgs() {
		return getArguments() != null && !getArguments().isEmpty();
	}

}
