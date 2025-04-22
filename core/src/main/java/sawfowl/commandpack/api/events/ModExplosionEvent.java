package sawfowl.commandpack.api.events;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.world.explosion.Explosion;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3d;
import org.spongepowered.math.vector.Vector3i;

public interface ModExplosionEvent extends Event, Cancellable {

	/**
	 * The object returned by this method may be `null` if its type cannot be cast to the type used in SpongeAPI.
	 */
	@Nullable Explosion getExplosion();

	ServerWorld getWorld();

	Vector3d getCenter();

	List<Vector3i> getBlockPositionsAffected();

	@Deprecated
	void removeBlock(Vector3i vector3i);

	@Deprecated
	void removeBlocks(Collection<Vector3i> vectors3i);

	void removeBlockIf(Predicate<Vector3i> filter);

	List<Entity> getEntitiesAffected();

	void removeEntityIf(Predicate<Entity> filter);

	@Nullable Entity getDirectSourceEntity();

	@Nullable Living getIndirectSourceEntity();

	float getRadius();

}
