package sawfowl.commandpack.api.commands.raw.arguments;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.registrar.tree.CommandTreeNode;
import org.spongepowered.api.command.registrar.tree.CommandTreeNodeTypes;
import org.spongepowered.api.command.registrar.tree.CommandTreeNode.Argument;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

import sawfowl.commandpack.api.commands.raw.RawCommand;

/**
 * You can use this interface to create arguments for commands implementing the {@link RawCommand} interface.<br>
 * This is a bit like the way arguments work in {@link Parameterized} type commands.<br>
 * All command arguments will be sequenced from 0 to the specified maximum of the last argument.<br>
 * In addition, the arguments are assigned an identifier as a string value.
 * 
 * @author SawFowl
 *
 * @param <T> - The object type of the command argument.
 */
public interface RawArgument<T> extends DataSerializable {

	@SuppressWarnings("unchecked")
	private static <T> Builder<T> builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	/**
	 * @param clazz - Argument object type.
	 * @param argumentNodeType - {@link CommandTreeNode} type
	 * @param variants - See {@link RawCompleterSupplier}
	 * @param result - See {@link RawResultSupplier}
	 * @param key - Argument {@link String} key
	 * @param optional - If true, the argument will be optional.
	 * @param optionalForConsole - If true, the argument will be optional for the console. Parameter `optional` has a higher priority.
	 * @param cursor - The number of the argument in the array of arguments input.
	 * @param permission - The permission required to use the argument. Can be `null`.
	 * @param localesPath - Path to the text in the plugin localization. The text will be shown if the argument was not entered or was entered incorrectly and it is mandatory.
	 * @param requiredArgumentsById - Listing the required arguments by their id. Can be `null`.
	 * @param requiredArgumentsByKey - Listing the required arguments by their keys. Can be `null`.
	 * @return {@link RawArgument}
	 */
	@SuppressWarnings("unchecked")
	static <T, C extends CommandTreeNode<C>> RawArgument<T> of(@NotNull Class<T> clazz, Argument<C> argumentNodeType, @NotNull RawCompleterSupplier<Stream<String>> variants, @NotNull RawResultSupplier<T> result, @NotNull String key, boolean optional, boolean optionalForConsole, int cursor, String permission, Integer[] requiredArgumentsById, String[] requiredArgumentsByKey, @NotNull Object... localesPath) {
		return (RawArgument<T>) builder().setArgumentType(argumentNodeType).variants(variants).result(clazz, result).optional(optional).optionalForConsole(optionalForConsole).cursor(cursor).permission(permission).treeKey(key).localeTextPath(localesPath).build();
	}


	/**
	 * Specifies the argument type.<br>
	 * See the {@link CommandTreeNodeTypes} class for available variants.<br>
	 * The specified type defines what the player can enter as an argument.<br>
	 * This is not controlled by the plugin, but the player will see an error message if the value type is not appropriate.
	 */
	Argument<?> getArgumentType();

	/**
	 * The identifier of the argument in the argument tree.<br>
	 * It is also displayed to the player above the input line as a hint.
	 */
	String getTreeKey();

	/**
	 * Autocomplete variants.
	 */
	Stream<String> getVariants(CommandCause cause, String[] args);

	/**
	 * Converts a string argument to a specified class. If the specified class does not match the argument class, the return will be empty.
	 */
	<V extends T> Optional<V> getResult(CommandCause cause, String[] args);

	/**
	 * Whether the argument is optional.
	 */
	boolean isOptional();

	/**
	 * Is the argument optional for non-players.
	 */
	boolean isOptionalForConsole();

	/**
	 * Argument number. The numbering starts with 0.
	 */
	int getCursor();

	/**
	 * Path to find the message that there is no matching command argument in the localization configuration.
	 */
	Object[] getLocalesPath();

	/**
	 * Class of argument.
	 */
	Class<?> getAssociatedClass();

	/**
	 * Getting the required permission for the argument, if available.
	 */
	Optional<String> getPermision();

	/**
	 * Checks for permission to use the argument, if permission is set.
	 */
	boolean hasPermission(CommandCause cause);

	/**
		 * Listing the required arguments by their id.
	 */
	Integer[] getRequiredArgumentsById();

	/**
	 * Listing the required arguments by their keys.
	 */
	String[] getRequiredArgumentsByKey();

	/**
	 * The method checks if the other arguments of the command are entered correctly.<br>
	 * The method does not perform parsing of the argument. Only valid string values are checked.
	 * 
	 * @return `true` if there is no requirement to check other arguments or they are entered correctly.<br>`false`, if the required argument is not entered or is entered incorrectly.
	 */
	boolean checkRequiredOtherArguments(CommandCause cause, Map<Integer, RawArgument<?>> args, String[] inputArgs);

	interface Builder<T> extends AbstractBuilder<RawArgument<T>>, org.spongepowered.api.util.Builder<RawArgument<T>, Builder<T>>, RawArgument<T> {

		/**
		 * Set variants for auto-complete commands.
		 */
		Builder<T> variants(@NotNull RawCompleterSupplier<Stream<String>> variants);

		/**
		 * Setting parameters to get the target object from the command argument.
		 */
		Builder<T> result(@NotNull Class<?> clazz, @NotNull RawResultSupplier<?> result);

		/**
		 * Whether the argument is optional.
		 */
		Builder<T> optional(boolean value);

		/**
		 * Is the argument optional for non-players.
		 */
		Builder<T> optionalForConsole(boolean value);

		/**
		 * Argument number. The numbering starts with 0.
		 */
		Builder<T> cursor(int value);

		/**
		 * Path to find the message that there is no matching command argument in the localization configuration.
		 */
		Builder<T> localeTextPath(Object... value);

		/*
		 * The required permission for this argument.
		 */
		Builder<T> permission(String value);

		/**
		 * The identifier of the argument in the argument tree.<br>
		 * It is also displayed to the player above the input line as a hint.
		 */
		Builder<T> treeKey(String value);

		/**
		 * Specifies the argument type.<br>
		 * See the {@link CommandTreeNodeTypes} class for available variants.<br>
		 * The specified type defines what the player can enter as an argument.<br>
		 * This is not controlled by the plugin, but the player will see an error message if the value type is not appropriate.
		 */
		<C extends CommandTreeNode<C>> Builder<T> setArgumentType(Argument<C> node);

		/**
		 * Listing the required arguments by their id.
		 */
		Builder<T> setRequiredArguments(Integer... ids);

		/**
		 * Listing the required arguments by their keys.
		 */
		Builder<T> setRequiredArguments(String... keys);

	}

}
