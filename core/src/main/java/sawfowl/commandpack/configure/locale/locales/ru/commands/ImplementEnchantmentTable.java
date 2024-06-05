package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.EnchantmentTable;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementEnchantmentTable implements EnchantmentTable {

	public ImplementEnchantmentTable() {}

	@Setting("SuccessStaff")
	private Component successStaff = TextUtils.deserializeLegacy("&aYou have opened the enchantment table inventory to a player &e" + Placeholders.PLAYER + "&a.");

	@Override
	public Component getSuccessStaff(ServerPlayer player) {
		return Text.of(successStaff).replace(Placeholders.PLAYER, player.name()).get();
	}

}
