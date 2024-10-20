package sawfowl.commandpack.mixins.neoforge.plugin;

import java.util.Collection;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.world.explosion.Explosion;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.math.vector.Vector3i;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.ExplosionEvent;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.events.ModExplosionEvent;
import sawfowl.commandpack.listeners.ModPlatformEventListener;

@Mixin(value = ModPlatformEventListener.class, remap = false)
public class MixinModPlatformEventListenerImpl {

	@Shadow CommandPackInstance plugin;

	@Overwrite
	void register() {
		NeoForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onExplosion(ExplosionEvent.Start event) {
		if(event.isCanceled()) return;
		Sponge.eventManager().post(new ModExplosionEvent() {

			private List<Vector3i> blockPositionsAffected = event.getExplosion().getToBlow().stream().map(p -> Vector3i.from(p.getX(), p.getY(), p.getZ())).toList();
			private Cause cause = createCause(getIndirectSourceEntity(), getDirectSourceEntity());

			@Override
			public void setCancelled(boolean cancel) {
				event.setCanceled(cancel);
			}

			@Override
			public boolean isCancelled() {
				return event.isCanceled();
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
			public Collection<Vector3i> getBlockPositionsAffected() {
				return blockPositionsAffected;
			}

			@Override
			public void removeBlock(Vector3i vector3i) {
				event.getExplosion().getToBlow().removeIf(position -> position.getX() == vector3i.x() && position.getY() == vector3i.y() && position.getZ() == vector3i.z());
			}

			@Override
			public void removeBlocks(Collection<Vector3i> vectors3i) {
				vectors3i.forEach(vector3i -> removeBlock(vector3i));
			}

		});
	}

	private Cause createCause(Living living, Entity entity) {
		if(living != null && entity != null) return Cause.builder().build(EventContext.builder().add(EventContextKeys.IGNITER, living).add(EventContextKeys.CREATOR, entity.uniqueId()).build());
		if(living != null) return Cause.builder().build(EventContext.builder().add(EventContextKeys.IGNITER, living).build());
		if(entity != null) return Cause.builder().build(EventContext.builder().add(EventContextKeys.CREATOR, entity.uniqueId()).build());
		return Cause.builder().build(EventContext.empty());
	}

}
