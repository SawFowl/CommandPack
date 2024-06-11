package sawfowl.commandpack.configure.locale.locales.ru.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;
import sawfowl.commandpack.configure.Placeholders;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.Spawn;
import sawfowl.localeapi.api.Text;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class ImplementSpawn implements Spawn {

	public ImplementSpawn(){}

	@Setting("Set")
	private Component set = TextUtils.deserializeLegacy("&aТочка спавна установлена.");
	@Setting("teleport")
	private Component teleport = TextUtils.deserializeLegacy("&aВы телепортировались на спавн.");
	@Setting("TeleportStaff")
	private Component teleportStaff = TextUtils.deserializeLegacy("&aВы телепортировали &e" + Placeholders.PLAYER + "&a на спавн.");

	@Override
	public Component getSet() {
		return set;
	}

	@Override
	public Component getTeleport() {
		return teleport;
	}

	@Override
	public Component getTeleportStaff(ServerPlayer player) {
		return Text.of(teleportStaff).replace(Placeholders.PLAYER, player.name()).get();
	}

}
