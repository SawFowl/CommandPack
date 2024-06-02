package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.text.Component;

public interface Spawn {

	Component getSet();

	Component getTeleport();

	Component getTeleportStaff(ServerPlayer player);

}
