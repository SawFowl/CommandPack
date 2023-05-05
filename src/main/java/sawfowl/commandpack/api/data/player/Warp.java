package sawfowl.commandpack.api.data.player;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.entity.Entity;

import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;

import sawfowl.commandpack.api.data.miscellaneous.Location;

/**
 * @author SawFowl
 */
public interface Warp extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	/**
	 * Creating a new warp point.
	 */
	static Warp of(String name, Location location) {
		return builder().setName(name).setLocation(location).build();
	}

	/**
	 * Creating a new warp point.
	 */
	static Warp of(String name, Location location, boolean privated) {
		return builder().setName(name).setLocation(location).setPrivate(privated).build();
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
	 * Warp plain name
	 */
	String getPlainName();

	/**
	 * Warp {@link Location}.
	 */
	Location getLocation();

	/**
	 * Whether the warp is personal for the player.
	 */
	boolean isPrivate();

	/**
	 * Teleport an entity to warp.
	 */
	boolean moveHere(Entity entity);

	interface Builder extends AbstractBuilder<Warp>, org.spongepowered.api.util.Builder<Warp, Builder> {

		/**
		 * Set new name of warp point.
		 */
		Builder setName(String name);

		/**
		 * Set new location of warp point.
		 */
		Builder setLocation(Location location);

		/**
		 * Changing Warp Status. If true, the warp will be private. If false, the warp will be public.
		 */
		Builder setPrivate(boolean value);
		
	}

}
