package sawfowl.commandpack.api.data.player;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;
import sawfowl.commandpack.api.data.miscellaneous.Location;

/**
 * Interface for managing the data of the home point.
 */
@ConfigSerializable
public interface Home extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	/**
	 * Creating a new home point.
	 */
	static Home of(String name, Location location, boolean def) {
		return builder().setName(name).setLocation(location).setDefault(def).build();
	}

	/**
	 * Creating a new home point.
	 */
	static Home of(String name, Location location) {
		return builder().setName(name).setLocation(location).setDefault(false).build();
	}

	/**
	 * Home name converted to {@link Component}.
	 */
	Component asComponent();

	/**
	 * Home name.
	 */
	String getName();

	/**
	 * Home {@link Location}.
	 */
	Location getLocation();

	/**
	 * If true, then this home point is the default home point.
	 */
	boolean isDefault();

	interface Builder extends AbstractBuilder<Home>, org.spongepowered.api.util.Builder<Home, Builder> {

		/**
		 * Set new name of home point.
		 * @return TODO
		 */
		Builder setName(String name);

		/**
		 * Set new location of home point.
		 * @return TODO
		 */
		Builder setLocation(Location location);

		/**
		 * Assign this home point as the default home point.<br>
		 * If a player will have several points marked as the default point, the first matching this condition will be chosen when receiving the default point.
		 */
		Builder setDefault(boolean def);
		
	}

}
