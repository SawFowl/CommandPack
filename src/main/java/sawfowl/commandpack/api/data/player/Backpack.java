package sawfowl.commandpack.api.data.player;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;
import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;

/**
 * @author SawFowl
 */
public interface Backpack extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	/**
	 * Add an item to the backpack.
	 */
	boolean addItem(int slot, ItemStack item);

	/**
	 * Add an item to the backpack.
	 */
	boolean addItem(int slot, ItemStackSnapshot item);

	/**
	 * Remove an item from the backpack.
	 */
	void removeItem(int slot);

	/**
	 * The number of slots in the backpack that have items in them.
	 */
	int size();

	/**
	 * Get the numbers of the slots that contain the items.
	 */
	Collection<Integer> getSlots();

	/**
	 * Get a copy of all the items in the backpack.
	 */
	Collection<ItemStackSnapshot> getItems();

	/**
	 * Getting an item from a backpack.
	 */
	Optional<ItemStack> getItem(int slot);

	/**
	 * Getting the contents of the backpack in the menu with the specified number of lines.<br>
	 * All changes made will be automatically saved if the backpack data refers to the player's data in the CommandPack plugin.<br>
	 * The menu is cleared after closing.<br>
	 * Clearing when closed does not affect the backpack data.
	 */
	InventoryMenu asMenu(PluginContainer container, ServerPlayer openForTarget, int rows, Component title);

	/**
	 *  Save Backpack data.
	 */
	void save();

	interface Builder extends AbstractBuilder<Backpack>, org.spongepowered.api.util.Builder<Backpack, Builder> {

		Builder fromInventory(Inventory inventory);

		Builder fromMap(Map<Integer, ItemStack> map);

		Builder copyFrom(Backpack backpack);

	}

}
