package sawfowl.commandpack.mixins.forge.game;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import net.minecraftforge.event.level.ExplosionEvent;

import org.jetbrains.annotations.Nullable;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.math.vector.Vector3d;
import org.spongepowered.math.vector.Vector3i;

import sawfowl.commandpack.api.events.ModExplosionEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Mixin(value = ExplosionEvent.Detonate.class, remap = false)
public class MixinExplosionDetonateEvent {

	@Shadow @Final private List<BlockPos> blocks;
	@Shadow @Final private List<Entity> entityList;

	private List<org.spongepowered.api.entity.Entity> entities;
	private List<Vector3i> vectors;
	boolean cancelled = false;

	@SuppressWarnings("unchecked")
	@Inject(method = "<init>", at = @At("RETURN"))
	private void commandPack$InitDetonate(Level level, Explosion explosion, List<BlockPos> blocks, List<Entity> entityList, CallbackInfo callback) {
		vectors = blocks.stream().map(pos -> Vector3i.from(pos.getX(), pos.getY(), pos.getZ())).collect(Collectors.toList());
		entities = new ArrayList<org.spongepowered.api.entity.Entity>((Collection<? extends org.spongepowered.api.entity.Entity>) entityList);
		try {
			if(Sponge.eventManager().post(createSpongeEvent(level, explosion))) {
				vectors.clear();
				entities.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Inject(method = "getAffectedBlocks", at = @At("HEAD"))
	public void commandpack$getAffectedBlocks(CallbackInfoReturnable<List<BlockPos>> cir) {
		if(vectors.size() != blocks.size()) cir.setReturnValue(vectors.isEmpty() ? new ArrayList<>() : vectors.stream().map(v -> new BlockPos(v.x(), v.y(), v.z())).collect(Collectors.toList()));
	}

	@SuppressWarnings("unchecked")
	@Inject(method = "getAffectedEntities", at = @At("HEAD"))
	public void commandpack$getAffectedEntities(CallbackInfoReturnable<List<Entity>> cir) {
		if(entities.size() != entityList.size()) cir.setReturnValue((List<Entity>) (List<? extends Entity>) entities);
	}

	private ModExplosionEvent createSpongeEvent(Level level, Explosion explosion) {
		return new ModExplosionEvent() {

			private Cause cause = createCause(getIndirectSourceEntity(), getDirectSourceEntity());
			Vector3d center = Vector3d.from(explosion.center().x, explosion.center().y, explosion.center().z);

			@Override
			public @Nullable org.spongepowered.api.world.explosion.Explosion getExplosion() {
				return explosion instanceof org.spongepowered.api.world.explosion.Explosion e ? e : null;
			}

			@Override
			public ServerWorld getWorld() {
				return (ServerWorld) level;
			}

			@Override
			public Vector3d getCenter() {
				return center;
			}

			@Override
			public List<Vector3i> getBlockPositionsAffected() {
				return vectors;
			}

			@Override
			public void removeBlock(Vector3i vector3i) {
				vectors.remove(vector3i);
			}

			@Override
			public void removeBlocks(Collection<Vector3i> vectors3i) {
				vectors.removeAll(vectors3i);
			}

			@Override
			public void removeBlockIf(Predicate<Vector3i> filter) {
				vectors.removeIf(filter);
			}

			@Override
			public List<org.spongepowered.api.entity.Entity> getEntitiesAffected() {
				return entities;
			}

			@Override
			public void removeEntityIf(Predicate<org.spongepowered.api.entity.Entity> filter) {
				entities.removeIf(filter);
			}

			@Override
			public @Nullable org.spongepowered.api.entity.Entity getDirectSourceEntity() {
				return (org.spongepowered.api.entity.Entity) explosion.getDirectSourceEntity();
			}

			@Override
			public @Nullable Living getIndirectSourceEntity() {
				return (Living) explosion.getIndirectSourceEntity();
			}

			@Override
			public float getRadius() {
				return explosion.radius();
			}

			@Override
			public boolean isCancelled() {
				return cancelled;
			}

			@Override
			public void setCancelled(boolean cancel) {
				cancelled = cancel;
			}

			@Override
			public Cause cause() {
				return cause;
			}

		};
	}

	private Cause createCause(Living living, org.spongepowered.api.entity.Entity entity) {
		if(living != null && entity != null) return Cause.builder().build(EventContext.builder().add(EventContextKeys.IGNITER, living).add(EventContextKeys.CREATOR, entity.uniqueId()).build());
		if(living != null) return Cause.builder().build(EventContext.builder().add(EventContextKeys.IGNITER, living).build());
		if(entity != null) return Cause.builder().build(EventContext.builder().add(EventContextKeys.CREATOR, entity.uniqueId()).build());
		return Cause.builder().build(EventContext.empty());
	}

}
