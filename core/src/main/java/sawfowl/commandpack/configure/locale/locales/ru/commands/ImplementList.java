package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.List;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementList implements List {

	public ImplementList() {}

	@Setting("Success")
	private Component success = TextUtils.deserializeLegacy("&aOnline(" + Placeholders.VALUE + ")&f: &e");
	@Setting("Vanished")
	private Component vanished = TextUtils.deserializeLegacy("&7" + Placeholders.PLAYER);

	@Override
	public Component getSuccess(int online) {
		return Text.of(success).replace(Placeholders.VALUE, online).get();
	}

	@Override
	public Component getVanished(ServerPlayer player) {
		return Text.of(vanished).replace(Placeholders.PLAYER, player.name()).get();
	}

}
