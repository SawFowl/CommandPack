package sawfowl.commandpack.configure.locale.locales.abstractclasses.commands;

import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.text.Component;

@ConfigSerializable
public interface RandomTeleport {

	Component getPositionSearchErrorStaff(ServerWorld world, int limit);

	Component getPositionSearchError();

	Component getSuccessStaff(ServerPlayer player, ServerLocation location);

	Component getSuccess(ServerLocation location);

	Component getWait();

	Component getCancelled();

}
