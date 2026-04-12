package sawfowl.commandpack.mixins.forge.game;

import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.math.vector.Vector3i;

import sawfowl.commandpack.apiclasses.BlockDestroyEventImpl;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = ServerPlayerGameMode.class, remap = false)
public class MixinServerPlayerGameMode {

	@Shadow protected ServerLevel level;
	@Shadow @Final protected ServerPlayer player;
	@Shadow private int lastSentState;
	@Shadow private int gameTicks;
	@Shadow private boolean hasDelayedDestroy;
	@Shadow private int delayedTickStart;
	@Shadow private BlockPos delayedDestroyPos;
	@Shadow private BlockPos destroyPos;

	@ModifyVariable(
		method = "incrementDestroyProgress",
		slice = @Slice(
			from = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/world/level/block/state/BlockState;getDestroyProgress(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"
			)
		),
		at = @At(
			value = "STORE",
			ordinal = 0
		),
		name = "f"
	)
	private float commandpack$modifyDestroyProgress(float f, BlockState blockState, BlockPos delayedDestroyPos, int destroyStartTick) {
		if(Sponge.eventManager().post(
				BlockDestroyEventImpl.createEvent(
					(ServerWorld) level,
					(org.spongepowered.api.entity.living.player.server.ServerPlayer) player,
					(org.spongepowered.api.block.BlockState) blockState,
					Vector3i.from(delayedDestroyPos.getX(), delayedDestroyPos.getY(), delayedDestroyPos.getZ()),
					player.getDestroySpeed(blockState, delayedDestroyPos),
					(float) lastSentState / 10.0f, f
				)
			)
		) {
			hasDelayedDestroy = true;
			delayedTickStart = gameTicks - 250;
			lastSentState = -1;
			if(f >= 0.9f) {
				this.delayedDestroyPos = new BlockPos(0, player.level().getMaxY() + 1, 0);
				destroyPos = this.delayedDestroyPos;
			}
			return 0.0f;
		}
		return f;
	}

	@ModifyVariable(
		method = "handleBlockBreakAction",
		slice = @Slice(
			from = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/world/level/block/state/BlockState;getDestroyProgress(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F")
		),
		at = @At(
			value = "STORE",
			ordinal = 0
		),
		name = "f"
	)
	private float commandpack$handleBlockBreakAction(float value, BlockPos $$0) {
		BlockState blockState = level.getBlockState($$0);
		if(!blockState.isAir() && Sponge.eventManager().post(
				BlockDestroyEventImpl.createEvent(
					(ServerWorld) level,
					(org.spongepowered.api.entity.living.player.server.ServerPlayer) player,
					(org.spongepowered.api.block.BlockState) blockState,
					Vector3i.from($$0.getX(), $$0.getY(), $$0.getZ()),
					player.getDestroySpeed(blockState, $$0),
					(float) lastSentState / 10.0f,
					value
				)
			)
		) {
			hasDelayedDestroy = true;
			delayedTickStart = gameTicks - 250;
			lastSentState = -1;
			if(value >= 0.9f) {
				delayedDestroyPos = new BlockPos(0, player.level().getMaxY() + 1, 0);
				destroyPos = delayedDestroyPos;
			}
			return 0.0f;
		}
		blockState = null;
		return value;
	}

}
