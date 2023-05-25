package sawfowl.commandpack.api.commands.raw;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

public interface RawArgument<T> extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	@SuppressWarnings("unchecked")
	static <T> RawArgument<T> of(Class<T> clazz, Supplier<Stream<String>> variants, RawSupplier<?> result, boolean optional, boolean optionalForConsole, Object defaultValue, int cursor ,Object[] localesPath) {
		return (RawArgument<T>) builder().variants(variants).result(result).optional(optional).optionalForConsole(optionalForConsole).defaultValue(defaultValue).cursor(cursor).localesPath(localesPath).build();
	}

	Stream<String> getVariants();

	T getResult(Class<T> object, String[] args);

	boolean isOptional();

	boolean isOptionalForConsole();

	int getCursor();

	Object[] getLocalesPath();

	interface Builder extends AbstractBuilder<RawArgument<?>>, org.spongepowered.api.util.Builder<RawArgument<?>, Builder> {

		Builder variants(Supplier<Stream<String>> variants);

		Builder result(RawSupplier<?> result);

		Builder optional(boolean value);

		Builder optionalForConsole(boolean value);

		Builder defaultValue(Object value);

		Builder cursor(int value);

		Builder localesPath(Object[] value);

	}

}
