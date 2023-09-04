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

	/**
	 * kit id
	 */
	String id();

	/**
	 * Localized kit name
	 */
	Component getLocalizedName(Locale locale);

	/**
	 * Localized kit lore
	 */
	List<Component> getLocalizedLore(Locale locale);

	/**
	 * Getting a copy of the items in the kit.
	 */
	List<ItemStack> getContent();

	/**
	 * The rule for giving a kit to a player.
	 */
	GiveRule getGiveRule();

	/**
	 * Time until access to the kit is restored.
	 */
	long getCooldown();

	/**
	 * How many times a given set can be given to a player.
	 */
	int getGiveLimit();

	/**
	 * Should the kit be given to the player upon first entry.
	 */
	boolean isFirstTime();

	/**
	 * Should the kit be given to the player upon entering the game.<br>
	 * The time of restoration of access to the kit is taken into account.
	 */
	boolean isGiveOnJoin();

	/**
	 * Whether to check if the player has permission to get a set.
	 */
	boolean isNeedPerm();

	/**
	 * Permission to getting a kit.
	 */
	String permission();

	/**
	 * A list of commands to be executed when a kit is giving.
	 */
	Optional<List<String>> getExecuteCommands();

	/**
	 * Adding a command to the kit.
	 */
	void addCommand(String command);

	/**
	 * Removing a command from the kit.
	 */
	void removeCommand(String command);

	/**
	 * Obtaining kit price data.
	 */
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
