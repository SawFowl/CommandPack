package sawfowl.commandpack.api.events;

import java.util.List;

import javax.annotation.Nullable;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.world.explosion.Explosion;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3i;

public interface ModExplosionEvent extends Event, Cancellable {

	Explosion getExplosion();

	ServerWorld getWorld();

	List<Vector3i> getBlockPositionsAffected();

	@Nullable Entity getDirectSourceEntity();

	@Nullable Living getIndirectSourceEntity();

}
