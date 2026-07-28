package sawfowl.commandpack.mixins.forge.plugin;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.world.explosion.Explosion;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.math.vector.Vector3d;
import org.spongepowered.math.vector.Vector3i;

import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.listener.Priority;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.events.ModExplosionEvent;
import sawfowl.commandpack.listeners.ModPlatformEventListener;

@Mixin(value = ModPlatformEventListener.class, remap = false)
public class MixinModPlatformEventListenerImpl {

	@Shadow @Final CommandPackInstance plugin;

	/**
	 * @author SawFowl
	 * @reason This is an internal method of the plugin.
	 */
	@Overwrite
	void register() {
		ExplosionEvent.Detonate.BUS.addListener(this::commandPack$onExplosion);
	}

	@Unique
	@SubscribeEvent(priority = Priority.HIGHEST)
	public void commandPack$onExplosion(ExplosionEvent.Detonate event) {
		if(event.getAffectedBlocks().isEmpty() && event.getAffectedEntities().isEmpty()) return;
		Sponge.eventManager().post(new ModExplosionEvent() {
			private final Cause cause = commandPack$createCause(getIndirectSourceEntity(), getDirectSourceEntity());

			@Override
			public void setCancelled(boolean cancel) {
				event.getAffectedBlocks().clear();
				event.getAffectedEntities().clear();
			}

			@Override
			public boolean isCancelled() {
				return event.getAffectedBlocks().isEmpty() && event.getAffectedEntities().isEmpty();
			}

			@Override
			public Cause cause() {
				return cause;
			}

			@Override
			public ServerWorld getWorld() {
				return getExplosion().serverLocation().world();
			}

			@Override
			public Living getIndirectSourceEntity() {
				return (Living) event.getExplosion().getIndirectSourceEntity();
			}

			@Override
			public Explosion getExplosion() {
				return (Explosion) event.getExplosion();
			}

			@Override
			public Entity getDirectSourceEntity() {
				return (Entity) event.getExplosion().getDirectSourceEntity();
			}

			@Override
			public List<Vector3i> getBlockPositionsAffected() {
				return event.getAffectedBlocks().stream().map(p -> Vector3i.from(p.getX(), p.getY(), p.getZ())).toList();
			}

			@Override
			public void removeBlock(Vector3i vector3i) {
				event.getAffectedBlocks().removeIf(position -> position.getX() == vector3i.x() && position.getY() == vector3i.y() && position.getZ() == vector3i.z());
			}

			@Override
			public void removeBlocks(Collection<Vector3i> vectors3i) {
				vectors3i.forEach(vector3i -> removeBlock(vector3i));
			}

			@Override
			public Vector3d getCenter() {
				return Vector3d.from(event.getExplosion().center().x(), event.getExplosion().center().y(), event.getExplosion().center().z());
			}

			@Override
			public void removeBlockIf(Predicate<Vector3i> filter) {
				event.getAffectedBlocks().removeIf(p -> filter.test(Vector3i.from(p.getX(), p.getY(), p.getZ())));
			}

			@SuppressWarnings("unchecked")
			@Override
			public List<Entity> getEntitiesAffected() {
				return (List<Entity>) (Object) event.getAffectedEntities();
			}

			@Override
			public void removeEntityIf(Predicate<Entity> filter) {
				event.getAffectedEntities().removeIf(e -> filter.test((Entity) e));
			}

			@Override
			public float getRadius() {
				return event.getExplosion().radius();
			}

		});
	}

	@Unique
	private Cause commandPack$createCause(Living living, Entity entity) {
		if(living != null && entity != null) return Cause.builder().append(living).append(entity).build(EventContext.builder().add(EventContextKeys.IGNITER, living).add(EventContextKeys.CREATOR, entity.uniqueId()).build());
		if(living != null) return Cause.builder().append(living).build(EventContext.builder().add(EventContextKeys.IGNITER, living).build());
		if(entity != null) return Cause.builder().append(entity).build(EventContext.builder().add(EventContextKeys.CREATOR, entity.uniqueId()).build());
		return Cause.builder().append(Sponge.systemSubject()).build(EventContext.empty());
	}

}