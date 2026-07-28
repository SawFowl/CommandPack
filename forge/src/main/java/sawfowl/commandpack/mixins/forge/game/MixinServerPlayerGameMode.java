package sawfowl.commandpack.mixins.forge.game;

import net.minecraft.world.level.block.state.BlockState;

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
			at = @At(
					value = "STORE",
					ordinal = 0
			),
			remap = false
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
			at = @At(
					value = "STORE",
					ordinal = 1
			),
			remap = false
	)
	private float commandpack$handleBlockBreakAction(float f, BlockPos pos) {
		System.out.println(f + " " + pos.toString());
		BlockState blockState = level.getBlockState(pos);
		if(!blockState.isAir() && Sponge.eventManager().post(
				BlockDestroyEventImpl.createEvent(
					(ServerWorld) level,
					(org.spongepowered.api.entity.living.player.server.ServerPlayer) player,
					(org.spongepowered.api.block.BlockState) blockState,
					Vector3i.from(pos.getX(), pos.getY(), pos.getZ()),
					player.getDestroySpeed(blockState, pos),
					(float) lastSentState / 10.0f,
					f
				)
			)
		) {
			hasDelayedDestroy = true;
			delayedTickStart = gameTicks - 250;
			lastSentState = -1;
			if(f >= 0.9f) {
				delayedDestroyPos = new BlockPos(0, player.level().getMaxY() + 1, 0);
				destroyPos = delayedDestroyPos;
			}
			return 0.0f;
		}
		blockState = null;
		return f;
	}

}
