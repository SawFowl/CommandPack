package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.List;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementList implements List {

	public ImplementList() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aОнлайн(" + Placeholders.SIZE + ")&f: &e" + Placeholders.PLAYERS + "&a.");
	@Setting("Vanished")
	private Component vanished = TextUtils.deserializeLegacy("&7" + Placeholders.PLAYER);

	@Override
	public Component getSuccess(java.util.List<Component> list) {
		return Text.of(success).replace(Placeholders.SIZE, list.size()).replace(Placeholders.PLAYERS, Component.join(JoinConfiguration.separator(Component.text(", ")), list)).get();
	}

	@Override
	public Component getVanished(ServerPlayer player) {
		return Text.of(vanished).replace(Placeholders.PLAYER, player.name()).get();
	}

}
