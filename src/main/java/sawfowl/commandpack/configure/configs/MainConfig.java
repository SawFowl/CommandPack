package sawfowl.commandpack.configure.configs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.registry.RegistryTypes;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class MainConfig {

	public MainConfig() {}

	@Setting("JsonLocales")
	private boolean jsonLocales = false;
	@Setting("DebugEconomy")
	private boolean debugEconomy = true;
	@Setting("BlackListHatItems")
	private List<String> blackListHatItems = new ArrayList<>(Arrays.asList("minecraft:diamond_chestplate"));

	public boolean isJsonLocales() {
		return jsonLocales;
	}

	public boolean isDebugEconomy() {
		return debugEconomy;
	}

	public boolean isBlackListHat(ItemStack itemStack) {
		return blackListHatItems.contains(itemFullID(itemStack)) || blackListHatItems.contains(itemID(itemStack));
	}

	private static String itemFullID(ItemStack item) {
		return Sponge.game().registry(RegistryTypes.ITEM_TYPE).valueKey(item.type()).asString();
	}

	private static String itemID(ItemStack item) {
		return Sponge.game().registry(RegistryTypes.ITEM_TYPE).valueKey(item.type()).value();
	}

}
