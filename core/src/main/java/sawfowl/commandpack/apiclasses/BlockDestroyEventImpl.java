package sawfowl.commandpack.apiclasses;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.math.vector.Vector3i;

import sawfowl.commandpack.api.events.BlockDestroyProgressEvent;

public class BlockDestroyEventImpl {

	public static BlockDestroyProgressEvent createEvent(ServerWorld world, ServerPlayer player, BlockState blockState, Vector3i blockPos, float speed, float previousProgress, float progress) {
		return new BlockDestroyProgressEvent() {

			private Cause cause = Cause.builder().append(player).build(EventContext.builder().add(EventContextKeys.PLAYER, player).add(EventContextKeys.NOTIFIER, player.uniqueId()).add(EventContextKeys.AUDIENCE, player).add(EventContextKeys.SUBJECT, player).build());
			private float result = progress;
			private boolean cancel;

			@Override
			public Cause cause() {
				return cause;
			}

			@Override
			public ServerWorld getWorld() {
				return world;
			}

			@Override
			public float getSpeed() {
				return speed;
			}

			@Override
			public float getPreviousProgress() {
				return previousProgress;
			}

			@Override
			public ServerPlayer getPlayer() {
				return player;
			}

			@Override
			public Vector3i getBlockPosition() {
				return blockPos;
			}

			@Override
			public BlockState getBlock() {
				return blockState;
			}

			@Override
			public float getProgress() {
				return result;
			}

			@Override
			public boolean isCancelled() {
				return cancel;
			}

			@Override
			public void setCancelled(boolean cancel) {
				this.cancel = cancel;
			}

		};
		
	}

}
