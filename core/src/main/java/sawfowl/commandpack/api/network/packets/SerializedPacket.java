package sawfowl.commandpack.api.network.packets;

import java.util.function.Function;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

/**
 * This interface allows you to work with serialized data that the client sends to the server.<br>
 * You do not need to create an instance of the class implementing this interface, it cannot be sent to the client. Currently, data can only be sent to the client in its raw form.<br>
 * However, you must register a serializer that will convert the raw data into the object you need.
 */
public interface SerializedPacket<T> extends DataSerializable {

	@SuppressWarnings("unchecked")
	static <T> SerializedPacket<T> of(ResourceKey channel, Function<String, T> function) {
		return (SerializedPacket<T>) builder().build(channel, (Function<String, Object>) function);
	}

	@SuppressWarnings("unchecked")
	static <T> SerializedPacket<T> of(ResourceKey channel, Function<String, T> function, Class<T> clazz) {
		return (SerializedPacket<T>) builder().build(channel, (Function<String, Object>) function);
	}

	@SuppressWarnings("unchecked")
	private static <T> Builder<T> builder() {
		return (Builder<T>) Sponge.game().builderProvider().provide(Builder.class);
	}

	/**
	 * Getting serialized data.<br>
	 * Serialization is performed by the serializer you created and registered.
	 */
	T get();

	/**
	 * Identifier of the data channel.
	 */
	ResourceKey channel();

	interface Builder<T> extends AbstractBuilder<SerializedPacket<T>>, org.spongepowered.api.util.Builder<SerializedPacket<T>, Builder<T>> {

		SerializedPacket<T> build(ResourceKey channel, Function<String, T> function);

	}

}
