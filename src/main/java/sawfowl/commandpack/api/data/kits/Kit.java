package sawfowl.commandpack.api.data.kits;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;
import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;

/**
 * @author SawFowl
 */
public interface Kit extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	String id();

	Component getLocalizedName(Locale locale);

	List<Component> getLocalizedLore(Locale locale);

	List<ItemStack> getContent();

	GiveRule getGiveRule();

	long getCooldown();

	int getGiveLimit();

	boolean isFirstTime();

	boolean isGiveOnJoin();

	boolean isNeedPerm();

	String permission();

	Optional<List<String>> getExecuteCommands();

	Optional<KitPrice> getKitPrice();

	default boolean isUnlimited() {
		return getGiveLimit() < 0;
	}

	/**
	 * Getting the contents of the kit in the menu with the specified number of lines.<br>
	 * All changes made will be automatically saved.
	 */
	InventoryMenu asMenu(PluginContainer container, ServerPlayer carrier, boolean readOnly);

	void save();

	interface Builder extends AbstractBuilder<Kit>, org.spongepowered.api.util.Builder<Kit, Builder> {

		Builder fromInventory(Inventory inventory);

		Builder fromList(List<ItemStack> list);

		Builder copyFrom(Kit kit);

		Builder cooldown(long cooldown);

		Builder giveLimit(int giveLimit);

		Builder firstTime(boolean firstTime);

		Builder id(String id);

		Builder name(Locale locale, Component name);

		Builder lore(Locale locale, List<Component> lore);

		Builder needPerm(boolean needPerm);

		Builder executeCommands(List<String> commands);

		Builder kitPrice(KitPrice price);

	}

}
