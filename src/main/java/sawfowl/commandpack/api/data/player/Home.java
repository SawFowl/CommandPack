package sawfowl.commandpack.api.data.player;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.api.data.miscellaneous.Location;
import sawfowl.commandpack.configure.configs.player.HomeData;

/**
 * Interface for managing the data of the home point.
 */
@ConfigSerializable
public interface Home {

	/**
	 * Creating a new home point.
	 */
	static Home create(String name, Location location, boolean def) {
		return new HomeData(name, location, def);
	}

	/**
	 * Creating a new home point.
	 */
	static Home create(String name, Location location) {
		return new HomeData(name, location, false);
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
	 * Set new name of home point.
	 */
	void setName(String name);

	/**
	 * Home {@link Location}.
	 */
	Location getLocation();

	/**
	 * Set new location of home point.
	 */
	void setLocation(Location location);

	/**
	 * If true, then this home point is the default home point.
	 */
	boolean isDefault();

}
