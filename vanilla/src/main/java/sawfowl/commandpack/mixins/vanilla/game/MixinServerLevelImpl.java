package sawfowl.commandpack.mixins.vanilla.game;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

import org.checkerframework.checker.nullness.qual.NonNull;

import org.jetbrains.annotations.Nullable;

import org.spongepowered.api.util.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundTickingStatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.level.portal.PortalForcer;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.math.vector.Vector3i;

import sawfowl.commandpack.api.mixin.game.MixinServerWorld;
import sawfowl.commandpack.api.mixin.game.PortalShape;
import sawfowl.commandpack.mixins.PortalShapeAccessor;

@Mixin(value = ServerLevel.class)
public abstract class MixinServerLevelImpl implements MixinServerWorld {

	@Shadow
	public abstract @NonNull MinecraftServer shadow$getServer();
	@Shadow
	public abstract PortalForcer getPortalForcer();
	abstract long[] bridge$recentTickTimes();
	private TickRateManager ticksManager = new TickRateManager();

	public boolean isFreezeTicks() {
		return ticksManager.isFrozen();
	}

	public void setFreezeTicks(boolean enable) {
		ticksManager.setFrozen(enable);
		updateStateToClients(ClientboundTickingStatePacket.from(ticksManager));
	}

	@Override
	public double getTickTime() {
		long[] tickTimes = bridge$recentTickTimes();
		long $$1 = 0L;
		for(long $$2 : tickTimes) $$1 += $$2;
		return ((double)$$1 / (double)tickTimes.length) * 1.0E-6D;
	}

	@Override
	public double getTPS() {
		return Math.min(1000.0 / getTickTime(), 20.0);
	}

	public TickRateManager getTicksManager() {
		return ticksManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<PortalShape> findPortalShape(boolean empty, int x, int y, int z, Direction direction, @Nullable Predicate<PortalShape> predicate) {
		if(predicate == null) predicate = shape -> true;
		return findPortalShape(empty, new BlockPos(x, y, z), direction == null ? net.minecraft.core.Direction.NORTH : convert(direction), (Predicate<net.minecraft.world.level.portal.PortalShape>) (Object) predicate).map(s -> ((PortalShapeAccessor) s).setWorld(this));
	}

	@Override
	public Optional<Vector3i> findClosestPortalPosition(Vector3i blockPos, boolean isNether) {
		return findClosestPortalPosition(new BlockPos(blockPos.x(), blockPos.y(), blockPos.z()), isNether).map(pos -> Vector3i.from(pos.getX(), pos.getY(), pos.getZ()));
	}

	private Optional<BlockPos> findClosestPortalPosition(BlockPos blockPos, boolean isNether) {
		return getPortalForcer().findClosestPortalPosition(blockPos, isNether, asVanilla().getWorldBorder());
	}

	private Optional<net.minecraft.world.level.portal.PortalShape> findPortalShape(boolean empty, BlockPos blockPos, net.minecraft.core.Direction direction, Predicate<net.minecraft.world.level.portal.PortalShape> predicate) {
		if(empty) return net.minecraft.world.level.portal.PortalShape.findEmptyPortalShape(asVanilla(), blockPos, direction.getAxis()).filter(predicate);
		return net.minecraft.world.level.portal.PortalShape.findPortalShape(asVanilla(), blockPos, predicate, direction.getAxis());
	}

	private ServerLevel asVanilla() {
		return (ServerLevel) (Object) this;
	}

	@SuppressWarnings("unchecked")
	private void updateStateToClients(ClientboundTickingStatePacket packet) {
		((Collection<ServerPlayer>) (Object) players()).forEach(player -> {
			player.connection.send(packet);
		});
	}

	@Inject(method = "addPlayer", at = @At("HEAD"))
	private void onAddPlayer(ServerPlayer $$0, CallbackInfo info) {
		$$0.connection.send(ClientboundTickingStatePacket.from(ticksManager));
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void onTick(BooleanSupplier $$0, CallbackInfo info) {
		if(isFreezeTicks()) {
			bridge$recentTickTimes()[this.shadow$getServer().getTickCount() % 100] = 0;
			info.cancel();
		}
	}

	private net.minecraft.core.Direction convert(Direction direction) {
		switch (direction) {
		case UP: {
			return net.minecraft.core.Direction.UP;
		}
		case DOWN: {
			return net.minecraft.core.Direction.DOWN;
		}
		case NORTH: {
			return net.minecraft.core.Direction.NORTH;
		}
		case EAST: {
			return net.minecraft.core.Direction.EAST;
		}
		case WEST: {
			return net.minecraft.core.Direction.WEST;
		}
		case SOUTH: {
			return net.minecraft.core.Direction.SOUTH;
		}
		default:
			return fromDelta(direction.asBlockOffset().x(), direction.asBlockOffset().y(), direction.asBlockOffset().z());
		}
	}

	private net.minecraft.core.Direction fromDelta(int $$0, int $$1, int $$2) {
		if ($$0 == 0) {
			if ($$1 == 0) {
				if ($$2 > 0) {
					return net.minecraft.core.Direction.SOUTH;
				}
				if ($$2 < 0) {
					return net.minecraft.core.Direction.NORTH;
				}
			} else if ($$2 == 0) {
				if ($$1 > 0) {
					return net.minecraft.core.Direction.UP;
				}

				return net.minecraft.core.Direction.DOWN;
			}
		} else if ($$1 == 0 && $$2 == 0) {
			if ($$0 > 0) {
				return net.minecraft.core.Direction.EAST;
			}
			return net.minecraft.core.Direction.WEST;
		}
		return net.minecraft.core.Direction.getRandom(asVanilla().getRandom());
	}

}