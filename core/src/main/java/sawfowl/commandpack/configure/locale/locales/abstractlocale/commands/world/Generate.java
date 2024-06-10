package sawfowl.commandpack.configure.locale.locales.abstractlocale.commands.world;

import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3i;

import net.kyori.adventure.text.Component;

public interface Generate {

	String getDebug(ServerWorld world, double value, Vector3i lastChunk);

	Component getNotStarted(ServerWorld world);

	Component getNotPaused();

	Component getStart(ServerWorld world);

	Component getPause(ServerWorld world);

	Component getStop(ServerWorld world);

}
