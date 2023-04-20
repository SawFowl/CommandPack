package sawfowl.commandpack.apiclasses;

import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.inventory.ItemStack;

import net.minecraft.enchantment.EnchantmentData;

import sawfowl.commandpack.api.EnchantmentHelper;

public class EnchantmentForgeHelper implements EnchantmentHelper {

	public EnchantmentForgeHelper() {
	}

	public List<Enchantment> buildEnchantmentList(ItemStack itemStack, int level, boolean allowTreasure) {
		List<EnchantmentData> enchantmentDatas = net.minecraft.enchantment.EnchantmentHelper.getAvailableEnchantmentResults(level, (net.minecraft.item.ItemStack) (Object) itemStack, allowTreasure);
		return enchantmentDatas.stream().map(d -> ((Enchantment) d)).collect(Collectors.toList());
	}

	/**
	 * net.minecraft.enchantment.EnchantmentHelper - Forge helper class
	 * net.minecraft.world.item.enchantment.EnchantmentHelper - Vanilla helper class
	 */
}
