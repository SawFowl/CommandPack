package sawfowl.commandpack.api.commands.raw;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

public interface RawArgument<T> extends DataSerializable {

	@SuppressWarnings("unchecked")
	static <T> Builder<T> builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	@SuppressWarnings("unchecked")
	static <T> RawArgument<T> of(Class<T> clazz, Supplier<Stream<String>> variants, RawSupplier<?> result, boolean optional, boolean optionalForConsole, int cursor ,Object[] localesPath) {
		return (RawArgument<T>) builder().variants(variants).result(clazz, result).optional(optional).optionalForConsole(optionalForConsole).cursor(cursor).localesPath(localesPath).build();
	}

	Stream<String> getVariants();

	Optional<T> getResult(Class<T> clazz, String[] args);

	boolean isOptional();

	boolean isOptionalForConsole();

	int getCursor();

	Object[] getLocalesPath();

	Class<?> getClazz();

	interface Builder<T> extends AbstractBuilder<RawArgument<T>>, org.spongepowered.api.util.Builder<RawArgument<T>, Builder<T>> {

		Builder<T> variants(Supplier<Stream<String>> variants);

		Builder<T> result(Class<?> clazz, RawSupplier<?> result);

		Builder<T> optional(boolean value);

		Builder<T> optionalForConsole(boolean value);

		Builder<T> cursor(int value);

		Builder<T> localesPath(Object[] value);

	}

}
