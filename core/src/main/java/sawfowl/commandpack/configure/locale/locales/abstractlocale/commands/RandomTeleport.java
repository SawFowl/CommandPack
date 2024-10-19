package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;

import net.kyori.adventure.text.Component;

public interface RandomTeleport {

	Component getPositionSearchErrorStaff(ServerWorld world, int limit);

	Component getPositionSearchError();

	Component getSuccessStaff(ServerPlayer player, ServerLocation location);

	Component getSuccess(ServerLocation location);

	Component getWait();

	Component getCancelled();

}
