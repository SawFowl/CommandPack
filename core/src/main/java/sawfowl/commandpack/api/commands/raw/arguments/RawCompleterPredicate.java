package sawfowl.commandpack.api.commands.raw.arguments;

import java.util.stream.Stream;

import org.spongepowered.api.command.CommandCause;

@FunctionalInterface
public interface RawCompleterPredicate<C extends CommandCause, V extends Stream<String>> {

	public boolean test(C cause, V variants, String input);

}
