package sawfowl.commandpack.commands;

import java.util.function.Consumer;

import org.spongepowered.api.command.exception.CommandException;

public interface ThrowingConsumer<T, E extends CommandException> {
	void accept(T t) throws E;

	static <T, E extends CommandException> Consumer<T> unchecked(ThrowingConsumer<T, E> consumer) {
	return (t) -> {
			try {
				consumer.accept(t);
			} catch (CommandException e) {
			}
		};
	}
}