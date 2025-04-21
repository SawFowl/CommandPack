package sawfowl.commandpack.mixins.neoforge.game;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerExplosion;

import net.neoforged.neoforge.event.level.ExplosionEvent;

import org.jetbrains.annotations.Nullable;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.world.explosion.Explosion;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.math.vector.Vector3i;

import sawfowl.commandpack.api.events.ModExplosionEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(ExplosionEvent.Detonate.class)
public class MixinExplosionDetonateEvent {

	@Shadow @Final private List<BlockPos> blockList;

	private List<Vector3i> vectors;
	boolean cancelled = false;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void commandPack$InitDetonate(Level level, ServerExplosion explosion, List<Entity> entityList, List<BlockPos> blockList, CallbackInfo callback) {
		vectors = blockList.stream().map(pos -> Vector3i.from(pos.getX(), pos.getY(), pos.getZ())).collect(Collectors.toList());
		if(Sponge.eventManager().post(createSpongeEvent(level, explosion))) vectors.clear();
	}

	@Inject(method = "getAffectedBlocks", at = @At("HEAD"))
	public void commandpack$getAffectedBlocks(CallbackInfoReturnable<List<BlockPos>> cir) {
		if(vectors.size() != blockList.size()) cir.setReturnValue(vectors.isEmpty() ? new ArrayList<>() : vectors.stream().map(v -> new BlockPos(v.x(), v.y(), v.z())).collect(Collectors.toList()));
	}

	@Inject(method = "getAffectedEntities", at = @At("HEAD"))
	public void commandpack$getAffectedEntities(CallbackInfoReturnable<List<Entity>> cir) {
		if(cancelled) cir.setReturnValue(new ArrayList<>());
	}

	private ModExplosionEvent createSpongeEvent(Level level, ServerExplosion explosion) {
		return new ModExplosionEvent() {
			private Cause cause = createCause(getIndirectSourceEntity(), getDirectSourceEntity());

			@Override
			public Explosion getExplosion() {
				return explosion instanceof Explosion e ? e : null;
			}

			@Override
			public ServerWorld getWorld() {
				return (ServerWorld) level;
			}

			@Override
			public Collection<Vector3i> getBlockPositionsAffected() {
				return vectors;
			}

			@Override
			public void removeBlock(Vector3i vector3i) {
				getBlockPositionsAffected().remove(vector3i);
			}

			@Override
			public void removeBlocks(Collection<Vector3i> vectors3i) {
				getBlockPositionsAffected().removeAll(vectors3i);
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
