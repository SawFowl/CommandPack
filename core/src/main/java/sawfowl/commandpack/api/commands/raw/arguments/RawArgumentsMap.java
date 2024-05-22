package sawfowl.commandpack.api.commands.raw.arguments;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

import sawfowl.commandpack.api.commands.raw.RawCommand;

/**
 * A map of the entered arguments of the command.
 * 
 * @author SawFowl
 */
public interface RawArgumentsMap extends DataSerializable {

	private static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	/**
	 * Creating a map of the entered command arguments.
	 * 
	 * @param command - {@link RawCommand}
	 * @param cause - {@link CommandCause}
	 * @param args - Array of input arguments.
	 * @return {@link RawArgumentsMap}
	 */
	public static RawArgumentsMap create(RawCommand command, CommandCause cause, String[] args) {
		return builder().create(command, cause, args);
	}

	/**
	 * Getting the value of an argument by its key.
	 * 
	 * @param <T> - argument type
	 * @param key - argument key
	 * @return {@link Optional} with argument T or empty {@link Optional}
	 */
	<T> Optional<T> get(String key);

	/**
	 * Getting the value of an argument by its key.
	 * 
	 * @param <T> - argument type
	 * @param id - argument number
	 * @return {@link Optional} with argument T or empty {@link Optional}
	 */
	<T> Optional<T> get(int id);

	/**
	 * Getting the value of an argument by its key.
	 * 
	 * @param <T> - argument type
	 * @param valueType - argument type
	 * @param key - argument number
	 * @return {@link Optional} with argument T or empty {@link Optional}
	 */
	<T> Optional<T> get(Class<T> valueType, String key);

	/**
	 * Getting the value of an argument by its key.
	 * 
	 * @param <T> - argument type
	 * @param valueType - argument type
	 * @param id - argument number
	 * @return {@link Optional} with argument T or empty {@link Optional}
	 */
	<T> Optional<T> get(Class<T> valueType, int id);

	/**
	 * Performs the given action for each entry in this map until all entries
	 * have been processed or the action throws an exception.   Unless
	 * otherwise specified by the implementing class, actions are performed in
	 * the order of entry set iteration (if an iteration order is specified.)
	 * Exceptions thrown by the action are relayed to the caller.
	 * 
	 * @param <K> - argument key type. Only `String` and `Integer` are allowed.
	 * @param <V> - The return value of the argument in this method will always refer to the `Object` class.
	 * @param key - argument key type. Only {@link String} and {@link Integer} are allowed.
	 * @param action - The action to be performed for each entry
	 * @throws @{@link IllegalStateException} If an invalid key type is specified.
	 */
	<K, V extends Object> void forEach(Class<K> key, BiConsumer<? super K, ? super V> action);

	/**
	 * See {@link Map#keySet()}
	 *
	 * @return a set view of the keys contained in this map
	 */
	Set<Integer> idSet();

	/**
	 * See {@link Map#keySet()}
	 *
	 * @return a set view of the keys contained in this map
	 */
	Set<String> keySet();

	/**
	 * See {@link Map#values()}
	 *
	 * @return a collection view of the values contained in this map
	 */
	Collection<Object> values();

	/**
	 * See {@link Map#clear()}
	 */
	void clear();

	interface Builder extends AbstractBuilder<RawArgumentsMap>, org.spongepowered.api.util.Builder<RawArgumentsMap, Builder> {

		RawArgumentsMap create(RawCommand command, CommandCause cause, String[] args);

	}

}
