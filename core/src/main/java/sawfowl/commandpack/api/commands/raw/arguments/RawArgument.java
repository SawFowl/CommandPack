package sawfowl.commandpack.api.commands.raw.arguments;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.Command.Raw;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.registrar.tree.CommandTreeNode;
import org.spongepowered.api.command.registrar.tree.CommandTreeNode.Argument;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

import sawfowl.commandpack.api.commands.raw.RawCommand;

/**
 * The interface is designed to simplify the creation of {@link Raw} type commands.<br>
 * You can use it to create arguments for this commands, just like you do for {@link Parameterized} commands.<br>
 * Unlike {@link Parameterized} commands, {@link Raw} commands have no problem with the availability of game objects, which are registered after the commands are registered.<br>
 * However, adding autocomplete to Raw commands and then checking the arguments is very time-consuming.<br>
 * Using this interface together with the {@link RawCommand} interface saves you from having to do such time-consuming work.
 * 
 * @author SawFowl
 *
 * @param <T> - The object type of the command argument.
 */
public interface RawArgument<T> extends DataSerializable {

	@SuppressWarnings("unchecked")
	static <T> Builder<T> builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	static <T> RawArgument<T> of(Class<T> clazz, RawCompleterSupplier<Stream<String>> variants, RawResultSupplier<T> result, boolean optional, boolean optionalForConsole, int cursor, Object... localesPath) {
		return (RawArgument<T>) builder().variants(variants).result(clazz, result).optional(optional).optionalForConsole(optionalForConsole).cursor(cursor).localeTextPath(localesPath).build();
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	static <T> RawArgument<T> of(Class<T> clazz, RawCompleterSupplier<Stream<String>> variants, RawResultSupplier<T> result, boolean optional, boolean optionalForConsole, int cursor, String permission, Object... localesPath) {
		return (RawArgument<T>) builder().variants(variants).result(clazz, result).optional(optional).optionalForConsole(optionalForConsole).cursor(cursor).permission(permission).localeTextPath(localesPath).build();
	}

	@SuppressWarnings("unchecked")
	static <T, C extends CommandTreeNode<C>> RawArgument<T> of(Class<T> clazz, Argument<C> argumentNodeType, RawCompleterSupplier<Stream<String>> plainVariants, Supplier<Stream<String>> treeVariants, RawResultSupplier<T> result, RawCompleterPredicate<CommandCause, Stream<String>> canUse, boolean optional, boolean optionalForConsole, int cursor, String permission, Object... localesPath) {
		return (RawArgument<T>) builder().setArgumentType(argumentNodeType).variants(plainVariants).variants(treeVariants).result(clazz, result).optional(optional).optionalForConsole(optionalForConsole).cursor(cursor).permission(permission).canUse(canUse).localeTextPath(localesPath).build();
	}

	static <T> RawArgument<T> of(Class<T> clazz, Stream<String> variants, RawResultSupplier<T> result, boolean optional, boolean optionalForConsole, int cursor, Object[] localesPath) {
		return of(clazz, null, ((CommandCause cause, String[] args) -> args.length > cursor ? variants.filter(var -> var.startsWith(args[cursor])) : variants), () -> variants, result, null, optional, optionalForConsole, cursor, null, localesPath);
	}

	@Deprecated
	static <T> RawArgument<T> of(Class<T> clazz, Stream<String> variants, RawResultSupplier<T> result, boolean optional, boolean optionalForConsole, int cursor, String permission, Object... localesPath) {
		return of(clazz, null, ((CommandCause cause, String[] args) -> args.length > cursor ? variants.filter(var -> var.startsWith(args[cursor])) : variants), () -> variants, result, null, optional, optionalForConsole, cursor, permission, localesPath);
	}

	Argument<?> getArgumentType();

	/**
	 * Autocomplete variants.
	 */
	Stream<String> getVariants(CommandCause cause, String[] args);

	Collection<String> getVariants();

	/**
	 * Converts a string argument to a specified class. If the specified class does not match the argument class, the return will be empty.
	 */
	Optional<T> getResult(Class<T> clazz, String[] args);

	/**
	 * Converts a string argument to a specified class. If the specified class does not match the argument class, the return will be empty.
	 */
	Optional<T> getResult(Class<T> clazz, CommandCause cause, String[] args);

	/**
	 * Retrieves an object from the command's argument string without converting it to a specific type.
	 */
	Optional<?> getResultUnknownType(CommandCause cause, String[] args);

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
	Class<?> getClazz();

	Optional<String> getPermision();

	boolean hasPermission(CommandCause cause);

	boolean canUse(CommandCause cause, String input);

	interface Builder<T> extends AbstractBuilder<RawArgument<T>>, org.spongepowered.api.util.Builder<RawArgument<T>, Builder<T>> {

		/**
		 * Set variants for auto-complete commands.
		 */
		Builder<T> variants(@NotNull RawCompleterSupplier<Stream<String>> variants);

		Builder<T> variants(Supplier<Stream<String>> variants);

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

		Builder<T> canUse(RawCompleterPredicate<CommandCause, Stream<String>> predicate);

		<C extends CommandTreeNode<C>> Builder<T> setArgumentType(Argument<C> node);

	}

}
