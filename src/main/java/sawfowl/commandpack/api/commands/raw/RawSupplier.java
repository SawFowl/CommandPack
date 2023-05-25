package sawfowl.commandpack.api.commands.raw;

import java.util.Optional;

@FunctionalInterface
public interface RawSupplier<T> {

	Optional<T> get(String[] args);

}
