package sawfowl.commandpack.api.commands.raw;

import java.util.Optional;

@FunctionalInterface
public interface RawResultSupplier<T> {

	Optional<T> get(String[] args);

}
