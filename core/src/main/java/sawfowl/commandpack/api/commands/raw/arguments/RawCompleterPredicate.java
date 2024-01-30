package sawfowl.commandpack.api.commands.raw.arguments;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.spongepowered.api.command.CommandCause;

public interface RawCompleterPredicate<C extends CommandCause, V extends Stream<String>> extends Supplier<Object> {

	public boolean test(C cause, V variants);

}
