package sawfowl.commandpack.api;

import java.util.List;

import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.inventory.ItemStack;

public interface EnchantmentHelper {

	List<Enchantment> buildEnchantmentList(ItemStack itemStack, int level, boolean allowTreasure);

}
