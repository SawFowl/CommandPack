package sawfowl.commandpack.mixins.forge.plugin;

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

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.events.ModExplosionEvent;
import sawfowl.commandpack.listeners.ModPlatformEventListener;

@Mixin(value = ModPlatformEventListener.class, remap = false)
public class MixinForgeModPlatformEventListenerImpl {

	@Shadow CommandPackInstance plugin;

	@Overwrite
	void register() {
		MinecraftForge.EVENT_BUS.register(this);
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
			public List<Vector3i> getBlockPositionsAffected() {
				return blockPositionsAffected;
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
