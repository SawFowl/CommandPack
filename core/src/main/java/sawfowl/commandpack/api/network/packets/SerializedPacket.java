package sawfowl.commandpack.api.network.packets;

import java.util.function.Function;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

public interface SerializedPacket<T> extends DataSerializable {

	@SuppressWarnings("unchecked")
	static <T> SerializedPacket<T> of(Function<String, T> function) {
		return (SerializedPacket<T>) builder().build((Function<String, Object>) function);
	}

	@SuppressWarnings("unchecked")
	static <T> SerializedPacket<T> of(Function<String, T> function, Class<T> clazz) {
		return (SerializedPacket<T>) builder().build((Function<String, Object>) function);
	}

	@SuppressWarnings("unchecked")
	private static <T> Builder<T> builder() {
		return (Builder<T>) Sponge.game().builderProvider().provide(Builder.class);
	}

	T get();

	interface Builder<T> extends AbstractBuilder<SerializedPacket<T>>, org.spongepowered.api.util.Builder<SerializedPacket<T>, Builder<T>> {

		SerializedPacket<T> build(Function<String, T> function);

	}

}
