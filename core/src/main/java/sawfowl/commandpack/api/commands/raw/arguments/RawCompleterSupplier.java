package sawfowl.commandpack.api.commands.raw.arguments;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.spongepowered.api.command.CommandCause;
/**
 * 
 * The work of this interface is similar to that of the {@link Supplier} interface. But this interface returns only for the string stream.
 * 
 * @author SawFowl
 *
 * @param <T> - The object type of the command argument.
 */
@FunctionalInterface
public interface RawCompleterSupplier<T> {

	/**
	 * Returns a stream of strings to auto-complete the command with the argument. The array of strings passed to the method can be used to filter valid arguments.
	 * 
	 * @param cause - {@link CommandCause}
	 * @param args - Array of input arguments. You need to select an element from an array with an argument number to perform a search for possible speed dial options.
	 * @return {@link Stream} of variants for fast argument input. The stream shouldn't be finished! The use of empty stream is allowed.
	 */
	Stream<String> get(CommandCause cause, String[] args);

}
