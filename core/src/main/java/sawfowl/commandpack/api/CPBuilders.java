package sawfowl.commandpack.api;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

/**
 * If possible, use SpongeAPI instead of this class.
 */
public class CPBuilders {

	private static final Map<Class<? extends AbstractBuilder<? extends DataSerializable>>, Supplier<AbstractBuilder<? extends DataSerializable>>> BUILDERS = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static <B extends AbstractBuilder<? extends DataSerializable>> void register(Class<B> clazz, Supplier<B> builder) {
		if(!BUILDERS.containsKey(clazz)) BUILDERS.put(clazz, (Supplier<AbstractBuilder<? extends DataSerializable>>) builder);
	}

	@SuppressWarnings("unchecked")
	public static <T extends AbstractBuilder<? extends DataSerializable>> Supplier<T> getBuilder(Class<T> clazz) {
		return (Supplier<T>) BUILDERS.get(clazz);
	}

}
