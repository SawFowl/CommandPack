package sawfowl.commandpack.api.commands.raw;

import java.util.Optional;
import java.util.stream.Stream;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

public interface RawArgument<T> extends DataSerializable {

	@SuppressWarnings("unchecked")
	static <T> Builder<T> builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	@SuppressWarnings("unchecked")
	static <T> RawArgument<T> of(Class<T> clazz, RawCompleterSupplier<Stream<String>> variants, RawResultSupplier<T> result, boolean optional, boolean optionalForConsole, int cursor ,Object[] localesPath) {
		return (RawArgument<T>) builder().variants(variants).result(clazz, result).optional(optional).optionalForConsole(optionalForConsole).cursor(cursor).localesPath(localesPath).build();
	}

	static <T> RawArgument<T> of(Class<T> clazz, Stream<String> variants, RawResultSupplier<T> result, boolean optional, boolean optionalForConsole, int cursor ,Object[] localesPath) {
		return of(clazz, new RawCompleterSupplier<Stream<String>>() {
			@Override
			public Stream<String> get(String[] args) {
				return variants;
			}
		}, result, optional, optionalForConsole, cursor, localesPath);
	}

	Stream<String> getVariants(String[] args);

	Optional<T> getResult(Class<T> clazz, String[] args);

	Optional<?> getResultUnknownType(String[] args) throws CommandException;

	boolean isOptional();

	boolean isOptionalForConsole();

	int getCursor();

	Object[] getLocalesPath();

	Class<?> getClazz();

	interface Builder<T> extends AbstractBuilder<RawArgument<T>>, org.spongepowered.api.util.Builder<RawArgument<T>, Builder<T>> {

		Builder<T> variants(RawCompleterSupplier<Stream<String>> variants);

		Builder<T> result(Class<?> clazz, RawResultSupplier<?> result);

		Builder<T> optional(boolean value);

		Builder<T> optionalForConsole(boolean value);

		Builder<T> cursor(int value);

		Builder<T> localesPath(Object[] value);

	}

}
