package sawfowl.commandpack.api.mixin.game;

import java.util.Optional;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

/**
 * The interface is designed to retrieve the id of an item if it is an entity.
 * 
 * @author SawFowl
 */
public interface EntityItem extends Entity {

	static Optional<EntityItem> tryCast(Entity entity) {
		return Optional.ofNullable(entity instanceof EntityItem ? (EntityItem) entity : null);
	}

	ItemStack getItemStack();

	Optional<Entity> getOwner();

	default ResourceKey getId() {
		return ItemTypes.registry().findValueKey(getItemStack().type()).orElse(ResourceKey.minecraft("air"));
	}

}
