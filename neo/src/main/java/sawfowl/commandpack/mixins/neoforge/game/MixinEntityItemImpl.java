package sawfowl.commandpack.mixins.neoforge.game;

import java.util.Optional;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import sawfowl.commandpack.api.mixin.game.EntityItem;

@Mixin(ItemEntity.class)
public abstract class MixinEntityItemImpl implements EntityItem {

	@Shadow public abstract ItemStack getItem();

	@Override
	public org.spongepowered.api.item.inventory.ItemStack getItemStack() {
		return (org.spongepowered.api.item.inventory.ItemStack) (Object) getItem();
	}

	@Override
	public Optional<Entity> getOwner() {
		return Optional.ofNullable((Entity) ((ItemEntity) (Object) this).getOwner());
	}

}
