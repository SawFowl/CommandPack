package sawfowl.commandpack.api.commands.raw;

import java.util.stream.Stream;

@FunctionalInterface
public interface RawCompleterSupplier<T> {

	Stream<String> get(String[] args);

}
