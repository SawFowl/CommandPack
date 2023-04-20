package sawfowl.commandpack.apiclasses;

import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.inventory.ItemStack;

import net.minecraft.world.item.enchantment.EnchantmentInstance;
import sawfowl.commandpack.api.EnchantmentHelper;

public class EnchantmentVanillaHelper implements EnchantmentHelper {

	public EnchantmentVanillaHelper() {
	}

	@Override
	public List<Enchantment> buildEnchantmentList(ItemStack itemStack, int level, boolean allowTreasure) {
		List<EnchantmentInstance> enchantmentInstances = net.minecraft.world.item.enchantment.EnchantmentHelper.getAvailableEnchantmentResults(level, (net.minecraft.world.item.ItemStack) (Object) itemStack, allowTreasure);
		return enchantmentInstances.stream().map(i -> ((Enchantment) i)).collect(Collectors.toList());
	}

}
