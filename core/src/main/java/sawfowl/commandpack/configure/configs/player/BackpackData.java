package sawfowl.commandpack.configure.configs.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.ContainerType;
import org.spongepowered.api.item.inventory.ContainerTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;
import org.spongepowered.api.item.inventory.menu.handler.CloseHandler;
import org.spongepowered.api.item.inventory.type.ViewableInventory;
import org.spongepowered.api.registry.DefaultedRegistryReference;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.api.data.player.Backpack;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class BackpackData implements Backpack {

	public BackpackData(){}

	@Setting("Items")
	private Map<Integer, ItemStack> items = new HashMap<>();
	private Consumer<BackpackData> save;

	void setSaveConsumer(Consumer<BackpackData> save) {
		this.save = save;
	}

	boolean canSave() {
		return save != null;
	}

	public Builder builder() {
		return new Builder();
	}

	@Override
	public int contentVersion() {
		return 1;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
			.set(DataQuery.of("Items"), items)
			.set(Queries.CONTENT_VERSION, contentVersion());
	}

	@Override
	public boolean addItem(int slot, ItemStack item) {
		if(slot < 0 || slot > 53) return false;
		if(items.containsKey(slot)) items.remove(slot);
		items.put(slot, item);
		return true;
	}

	@Override
	public boolean addItem(int slot, ItemStackSnapshot item) {
		return addItem(slot, item.createStack());
	}

	@Override
	public void removeItem(int slot) {
		if(items.containsKey(slot)) items.remove(slot);
	}

	@Override
	public int size() {
		return items.size();
	}

	@Override
	public Collection<Integer> getSlots() {
		return items.keySet();
	}

	@Override
	public Collection<ItemStack> getItems() {
		return new ArrayList<ItemStack>(items.values());
	}

	@Override
	public Optional<ItemStack> getItem(int slot) {
		return Optional.ofNullable(items.getOrDefault(slot, null)).filter(s -> (s != null));
	}

	@Override
	public InventoryMenu asMenu(PluginContainer container, ServerPlayer player, int rows, Component title) {
		InventoryMenu menu = ViewableInventory.builder().type(getType(rows)).completeStructure().carrier(player).plugin(container).build().asMenu();
		if(title == null) {
			menu.setTitle(TextUtils.deserializeLegacy("&8Backpack"));
		} else menu.setTitle(title);
		menu.registerClose(new CloseHandler() {
			@Override
			public void handle(Cause cause, Container container) {
				menu.inventory().slots().stream().forEach(slot -> {
					slot.get(Keys.SLOT_INDEX).ifPresent(slotIndex -> {
						if(items.containsKey(slotIndex)) items.remove(slotIndex);
						if(slot.peek().quantity() > 0) addItem(slotIndex, slot.peek());
					});
				});
				save();
				menu.unregisterAll();
				menu.inventory().clear();
			}
		});
		items.forEach((k, v) -> {
			if(menu.inventory().slot(k).isPresent()) menu.inventory().offer(k, v);
		});
		return menu;
	}

	@Override
	public void save() {
		if(save != null) save.accept(this);
	}

	private DefaultedRegistryReference<ContainerType> getType(int rows) {
		switch (rows) {
		case 2:
			return ContainerTypes.GENERIC_9X2;
		case 3:
			return ContainerTypes.GENERIC_9X3;
		case 4:
			return ContainerTypes.GENERIC_9X4;
		case 5:
			return ContainerTypes.GENERIC_9X5;
		case 6:
			return ContainerTypes.GENERIC_9X6;
		default:
			return ContainerTypes.GENERIC_9X1;
		}
	}

	public class Builder implements Backpack.Builder {

		@Override
		public Backpack build() {
			return BackpackData.this;
		}

		@Override
		public Builder fromInventory(Inventory inventory) {
			inventory.slots().forEach(slot -> {
				if(slot.totalQuantity() > 0 && slot.get(Keys.SLOT_INDEX).isPresent()) addItem(slot.get(Keys.SLOT_INDEX).get(), slot.peek());
			});
			return this;
		}

		@Override
		public Builder fromMap(Map<Integer, ItemStack> map) {
			map.forEach((k,v) -> {
				if(v.quantity() > 0) addItem(k, v);
			});
			return this;
		}

		@Override
		public Builder copyFrom(Backpack backpack) {
			if(backpack.getSlots() != null && !backpack.getSlots().isEmpty()) backpack.getSlots().forEach(slot -> {
				backpack.getItem(slot).ifPresent(item -> {
					addItem(slot, item);
				});
			});
			return this;
		}
		
	}

}
