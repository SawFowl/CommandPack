package sawfowl.commandpack.api.commands.raw;

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

import org.apache.commons.lang3.ArrayUtils;

import org.jetbrains.annotations.Nullable;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
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

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.commands.PluginCommand;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgument;
import sawfowl.commandpack.api.commands.raw.arguments.RawArgumentsMap;
import sawfowl.commandpack.utils.CommandsUtil;
import sawfowl.localeapi.api.Text;

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
					return completeArgs(context.cause(), Stream.of(currentInput.split(" ")).filter(string -> (!string.equals(""))).toArray(String[]::new), currentInput);
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

	/**
	 * Checks for child commands and who activated the command.
	 */
	@Override
	default CommandResult process(CommandCause cause, Mutable arguments) throws CommandException {
		boolean isPlayer = cause.first(ServerPlayer.class).isPresent();
		Locale locale = getLocale(cause);
		String[] args = Stream.of(arguments.input().split(" ")).map(String::toString).filter(string -> (string != null && !string.equals(""))).toArray(String[]::new);
		Object[] child = getChildExecutor(cause, args);
		if(child != null && child.length > 0) {
			((RawCommand) child[0]).process(cause, cause.audience(), locale, isPlayer, arguments, ((RawCommand) child[0]).checkArguments(cause, ((RawCommand) child[0]).createResultCollection(cause, (String[]) child[1]), isPlayer, locale));
			args = null;
			child = null;
		} else process(cause, cause.first(ServerPlayer.class).map(player -> (Audience) player).orElse(cause.audience()), locale, isPlayer, arguments, checkArguments(cause, createResultCollection(cause, args), isPlayer, locale));
		return success();
	}

	@Override
	default List<CommandCompletion> complete(CommandCause cause, Mutable arguments) throws CommandException {
		return enableAutoComplete() ? completeChild(cause, Stream.of(arguments.input().split(" ")).filter(string -> (!string.equals(""))).toArray(String[]::new), arguments.input()) : CommandsUtil.getEmptyList();
	}

	default RawArgumentsMap checkChildAndArguments(CommandCause cause, RawArgumentsMap args, boolean isPlayer, Locale locale) throws CommandException {
		if(args.getInput().length != 0 && getChildExecutors() != null && !getChildExecutors().isEmpty() && getChildExecutors().containsKey(args.getInput()[0]) && getChildExecutors().get(args.getInput()[0]).canExecute(cause)) {
			return getChildExecutors().get(args.getInput()[0]).checkChildAndArguments(cause, (args.getInput().length > 1 ? createResultCollection(cause, Stream.of(Arrays.copyOfRange(args.getInput(), 1, args.getInput().length + 1)).filter(arg -> arg != null).toArray(String[]::new)) : RawArgumentsMap.empty()), isPlayer, locale);
		} else return checkArguments(cause, args, isPlayer, locale);
	}

	default RawArgumentsMap checkArguments(CommandCause cause, RawArgumentsMap args, boolean isPlayer, Locale locale) throws CommandException {
		if(getArguments() != null && args != null) {
			int size = getArguments().size();
			for(int current = 0; current<=size; current++) {
				RawArgument<?> arg = getArguments().get(current);
				if(arg != null && (!arg.isOptional() || (!isPlayer && !arg.isOptionalForConsole())) && !args.idSet().contains(arg.getCursor())) {
					exceptionAppendUsage(cause, arg.exception(locale), arg.getTreeKey());
				}
				arg = null;
			}
		}
		return args;
	}

	default List<CommandCompletion> completeChild(CommandCause cause, String[] args, String currentInput) {
		
		if(getChildExecutors() != null && !getChildExecutors().isEmpty()) {
			if(args.length == 0 || (args.length == 1 && !currentInput.endsWith(" "))) {
				return new ArrayList<CommandCompletion>() {
					private static final long serialVersionUID = 1L;
					{
						addAll(getChildExecutors().keySet().stream().filter(command -> (args.length == 0 || ((command.equals(args[0]) || command.startsWith(args[0])) && getChildExecutors().get(command).canExecute(cause)))).map(CommandCompletion::of).collect(Collectors.toList()));
						addAll(completeArgs(cause, args, currentInput));
					}
				};
			} else if(getChildExecutors().containsKey(args[0])) {
				return new ArrayList<CommandCompletion>() {
					private static final long serialVersionUID = 1L;
					{
						addAll(getChildExecutors().get(args[0]).completeChild(cause, ArrayUtils.remove(args, 0), currentInput.contains(args[0] + " ") ? currentInput.replace(args[0] + " ", "") : currentInput.replace(args[0], "")));
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
	default List<CommandCompletion> completeArgs(CommandCause cause, String[] args, String currentInput) {
		if(!enableAutoComplete() || getArguments() == null || getArguments().size() < args.length || (!getArguments().isEmpty() && !getArguments().get(args.length > 0 ? args.length - 1 : 0).hasPermission(cause)) || (getArguments().containsKey(args.length > 0 ? args.length : 0) && !getArguments().get(args.length > 0 ? args.length : 0).checkRequiredOtherArguments(cause, getArguments(), args))) return CommandsUtil.getEmptyList();
		if(currentInput.endsWith(" ") || args.length == 0) {
			if(getArguments().containsKey(args.length)) return getArguments().get(args.length).getVariants(cause, args).map(CommandCompletion::of).collect(Collectors.toList());
			return CommandsUtil.getEmptyList();
		} else return getArguments().get(args.length - 1).getVariants(cause, args).filter(v -> ((v.contains(args[args.length - 1]) && !currentInput.endsWith(" ")) || (args[args.length - 1].contains(v) && !args[args.length - 1].contains(v + " ")) || (v.contains(":") && (v.split(":")[0].contains(args[args.length - 1]) || v.split(":")[1].contains(args[args.length - 1]))))).map(CommandCompletion::of).collect(Collectors.toList());
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
			if(getCommandSettings() == null || getCommandSettings().isEnable()) CommandPackInstance.getInstance().getAPI().registerCommand(this);
		} else {
			if(!getCommandSettings().isEnable()) return;
			if(getCommandSettings() == null || getCommandSettings().isEnable()) CommandPackInstance.getInstance().getAPI().registerCommand(this);
		}
		CommandPackInstance.getInstance().getPlayersData().getTempData().addTrackingCooldownCommand(this);
	}

	default CommandException exceptionAppendUsage(CommandCause cause, Component text) throws CommandException {
		throw new CommandException(usage(cause) == null ? text : text.append(Component.newline()).append(usage(cause)));
	}

	default CommandException exceptionAppendUsage(CommandCause cause, Component text, String markArg) throws CommandException {
		throw new CommandException(usage(cause) == null ? text : text.append(Component.newline()).append(Text.of(usage(cause)).replace(markArg, "↳" + markArg + "↲").get()));
	}

	default CommandException exceptionAppendUsage(CommandCause cause, Locale locale, Object[] localePath) throws CommandException {
		throw new CommandException(getComponent(locale, localePath).append(Component.newline()).append(usage(cause)));
	}

	default List<CommandCompletion> getEmptyCompletion() {
		return CommandsUtil.getEmptyList();
	}

	/**
	 * Creating a command argument map from a list.
	 * 
	 * @param args - A collection of arguments.
	 * @return Arguments {@link Map}. Key {@link Integer} - argument number, Value {@link RawArgument} - raw argument.
	 */
	default Map<Integer, RawArgument<?>> createArgumentsMap(@Nullable Collection<RawArgument<?>> args) {
		return args == null ? new HashMap<>() : args.stream().collect(Collectors.toUnmodifiableMap(arg -> arg.getCursor(), arg -> arg, (arg1, arg2) -> {
			CommandPackInstance.getInstance().getLogger().warn("A duplicate command argument key was detected. The duplicate will not be added to the argument map. Command: \"" + command() + "\". Command class: \"" + getClass().getName() + "\". Argument key: \"" + arg1.getTreeKey() + "\"");
			return arg1;
		}));
	}

	default boolean containsChild() {
		return getChildExecutors() != null && !getChildExecutors().isEmpty();
	}

	default boolean containsArgs() {
		return getArguments() != null && !getArguments().isEmpty();
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
			Optional<String> perm = command.getArguments().get(depth).getPermision();
			if(perm.isPresent()) node = (Argument<?>) node.requires(cause -> cause.hasPermission(perm.get()));
			parrent.child(command.getArguments().get(depth).getTreeKey(), node);
		}
		argsTree(depth + 1, (AbstractCommandTreeNode<?, ?>) node, command);
		node = null;
	}

	private Object[] getChildExecutor(CommandCause cause, String[] args) {
		if(args.length != 0 && getChildExecutors() != null && !getChildExecutors().isEmpty() && getChildExecutors().containsKey(args[0]) && getChildExecutors().get(args[0]).canExecute(cause)) {
			return getChildExecutors().get(args[0]).getChildExecutor(cause, args.length > 1 ? Stream.of(Arrays.copyOfRange(args, 1, args.length + 1)).filter(arg -> arg != null).toArray(String[]::new) : new String[] {});
		}
		return new Object[] {this, args};
	}

	private RawArgumentsMap createResultCollection(CommandCause cause, String[] args) {
		return RawArgumentsMap.create(this, cause, args);
	}

}
