package sawfowl.commandpack.api.commands.raw;

public interface RawSupplier<T> {

	T get(String[] args);

}
