package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.InventorySee;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementInventorySee implements InventorySee {

	public ImplementInventorySee() {}

	@Setting("Title")
	private Component title = TextUtils.deserializeLegacy("&2&lInventory&f&l: &e&l" + Placeholders.PLAYER);

	@Override
	public Component getTitle(ServerPlayer player) {
		return Text.of(title).replace(Placeholders.PLAYER, player.name()).get();
	}

	@Override
	public Component getTitle(User player) {
		return Text.of(title).replace(Placeholders.PLAYER, player.name()).get();
	}

}
