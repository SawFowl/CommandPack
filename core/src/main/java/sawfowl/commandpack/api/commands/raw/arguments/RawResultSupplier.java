package sawfowl.commandpack.api.commands.raw.arguments;

import java.util.Optional;
import java.util.function.Supplier;

import org.spongepowered.api.command.CommandCause;

/**
 * 
 * The work of this interface is similar to that of the {@link Supplier} interface.
 * 
 * @author SawFowl
 *
 * @param <T> - The object type of the command argument.
 */
@FunctionalInterface
public interface RawResultSupplier<T> {

	/**
	 * Retrieves an instance of the appropriate type. The returned object may or may not be a new instance, depending on the implementation.<br>
	 * Depending on the conditions, a empty return is allowed.
	 * 
	 * @param cause - {@link CommandCause}
	 * @param args - Array of input arguments. It is necessary to select an element with the argument number from the array and search for the required object by it.
	 * @return {@link Optional} with argument T or empty {@link Optional}
	 */
	Optional<T> get(CommandCause cause, String[] args);

}
