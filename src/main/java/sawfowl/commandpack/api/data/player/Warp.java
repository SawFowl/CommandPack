package sawfowl.commandpack.api.data.player;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.api.data.miscellaneous.Location;
import sawfowl.commandpack.configure.configs.player.WarpData;

@ConfigSerializable
public interface Warp {

	/**
	 * Creating a new warp point.
	 */
	static Warp create(String name, Location location) {
		return new WarpData(name, location);
	}

	/**
	 * Creating a new warp point.
	 */
	static Warp create(String name, Location location, boolean privated) {
		return new WarpData(name, location).setPrivate(privated);
	}

	/**
	 * Warp name converted to {@link Component}
	 */
	Component asComponent();

	/**
	 * Warp name
	 */
	String getName();

	/**
	 * Set new name of warp point.
	 */
	void setName(String name);


	/**
	 * Warp {@link Location}.
	 */
	Location getLocation();

	/**
	 * Set new location of warp point.
	 */
	void setLocation(Location location);

	/**
	 * Whether the warp is personal for the player.
	 */
	boolean isPrivate();

	/**
	 * Changing Warp Status. If true, the warp will be private. If false, the warp will be public.
	 */
	Warp setPrivate(boolean value);

	/**
	 * Teleport an entity to warp.
	 */
	boolean moveToThis(Entity entity);

}
