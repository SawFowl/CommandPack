package sawfowl.commandpack.api.data.player;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.api.data.miscellaneous.Location;

@ConfigSerializable
public interface Home {

	Location getLocation();

	boolean isDefault();

	Component asComponent();

	String getName();

}
